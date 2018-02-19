package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.commands.RunElevator;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This subsystem controls the power cube elevator of the robot
 */
public class Elevator extends Subsystem {
	
	private static WPI_TalonSRX elevator;
	private static WPI_TalonSRX elevator2;
	
	//Instance Handling
	private static Elevator m_pInstance;
	public static Elevator GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new Elevator();
		return m_pInstance;
	}
	
	private Elevator() {
		elevator = new WPI_TalonSRX(RobotMap.CAN_ELEVATOR1);
		elevator2 = new WPI_TalonSRX(RobotMap.CAN_ELEVATOR2);
		elevator.setNeutralMode(NeutralMode.Brake);
		elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		elevator.set(ControlMode.PercentOutput, 0);
		
		elevator.overrideLimitSwitchesEnable(true);
		elevator.overrideSoftLimitsEnable(false);
		elevator.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
		elevator.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
		elevator.configForwardSoftLimitThreshold(Constants.kElevatorSoftMax, 0);//in ticks
		elevator.configReverseSoftLimitThreshold(Constants.kElevatorSoftMin, 0);//in ticks
		elevator.configSetParameter(ParamEnum.eClearPositionOnLimitR, 1, 0, 0, 10);

		elevator.config_kP(0, Constants.kElevatorP, 0);
		elevator.config_kI(0, Constants.kElevatorI, 0);
		elevator.config_kD(0, Constants.kElevatorD, 0);
		elevator.config_kF(0, Constants.kElevatorF, 0);
		elevator2.set(ControlMode.Follower, RobotMap.CAN_ELEVATOR1);
	}

    public void initDefaultCommand() {
    	setDefaultCommand(new RunElevator());
    }

    public static void runElevator(double percent) {
    	boolean goingDown = percent < 0;

    	// Offset the power by a bias to counteract the gravity
    	percent += Constants.kElevatorBias;

    	// At the bottom (if the height is lower than the safety zone) throttle down the power
    	double height = getHeight();
    	double zone = Constants.kElevatorZone;
    	if(goingDown && height < zone) {
    		percent *= Math.abs(height / zone);
    	}

    	elevator.set(ControlMode.PercentOutput, percent);
    }

    public synchronized static void setHeight(double height_inches){
    	elevator.set(ControlMode.Position, height_inches * Constants.kElevatorTicksPerInch);
    }

    public synchronized static double getHeight(){
    	return elevator.getSelectedSensorPosition(0) / Constants.kElevatorTicksPerInch; //Returns height in inches
    }

    public static void outputToSmartDashboard() {
    	SmartDashboard.putNumber("Elev_Height", getHeight());
    	SmartDashboard.putNumber("elev_m1current", elevator.getOutputCurrent() );
    	SmartDashboard.putNumber("elev_m2current", elevator2.getOutputCurrent() );
    }
}

