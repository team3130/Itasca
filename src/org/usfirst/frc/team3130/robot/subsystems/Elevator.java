package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.commands.RunElevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This subsystem controls the power cube elevator of the robot
 */
public class Elevator extends Subsystem {
	
	private static WPI_TalonSRX elevator;
	private static WPI_TalonSRX elevator2;
	private static boolean zeroed;
	
	//Instance Handling
	private static Elevator m_pInstance;
	public static Elevator GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new Elevator();
		return m_pInstance;
	}
	private static final int MAX_VELOCITY = 6300; // 1024
    private static final int MAX_ACCELERATION = 6100; // 1024
    private static final int MAX_VELOCITY_DOWN = (int) (MAX_VELOCITY * 0.45); // 1024
    private static final int MAX_ACCELERATION_DOWN = (int) (MAX_ACCELERATION * 0.4); // 1024
    
	private Elevator() {
		
		zeroed = false;
		
		elevator = new WPI_TalonSRX(RobotMap.CAN_ELEVATOR1);
		elevator2 = new WPI_TalonSRX(RobotMap.CAN_ELEVATOR2);
		elevator.setNeutralMode(NeutralMode.Brake);
		elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		elevator.set(ControlMode.PercentOutput, 0);
		
		
		elevator.overrideLimitSwitchesEnable(true);
		elevator.overrideSoftLimitsEnable(false);
		elevator.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
		elevator.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
		//elevator.configForwardSoftLimitThreshold(Constants.kElevatorSoftMax, 0);//in ticks
		//elevator.configReverseSoftLimitThreshold(Constants.kElevatorSoftMin, 0);//in ticks

		elevator.config_kP(0, 0.3, 0);
		elevator.config_kI(0, RobotMap.kElevatorI, 0);
		elevator.config_kD(0, RobotMap.kElevatorD, 0);
		elevator.config_kF(0, RobotMap.kElevatorF, 0);
		elevator2.set(ControlMode.Follower, RobotMap.CAN_ELEVATOR1);
	}

    public void initDefaultCommand() {
    	setDefaultCommand(new RunElevator());
    }

    public static void runElevator(double percent) {
    	
    	boolean goingDown = percent < 0;

    	// Offset the power by a bias to counteract the gravity
    	percent += RobotMap.kElevatorBias;

    	// At the bottom (if the height is lower than the safety zone) throttle down the power
    	double height = getHeight();
    	double zone = RobotMap.kElevatorZone;
    	if(goingDown) {
    		// Going down, so reduce the power
    		percent *= Preferences.getInstance().getDouble("ElevatorDown", 0.9);
    		if(height < zone) {
    			// And at the bottom reduce the power even more, aggressively squared
    			double ratio = Math.abs(height / zone);
        		percent *= ratio * ratio;
    		}
    	}

    	elevator.set(ControlMode.PercentOutput, percent);
    }

    public synchronized static void setHeight(double height_inches){
    	elevator.set(ControlMode.PercentOutput, 0);
    	if(elevator.getSelectedSensorPosition(0) >= RobotMap.kElevatorTicksPerInch * height_inches){
    		configMotionMagic(MAX_VELOCITY_DOWN, MAX_ACCELERATION_DOWN);
    	}else{
    		configMotionMagic(MAX_VELOCITY, MAX_ACCELERATION);
    	}
    	elevator.set(ControlMode.MotionMagic, RobotMap.kElevatorTicksPerInch * height_inches);
    	
    }
    public static void configMotionMagic(int cruiseVelocity, int acceleration){
    	elevator.configMotionCruiseVelocity(cruiseVelocity, 0);
    	elevator.configMotionAcceleration(acceleration, 0);
    }

    public synchronized static double getHeight(){
    	return elevator.getSelectedSensorPosition(0) / RobotMap.kElevatorTicksPerInch; //Returns height in inches
    }

    /**
     * Move the elevator to a relative amount
     * @param offset the offset in inches from the current height, positive is up
     */
    public static void addHeight(double offset) {
    	double newHeight = getHeight() + offset;
    	// If the elevator is (almost) at the bottom then just turn it off
    	if(newHeight < RobotMap.ElevatorBottom) {
    		elevator.set(ControlMode.PercentOutput, 0);
    	}
    	else {
    		setHeight(newHeight);
    	}
    }

    /**
     * Hold the current height by PID closed loop
     */
    public static void holdHeight() {
    	setHeight(getHeight());
    }
    public static void resetElevator(){
    	//elevator.set(ControlMode.MotionMagic, 0.0);
    	elevator.set(ControlMode.PercentOutput, 0.0);
    }

    public static void outputToSmartDashboard() {
    	//SmartDashboard.putNumber("elevator_velocity", elevator.getSelectedSensorVelocity(0));
    	SmartDashboard.putNumber("Elev_Height", getHeight());
    	SmartDashboard.putNumber("elev_m1current", elevator.getOutputCurrent() );
    	SmartDashboard.putNumber("elev_m2current", elevator2.getOutputCurrent() );
    	
    	SmartDashboard.putBoolean("Elev_Rev_Switch",elevator.getSensorCollection().isRevLimitSwitchClosed());
    	SmartDashboard.putBoolean("elev_Fwd_Switch", elevator.getSensorCollection().isFwdLimitSwitchClosed());
    	
    	//Zero Handling
    	if(elevator.getSensorCollection().isRevLimitSwitchClosed()){
    		if(!zeroed){
        		elevator.setSelectedSensorPosition(0, 0, 0);
        		setHeight(0.0);
        		DriverStation.reportWarning("Elevator is Zero!", false);
        		zeroed = true;
    		}
    	}
    	else{
    		if(zeroed)
    			zeroed = false;
    	}
    }

}

