package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.commands.DefaultDrive;
import org.usfirst.frc.team3130.robot.subsystems.Chassis.TurnDirection;
import org.usfirst.frc.team3130.robot.Constants;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class Chassis extends PIDSubsystem {

    // Initialize your subsystem here
	
	//Instance Handling
	private static Chassis m_pInstance;
	public static Chassis GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new Chassis();
		return m_pInstance;
	}
	
	//Create and define necessary objects
	private static DifferentialDrive m_drive;
	private static WPI_TalonSRX m_leftMotorFront;
	private static WPI_TalonSRX m_leftMotorRear;
	private static WPI_TalonSRX m_rightMotorFront;
	private static WPI_TalonSRX m_rightMotorRear;
	private static Solenoid m_shifter;
	private static AHRS m_navX;
	private static Sendable navX;
	private static boolean arcadeDrive;
	
	//Define an enum to control the direction to be turned
	public static enum TurnDirection{kLeft, kRight, kStraight};
	private static TurnDirection m_dir;
	
	//Create and define all standard data types needed
	private static boolean m_bShiftedHigh;
	private static boolean m_bNavXPresent;

	private static double prevSpeedLimit;
	
	public static final double InchesPerRev = ((Constants.kLWheelDiameter + Constants.kRWheelDiameter)/ 2.0) * Math.PI;
	public static double moveSpeed;
	
	//PID Preferences Defaults
	private static final double SUBSYSTEM_CURVE_HIGH_P_DEFAULT = 0.001;
	private static final double SUBSYSTEM_CURVE_HIGH_I_DEFAULT = 0.0;
	private static final double SUBSYSTEM_CURVE_HIGH_D_DEFAULT = 0.0;

	private static final double SUBSYSTEM_CURVE_LOW_P_DEFAULT = 0.018;
	private static final double SUBSYSTEM_CURVE_LOW_I_DEFAULT = 0.02;
	private static final double SUBSYSTEM_CURVE_LOW_D_DEFAULT = 0.125;

	private static final double SUBSYSTEM_STRAIGHT_HIGH_P_DEFAULT = 0.018;
	private static final double SUBSYSTEM_STRAIGHT_HIGH_I_DEFAULT = 0.001;
	private static final double SUBSYSTEM_STRAIGHT_HIGH_D_DEFAULT = 0.1;

	private static final double SUBSYSTEM_STRAIGHT_LOW_P_DEFAULT = 0.018;
	private static final double SUBSYSTEM_STRAIGHT_LOW_I_DEFAULT = 0.001;
	private static final double SUBSYSTEM_STRAIGHT_LOW_I_ZERO = 0.002;
	private static final double SUBSYSTEM_STRAIGHT_LOW_D_DEFAULT = 0.1;
	
	//PID Preferences Defaults
		private static final double TALON_CURVEDRIVE_LOW_POSITION_P_DEFAULT = 0.1;
		private static final double TALON_CURVEDRIVE_LOW_POSITION_I_DEFAULT = 0.0;
		private static final double TALON_CURVEDRIVE_LOW_POSITION_D_DEFAULT = 0.0;
		
		private static final double TALON_CURVEDRIVE_HIGH_POSITION_P_DEFAULT = 0.1;
		private static final double TALON_CURVEDRIVE_HIGH_POSITION_I_DEFAULT = 0.0;
		private static final double TALON_CURVEDRIVE_HIGH_POSITION_D_DEFAULT = 0.0;
		
		private static final double TALON_CURVEDRIVE_LOW_SPEED_P_DEFAULT = 0.1;
		private static final double TALON_CURVEDRIVE_LOW_SPEED_I_DEFAULT = 0.0;
		private static final double TALON_CURVEDRIVE_LOW_SPEED_D_DEFAULT = 0.0;
		
		private static final double TALON_CURVEDRIVE_HIGH_SPEED_P_DEFAULT = 0.1;
		private static final double TALON_CURVEDRIVE_HIGH_SPEED_I_DEFAULT = 0.0;
		private static final double TALON_CURVEDRIVE_HIGH_SPEED_D_DEFAULT = 0.0;
	
    private Chassis() {
    	
        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
    	super(1.0,0,0);
    	
    	m_leftMotorFront = new WPI_TalonSRX(RobotMap.CAN_LEFTMOTORFRONT);
    	m_leftMotorRear = new WPI_TalonSRX(RobotMap.CAN_LEFTMOTORREAR);
    	m_rightMotorFront = new WPI_TalonSRX(RobotMap.CAN_RIGHTMOTORFRONT);
    	m_rightMotorRear = new WPI_TalonSRX(RobotMap.CAN_RIGHTMOTORREAR);
    	
    	m_leftMotorFront.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    	m_rightMotorFront.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    	m_leftMotorFront.setSensorPhase(true); //for compbot
    	m_rightMotorFront.setSensorPhase(false); //for compbot
    	
    	m_leftMotorFront.setNeutralMode(NeutralMode.Brake);
    	m_rightMotorFront.setNeutralMode(NeutralMode.Brake);
    	m_leftMotorRear.set(ControlMode.Follower, RobotMap.CAN_LEFTMOTORFRONT);
    	m_rightMotorRear.set(ControlMode.Follower, RobotMap.CAN_RIGHTMOTORFRONT);
    	m_drive = new DifferentialDrive(m_leftMotorFront, m_rightMotorFront);
    	m_drive.setSafetyEnabled(false);
    	
    	m_shifter = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_SHIFT);
    	m_bShiftedHigh = true;
    	
    	arcadeDrive = true;
    	moveSpeed = 0;
    	
    	try{
			//Connect to navX Gyro on MXP port.
			m_navX = new AHRS(I2C.Port.kMXP);
			m_bNavXPresent = true;
			//navX.setName("Chassis", "NavX");
		} catch(Exception ex){
			//If connection fails log the error and fall back to encoder based angles.
			String str_error = "Error instantiating navX from MXP: ";
			str_error += ex.getLocalizedMessage();
			DriverStation.reportError(str_error, true);
			m_bNavXPresent = false;
		}
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    	setDefaultCommand(new DefaultDrive());
        //setDefaultCommand(new MySpecialCommand());
    }

    //Drive methods for the two forms of control used. Two of each type exist to allow a 2 arg call to default to non-squared inputs
    
    public static void DriveTank(double moveL, double moveR)
    {
    	m_drive.tankDrive(moveL, moveR, false);
    }
    
    public static void DriveArcade(double move, double turn, boolean squaredInputs)
    {
    	m_drive.arcadeDrive(move, turn, squaredInputs);
    }
    
    public static void DriveArcade(double move, double turn)
    {
    	m_drive.arcadeDrive(move, turn, false);
    }
    
    public static WPI_TalonSRX getFrontL(){
    	return m_leftMotorFront;
    }
    
    public static WPI_TalonSRX getFrontR(){
    	return m_rightMotorFront;
    }
    
  //shifts the robot either into high or low gear
    public static void Shift(boolean shiftUp)
    {
    	m_shifter.set(shiftUp);
    	m_bShiftedHigh = shiftUp;
    }
    
    /**
     * Returns the current shift of the robot
     * @return Current shift of the robot
     */
    public static boolean GetShiftedUp(){return m_bShiftedHigh;}
    
    protected double returnPIDInput() {
    	if(m_dir.equals(TurnDirection.kStraight))return GetAngle();
		return GetRate();
    }

    protected void usePIDOutput(double bias) {
    	//TODO: Replace this with a system that works under the curve drive, currently implemented under the else
    	if(m_dir.equals(TurnDirection.kStraight)){	//Maintnance of the Old Drive Straight Angle control.
    		//Chassis ramp rate is the limit on the voltage change per cycle to prevent skidding.
    		final double speedLimit = prevSpeedLimit + Preferences.getInstance().getDouble("ChassisRampRate", 0.25);
   			if (bias >  speedLimit) bias = speedLimit;
   			if (bias < -speedLimit) bias = -speedLimit;
   			double speed_L = moveSpeed+bias;
   			double speed_R = moveSpeed-bias;
			DriveTank(speed_L, speed_R); 
    		prevSpeedLimit = Math.abs(speedLimit);
    		}else{
    			setSpeedTalon(bias);
   			}
    }
    
    public static void TalonsToCoast(boolean coast)
    {
    	if (coast){
	    	m_leftMotorFront.setNeutralMode(NeutralMode.Coast);
			m_leftMotorRear.setNeutralMode(NeutralMode.Coast);
			m_rightMotorFront.setNeutralMode(NeutralMode.Coast);
			m_rightMotorRear.setNeutralMode(NeutralMode.Coast);
    	} else {
    		m_leftMotorFront.setNeutralMode(NeutralMode.Brake);
    		m_leftMotorRear.setNeutralMode(NeutralMode.Brake);
    		m_rightMotorFront.setNeutralMode(NeutralMode.Brake);
    		m_rightMotorRear.setNeutralMode(NeutralMode.Brake);
    	}
    }
    
    public static boolean getArcade(){
    	return arcadeDrive;
    }
    
    public static void setArcade(boolean a){
    	arcadeDrive = a;
    }
    
    /**
     * Returns the current speed of the front left motor
     * @return Current speed of the front left motor (RPS)
     */
    public static double GetSpeedL()
    {
    	// The speed units will be in the sensor's native ticks per 100ms.
    	return 10.0 * m_leftMotorFront.getSelectedSensorVelocity(0) * InchesPerRev / Constants.kDriveCodesPerRev;
    }
    
    /**
     * Returns the current speed of the front right motor
     * @return Current speed of the front right motor (RPS)
     */
    public static double GetSpeedR()
    {
    	// The speed units will be in the sensor's native ticks per 100ms.
    	return 10.0 * m_rightMotorFront.getSelectedSensorVelocity(0) * InchesPerRev / Constants.kDriveCodesPerRev;	
    }
    
    /**
     * Returns the current speed of the robot by averaging the front left and right motors
     * @return Current speed of the robot
     */
    public static double GetSpeed()
    {
    	//The right encoder is nonfunctional, just use the left speed.
    	//return (GetSpeedL() + GetSpeedR())/2.0;
    	return 0.5 * (GetSpeedL() + GetSpeedR());
    }
    
    /**
     * Returns the current voltage being output by the front left talon
     * @return voltage being output by talon in volts
     */
    public static double GetFrontVoltL() {
    	return m_leftMotorFront.get();
    }
    
    /**
     * Returns the current voltage being output by the front right talon
     * @return voltage being output by talon in volts
     */
    public static double GetFrontVoltR() {
    	return m_rightMotorFront.get();
    }
    
    /**
     * Returns the current voltage being output by the rear left talon
     * @return voltage being output by talon in volts
     */
    public static double GetRearVoltL() {
    	return m_leftMotorRear.get();
    }
    
    /**
     * Returns the current voltage being output by the rear right talon
     * @return voltage being output by talon in volts
     */
    public static double GetRearVoltR() {
    	return m_rightMotorRear.get();
    }
    
    /**
     * Returns the current going through the front left talon
     * @return current going through talon, in Amperes
     */
    public static double GetFrontCurrentL() {
    	return m_leftMotorFront.getOutputCurrent();
    }
    
    /**
     * Returns the current going through the front right talon
     * @return current going through talon, in Amperes
     */
    public static double GetFrontCurrentR() {
    	return m_rightMotorFront.getOutputCurrent();
    }
    
    /**
     * Returns the current going through the rear left talon
     * @return current going through talon, in Amperes
     */
    public static double GetRearCurrentL() {
    	return m_leftMotorRear.getOutputCurrent();
    }
    
    /**
     * Returns the current going through the rear right talon
     * @return current going through talon, in Amperes
     */
    public static double GetRearCurrentR() {
    	return m_rightMotorRear.getOutputCurrent();
    }
    
    /**
	 * Returns the current angle of the robot, usually from the navX
	 * @return current angle of the robot
	 */
    
	public static double GetAngle()
	{
		if(m_bNavXPresent)
		{
			//Angle use wants a faster, more accurate, but drifting angle, for quick use.
			return m_navX.getAngle();
		}else {
			//Means that angle use wants a driftless angle measure that lasts.
			return ( GetDistanceR() - GetDistanceL() ) * 180 / (Constants.kChassisWidth * Math.PI);
			/*
			 *  Angle is 180 degrees times encoder difference over Pi * the distance between the wheels
			 *	Made from geometry and relation between angle fraction and arc fraction with semicircles.
			 */
		}
	}
	
	/**
	 * Returns the current rate of change of the robots heading
	 * 
	 * <p> GetRate() returns the rate of change of the angle the robot is facing, 
	 * with a return of negative one if the gyro isn't present on the robot, 
	 * as calculating the rate of change of the angle using encoders is not currently being done.
	 * @return the rate of change of the heading of the robot.
	 */
	public static double GetRate()
	{
		if(m_bNavXPresent) return m_navX.getRate();
		return -1;
	}
	
	/**
	 * Returns the current error of the system (how far away the robot is from where we want it to be)
	 * @return Current PID error of the system
	 */
	public static double GetPIDError()
	{
		return GetInstance().getSetpoint() - GetInstance().getPosition();
	}
	
	public static void ReleaseAngle()
	{
		GetInstance().getPIDController().disable();
		GetInstance().getPIDController().reset();
		prevSpeedLimit = 0;
		m_leftMotorFront.set(ControlMode.PercentOutput, RobotMap.CAN_LEFTMOTORFRONT);
		m_rightMotorFront.set(ControlMode.PercentOutput, RobotMap.CAN_RIGHTMOTORFRONT);
	}
	
	/**
	 * 
	 * @return Current distance of the front left motor in inches
	 */
	public static double GetDistanceL()
	{
		return (m_leftMotorFront.getSelectedSensorPosition(0)/Constants.kDriveCodesPerRev) * InchesPerRev ;
	}
	
	/**
	 * 
	 * @return Current distance of the front right motor in inches
	 */
	public static double GetDistanceR()
	{
		return (m_rightMotorFront.getSelectedSensorPosition(0)/Constants.kDriveCodesPerRev) * InchesPerRev;
	}
	
	public static double GetDistance()
	{
		//Returns the average of the left and right distances
		return (GetDistanceL() + GetDistanceR()) / 2.0;
	}
	
	//Shouldn't be used unless absolutely necessary, takes an excessive amount of time to run
	public static void ResetEncoders()
	{
		m_leftMotorFront.setSelectedSensorPosition(0, 0, 20);
		m_rightMotorFront.setSelectedSensorPosition(0, 0, 20);
	}
	
	public static void SetPIDValues()
	{
		if(m_bShiftedHigh){
			if(m_dir.equals(TurnDirection.kStraight)){
				GetInstance().getPIDController().setPID(
					Preferences.getInstance().getDouble("ChassisHighP",SUBSYSTEM_STRAIGHT_HIGH_P_DEFAULT),
					Preferences.getInstance().getDouble("ChassisHighI",SUBSYSTEM_STRAIGHT_HIGH_I_DEFAULT),
					Preferences.getInstance().getDouble("ChassisHighD",SUBSYSTEM_STRAIGHT_HIGH_D_DEFAULT)
				);
			}else{
				GetInstance().getPIDController().setPID(
					Preferences.getInstance().getDouble("Chassis High Curve P",SUBSYSTEM_CURVE_HIGH_P_DEFAULT),
					Preferences.getInstance().getDouble("Chassis High Curve I",SUBSYSTEM_CURVE_HIGH_I_DEFAULT),
					Preferences.getInstance().getDouble("Chassis High Curve D",SUBSYSTEM_CURVE_HIGH_D_DEFAULT)
				);
			}
		}else{
			if(m_dir.equals(TurnDirection.kStraight)){
				GetInstance().getPIDController().setPID(
					Preferences.getInstance().getDouble("ChassisLowP",SUBSYSTEM_STRAIGHT_LOW_P_DEFAULT),
					Preferences.getInstance().getDouble("ChassisLowI",SUBSYSTEM_STRAIGHT_LOW_I_DEFAULT),
					Preferences.getInstance().getDouble("ChassisLowD",SUBSYSTEM_STRAIGHT_LOW_D_DEFAULT)
				);
			}else{
				GetInstance().getPIDController().setPID(
					Preferences.getInstance().getDouble("Chassis Low Curve P",SUBSYSTEM_CURVE_LOW_P_DEFAULT),
					Preferences.getInstance().getDouble("Chassis Low Curve I",SUBSYSTEM_CURVE_LOW_I_DEFAULT),
					Preferences.getInstance().getDouble("Chassis Low Curve D",SUBSYSTEM_CURVE_LOW_D_DEFAULT)
				);
			}
		}
	}
	
	/**
	 * 
	 * @param angle  in radians
	 */
	public static void HoldAngle(double angle)
	{
		double workingAngle = (180/Math.PI)*angle;
		SetPIDValues();
		if(m_dir.equals(TurnDirection.kStraight))
			GetInstance().getPIDController().setSetpoint(GetAngle() + workingAngle);
		else
			GetInstance().getPIDController().setSetpoint(workingAngle);
		GetInstance().getPIDController().enable();
	}
	
	/**
	 * 
	 * @param angle  in degrees
	 */
	public static void HoldAbsAngle(double angle)
	{
		double workingAngle = (180/Math.PI)*angle;
		SetPIDValues();
		if(m_dir.equals(TurnDirection.kStraight))
			GetInstance().getPIDController().setSetpoint(workingAngle);
		else
			GetInstance().getPIDController().setSetpoint(workingAngle);
		GetInstance().getPIDController().enable();
	}
	
	/**
	 * Sets the control modes and switches the mode of the subsystem for turning
	 * 
	 * <p>
	 * Sets the PID for both talons, changes the control mode for the subsystem, and changes the control mode of the talons.
	 * @param dir the direction that the robot will be turning
	 */
	public static void setTurnDir(TurnDirection dir)
	{
		m_dir = dir;
		if(dir.equals(TurnDirection.kLeft)){
			m_leftMotorFront.set(ControlMode.Velocity, RobotMap.CAN_LEFTMOTORFRONT);
			m_rightMotorFront.set(ControlMode.Position, RobotMap.CAN_RIGHTMOTORFRONT);
		}else if(dir.equals(TurnDirection.kRight)){
			m_rightMotorFront.set(ControlMode.Velocity, RobotMap.CAN_RIGHTMOTORFRONT);
			m_leftMotorFront.set(ControlMode.Position, RobotMap.CAN_LEFTMOTORFRONT);
		}else{
			//TODO: Rethink this area, it was done on a whim to make a pseudo functioning system
			m_leftMotorFront.set(ControlMode.Velocity, RobotMap.CAN_LEFTMOTORFRONT);
			m_rightMotorFront.set(ControlMode.Velocity, RobotMap.CAN_RIGHTMOTORFRONT);
		}
		setTalonPID();
	}
	
	/**
	 * Calls the .set() function on the Position talon
	 * <p>
	 * Sets the appropriate output on the talon, depending on the mode.
	 * </p>
	 * 
	 * <p>In PercentVbus, the output is between -1.0 and 1.0, with 0.0 as stopped. 
	 * <br/>In Follower mode, the output is the integer device ID of the talon to duplicate. 
	 * <br/>In Voltage mode, outputValue is in volts. In Current mode, outputValue is in amperes. 
	 * <br/>In Speed mode, outputValue is in position change / 10ms. 
	 * <br/><b>In Position mode, outputValue is in encoder ticks or an analog value, depending on the sensor.</b></p>
	 * 
	 * @param set - The setpoint value, as described above.
	 */
	public static void setPositionTalon(double set)
	{
		if(m_dir.equals(TurnDirection.kRight))
			m_leftMotorFront.set(set);
		else
		if(m_dir.equals(TurnDirection.kLeft))
			m_rightMotorFront.set(set);
	}
	
	
	/**
	 * Calls the .set() function on the Speed talon
	 * <p>
	 * Sets the appropriate output on the talon, depending on the mode.
	 * </p>
	 * 
	 * <p>In PercentVbus, the output is between -1.0 and 1.0, with 0.0 as stopped. 
	 * <br/>In Follower mode, the output is the integer device ID of the talon to duplicate. 
	 * <br/>In Voltage mode, outputValue is in volts. In Current mode, outputValue is in amperes. 
	 * <br/><b>In Speed mode, outputValue is in position change / 10ms.</b> 
	 * <br/>In Position mode, outputValue is in encoder ticks or an analog value, depending on the sensor.</p>
	 * 
	 * @param set - The setpoint value, as described above.
	 */
	public static void setSpeedTalon(double set)
	{
		if(m_dir.equals(TurnDirection.kRight))
			m_rightMotorFront.set(set);
		else
		if(m_dir.equals(TurnDirection.kLeft))
			m_leftMotorFront.set(set);
	}
	
	/**
	 * Returns the difference between the setpoint and the current position
	 * @return the error on the position talon returns negative 1 in in straight
	 */
	public static double getPositionTalonError()
	{
		if(m_dir.equals(TurnDirection.kRight)) return m_leftMotorFront.getClosedLoopError(0);
		else if(m_dir.equals(TurnDirection.kLeft))return m_rightMotorFront.getClosedLoopError(0);
		return -1;
	}
	
	/**
	 * Returns the difference between the setpoint and the current position
	 * @return the error on the speed talon, returns negative 1 if in straight mode
	 */
	public static double getSpeedTalonError()
	{
		if(m_dir.equals(TurnDirection.kRight)) return m_rightMotorFront.getClosedLoopError(0);
		else if(m_dir.equals(TurnDirection.kLeft))return m_leftMotorFront.getClosedLoopError(0);
		return -1;
	}
	
	/**
	 * Returns the current speed of the postion talon
	 * 
	 * Determines which side is being driven in position mode, and returns the speed of that side. Returns -1 if in kStraight mode
	 * @return the speed of the position talon
	 */
	public static double getPositionTalonSpeed()
	{
		if(m_dir.equals(TurnDirection.kRight)) return m_leftMotorFront.getSelectedSensorVelocity(0);
		else if(m_dir.equals(TurnDirection.kLeft))return m_rightMotorFront.getSelectedSensorVelocity(0);
		return -1;
	}
	
	/**
	 * Sets the PID Values of both talons
	 * <p>The PID Values to be used can be defined in Preferences, and have defaults set in the constants at the top of Chassis.
	 * The PID Values can differ for high and low gear, as well as for if the talons are in speed or position mode, determined by 
	 * turn direction.
	 */
	private static void setTalonPID()
	{
		if(m_bShiftedHigh){
			switch(m_dir){
			case kLeft:
				m_rightMotorFront.config_kP(0, Preferences.getInstance().getDouble("CurveDrive Low Position P", 
						TALON_CURVEDRIVE_LOW_POSITION_P_DEFAULT), 100);
				m_rightMotorFront.config_kI(0, Preferences.getInstance().getDouble("CurveDrive Low Position I", 
						TALON_CURVEDRIVE_LOW_POSITION_I_DEFAULT), 100);
				m_rightMotorFront.config_kD(0, Preferences.getInstance().getDouble("CurveDrive Low Position D", 
						TALON_CURVEDRIVE_LOW_POSITION_D_DEFAULT), 100);
				m_leftMotorFront.config_kP(0, Preferences.getInstance().getDouble("CurveDrive Low Position P", 
						TALON_CURVEDRIVE_LOW_SPEED_P_DEFAULT), 100);
				m_leftMotorFront.config_kI(0, Preferences.getInstance().getDouble("CurveDrive Low Position I", 
						TALON_CURVEDRIVE_LOW_SPEED_I_DEFAULT), 100);
				m_leftMotorFront.config_kD(0, Preferences.getInstance().getDouble("CurveDrive Low Position D", 
						TALON_CURVEDRIVE_LOW_SPEED_D_DEFAULT), 100);
				break;
			case kRight:
				m_rightMotorFront.config_kP(0, Preferences.getInstance().getDouble("CurveDrive Low Position P", 
						TALON_CURVEDRIVE_LOW_SPEED_P_DEFAULT), 100);
				m_rightMotorFront.config_kI(0, Preferences.getInstance().getDouble("CurveDrive Low Position I", 
						TALON_CURVEDRIVE_LOW_SPEED_I_DEFAULT), 100);
				m_rightMotorFront.config_kD(0, Preferences.getInstance().getDouble("CurveDrive Low Position D", 
						TALON_CURVEDRIVE_LOW_SPEED_D_DEFAULT), 100);
				m_leftMotorFront.config_kP(0, Preferences.getInstance().getDouble("CurveDrive Low Position P", 
						TALON_CURVEDRIVE_LOW_POSITION_P_DEFAULT), 100);
				m_leftMotorFront.config_kI(0, Preferences.getInstance().getDouble("CurveDrive Low Position I", 
						TALON_CURVEDRIVE_LOW_POSITION_I_DEFAULT), 100);
				m_leftMotorFront.config_kD(0, Preferences.getInstance().getDouble("CurveDrive Low Position D", 
						TALON_CURVEDRIVE_LOW_POSITION_D_DEFAULT), 100);
				break;
			default:
				break;
			}
		}else{
			switch(m_dir){
			case kLeft:
				m_rightMotorFront.config_kP(0, Preferences.getInstance().getDouble("CurveDrive Low Position P", 
						TALON_CURVEDRIVE_HIGH_POSITION_P_DEFAULT), 100);
				m_rightMotorFront.config_kI(0, Preferences.getInstance().getDouble("CurveDrive Low Position I", 
						TALON_CURVEDRIVE_HIGH_POSITION_I_DEFAULT), 100);
				m_rightMotorFront.config_kD(0, Preferences.getInstance().getDouble("CurveDrive Low Position D", 
						TALON_CURVEDRIVE_HIGH_POSITION_D_DEFAULT), 100);
				m_leftMotorFront.config_kP(0, Preferences.getInstance().getDouble("CurveDrive Low Position P", 
						TALON_CURVEDRIVE_HIGH_SPEED_P_DEFAULT), 100);
				m_leftMotorFront.config_kI(0, Preferences.getInstance().getDouble("CurveDrive Low Position I", 
						TALON_CURVEDRIVE_HIGH_SPEED_I_DEFAULT), 100);
				m_leftMotorFront.config_kD(0, Preferences.getInstance().getDouble("CurveDrive Low Position D", 
						TALON_CURVEDRIVE_HIGH_SPEED_D_DEFAULT), 100);
				break;
			case kRight:
				m_rightMotorFront.config_kP(0, Preferences.getInstance().getDouble("CurveDrive Low Position P", 
						TALON_CURVEDRIVE_HIGH_SPEED_P_DEFAULT), 100);
				m_rightMotorFront.config_kI(0, Preferences.getInstance().getDouble("CurveDrive Low Position I", 
						TALON_CURVEDRIVE_HIGH_SPEED_I_DEFAULT), 100);
				m_rightMotorFront.config_kD(0, Preferences.getInstance().getDouble("CurveDrive Low Position D", 
						TALON_CURVEDRIVE_HIGH_SPEED_D_DEFAULT), 100);
				m_leftMotorFront.config_kP(0, Preferences.getInstance().getDouble("CurveDrive Low Position P", 
						TALON_CURVEDRIVE_HIGH_POSITION_P_DEFAULT), 100);
				m_leftMotorFront.config_kI(0, Preferences.getInstance().getDouble("CurveDrive Low Position I", 
						TALON_CURVEDRIVE_HIGH_POSITION_I_DEFAULT), 100);
				m_leftMotorFront.config_kD(0, Preferences.getInstance().getDouble("CurveDrive Low Position D", 
						TALON_CURVEDRIVE_HIGH_POSITION_D_DEFAULT), 100);
				break;
			default:
				break;
			}
		}
	}
	
	
	public static void DriveStraight(double move){ 
		moveSpeed = move; 
	}
	
	public static void toMotionProfileMode(){
		
	}
}
