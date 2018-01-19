package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.commands.DefaultDrive;
import org.usfirst.frc.team3130.robot.subsystems.Chassis.TurnDirection;
import org.usfirst.frc.team3130.robot.Constants;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

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
	private static boolean arcadeDrive;
	
	//Define an enum to control the direction to be turned
	public static enum TurnDirection{kLeft, kRight, kStraight};
	private static TurnDirection m_dir;
	
	//Create and define all standard data types needed
	private static boolean m_bShiftedHigh;
	private static boolean m_bNavXPresent;
	
	public static final double InchesPerRev = ((Constants.kLWheelDiameter * Constants.kRWheelDiameter)/ 2.0) * Math.PI;
	
	//PID Preferences Defaults
	private static final double SUBSYSTEM_CURVE_HIGH_P_DEFAULT = 0.075;
	private static final double SUBSYSTEM_CURVE_HIGH_I_DEFAULT = 0.01;
	private static final double SUBSYSTEM_CURVE_HIGH_D_DEFAULT = 0.09;

	private static final double SUBSYSTEM_CURVE_LOW_P_DEFAULT = 0.085;
	private static final double SUBSYSTEM_CURVE_LOW_I_DEFAULT = 0.02;
	private static final double SUBSYSTEM_CURVE_LOW_D_DEFAULT = 0.125;

	private static final double SUBSYSTEM_STRAIGHT_HIGH_P_DEFAULT = 0.075;
	private static final double SUBSYSTEM_STRAIGHT_HIGH_I_DEFAULT = 0.01;
	private static final double SUBSYSTEM_STRAIGHT_HIGH_D_DEFAULT = 0.09;

	private static final double SUBSYSTEM_STRAIGHT_LOW_P_DEFAULT = 0.085;
	private static final double SUBSYSTEM_STRAIGHT_LOW_I_DEFAULT = 7.0E-7;
	private static final double SUBSYSTEM_STRAIGHT_LOW_I_ZERO = 0.002;
	private static final double SUBSYSTEM_STRAIGHT_LOW_D_DEFAULT = 0.125;
	
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
    	
    	m_leftMotorRear.set(ControlMode.Follower, RobotMap.CAN_LEFTMOTORFRONT);
    	m_rightMotorRear.set(ControlMode.Follower, RobotMap.CAN_RIGHTMOTORFRONT);
    	m_drive = new DifferentialDrive(m_leftMotorFront, m_rightMotorFront);
    	m_drive.setSafetyEnabled(false);
    	
    	arcadeDrive = true;
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
    public static boolean GetShiftedDown(){return m_bShiftedHigh;}
    
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        return 0.0;
    }

    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);
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
     * @return Current speed of the front left motor (unknown units)
     */
    public static double GetSpeedL()
    {
    	// The speed units will be in the sensor's native ticks per 100ms.
    	return 10.0 * m_leftMotorFront.get() * InchesPerRev / RobotMap.RATIO_DRIVECODESPERREV;
    }
    
    /**
     * Returns the current speed of the front right motor
     * @return Current speed of the front right motor (unknown units)
     */
    public static double GetSpeedR()
    {
    	// The speed units will be in the sensor's native ticks per 100ms.
    	return 10.0 * m_rightMotorFront.get() * InchesPerRev / RobotMap.RATIO_DRIVECODESPERREV;	
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
			return -m_navX.getAngle();
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
	 * 
	 * @return Current distance of the front left motor in inches
	 */
	public static double GetDistanceL()
	{
		return m_leftMotorFront.get() * InchesPerRev;
	}
	
	/**
	 * 
	 * @return Current distance of the front right motor in inches
	 */
	public static double GetDistanceR()
	{
		return m_rightMotorFront.get() * InchesPerRev;
	}
	
	public static double GetDistance()
	{
		//Returns the average of the left and right speeds
		return (GetDistanceL() + GetDistanceR()) / 2.0;
	}
	
	//Shouldn't be used unless absolutely necessary, takes an excessive amount of time to run
	public static void ResetEncoders()
	{
		m_leftMotorFront.set(0);
		m_rightMotorFront.set(0);
	}
	
	public static void SetPIDValues(double angle)
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
				/*GetInstance().getPIDController().setPID(
					Preferences.getInstance().getDouble("ChassisLowP",SUBSYSTEM_STRAIGHT_LOW_P_DEFAULT),
					GetI(angle),
					Preferences.getInstance().getDouble("ChassisLowD",SUBSYSTEM_STRAIGHT_LOW_D_DEFAULT)
				);*/
			}else{
				GetInstance().getPIDController().setPID(
					Preferences.getInstance().getDouble("Chassis Low Curve P",SUBSYSTEM_CURVE_LOW_P_DEFAULT),
					Preferences.getInstance().getDouble("Chassis Low Curve I",SUBSYSTEM_CURVE_LOW_I_DEFAULT),
					Preferences.getInstance().getDouble("Chassis Low Curve D",SUBSYSTEM_CURVE_LOW_D_DEFAULT)
				);
			}
		}
	}
	
	public static void HoldAngle(double angle)
	{
		double workingAngle = (180/Math.PI)*angle;
		SetPIDValues(workingAngle);
		if(m_dir.equals(TurnDirection.kStraight))
			GetInstance().getPIDController().setSetpoint(GetAngle() + workingAngle);
		else
			GetInstance().getPIDController().setSetpoint(workingAngle);
		GetInstance().getPIDController().enable();
	}
	
	public static void HoldAbsAngle(double angle)
	{
		double workingAngle = (180/Math.PI)*angle;
		SetPIDValues(workingAngle);
		if(m_dir.equals(TurnDirection.kStraight))
			GetInstance().getPIDController().setSetpoint(workingAngle);
		else
			GetInstance().getPIDController().setSetpoint(workingAngle);
		GetInstance().getPIDController().enable();
	}
	
	/**
	 * Sets the PID Values of both talons
	 * <p>The PID Values to be used can be defined in Preferences, and have defaults set in the constants at the top of Chassis.
	 * The PID Values can differ for high and low gear, as well as for if the talons are in speed or position mode, determined by 
	 * turn direction.
	 */
	private static void setTalonPID()
	{
		/*if(m_bShiftedHigh){
			switch(m_dir){
			case kLeft:
				m_rightMotorFront.setPID(
						Preferences.getInstance().getDouble("CurveDrive Low Position P", TALON_CURVEDRIVE_LOW_POSITION_P_DEFAULT), 
						Preferences.getInstance().getDouble("CurveDrive Low Position I", TALON_CURVEDRIVE_LOW_POSITION_I_DEFAULT),
						Preferences.getInstance().getDouble("CurveDrive Low Position D", TALON_CURVEDRIVE_LOW_POSITION_D_DEFAULT)
				);
				m_leftMotorFront.setPID(
						Preferences.getInstance().getDouble("CurveDrive Low Speed P", TALON_CURVEDRIVE_LOW_SPEED_P_DEFAULT), 
						Preferences.getInstance().getDouble("CurveDrive Low Speed I", TALON_CURVEDRIVE_LOW_SPEED_I_DEFAULT),
						Preferences.getInstance().getDouble("CurveDrive Low Speed D", TALON_CURVEDRIVE_LOW_SPEED_D_DEFAULT)
				);
				break;
			case kRight:
				m_leftMotorFront.setPID(
						Preferences.getInstance().getDouble("CurveDrive Low Position P", TALON_CURVEDRIVE_LOW_POSITION_P_DEFAULT), 
						Preferences.getInstance().getDouble("CurveDrive Low Position I", TALON_CURVEDRIVE_LOW_POSITION_I_DEFAULT),
						Preferences.getInstance().getDouble("CurveDrive Low Position D", TALON_CURVEDRIVE_LOW_POSITION_D_DEFAULT)
				);
				m_rightMotorFront.setPID(
						Preferences.getInstance().getDouble("CurveDrive Low Speed P", TALON_CURVEDRIVE_LOW_SPEED_P_DEFAULT), 
						Preferences.getInstance().getDouble("CurveDrive Low Speed I", TALON_CURVEDRIVE_LOW_SPEED_I_DEFAULT),
						Preferences.getInstance().getDouble("CurveDrive Low Speed D", TALON_CURVEDRIVE_LOW_SPEED_D_DEFAULT)
				);
				break;
			default:
				break;
			}
		}else{
			switch(m_dir){
			case kLeft:
				m_rightMotorFront.setPID(
						Preferences.getInstance().getDouble("CurveDrive High Position P", TALON_CURVEDRIVE_HIGH_POSITION_P_DEFAULT), 
						Preferences.getInstance().getDouble("CurveDrive High Position I", TALON_CURVEDRIVE_HIGH_POSITION_I_DEFAULT),
						Preferences.getInstance().getDouble("CurveDrive High Position D", TALON_CURVEDRIVE_HIGH_POSITION_D_DEFAULT)
				);
				m_leftMotorFront.setPID(
						Preferences.getInstance().getDouble("CurveDrive High Speed P", TALON_CURVEDRIVE_HIGH_SPEED_P_DEFAULT), 
						Preferences.getInstance().getDouble("CurveDrive High Speed I", TALON_CURVEDRIVE_HIGH_SPEED_I_DEFAULT),
						Preferences.getInstance().getDouble("CurveDrive High Speed D", TALON_CURVEDRIVE_HIGH_SPEED_D_DEFAULT)
				);
				break;
			case kRight:
				m_leftMotorFront.setPID(
						Preferences.getInstance().getDouble("CurveDrive High Position P", TALON_CURVEDRIVE_HIGH_POSITION_P_DEFAULT), 
						Preferences.getInstance().getDouble("CurveDrive High Position I", TALON_CURVEDRIVE_HIGH_POSITION_I_DEFAULT),
						Preferences.getInstance().getDouble("CurveDrive High Position D", TALON_CURVEDRIVE_HIGH_POSITION_D_DEFAULT)
				);
				m_rightMotorFront.setPID(
						Preferences.getInstance().getDouble("CurveDrive High Speed P", TALON_CURVEDRIVE_HIGH_SPEED_P_DEFAULT), 
						Preferences.getInstance().getDouble("CurveDrive High Speed I", TALON_CURVEDRIVE_HIGH_SPEED_I_DEFAULT),
						Preferences.getInstance().getDouble("CurveDrive High Speed D", TALON_CURVEDRIVE_HIGH_SPEED_D_DEFAULT)
				);
				break;
			default:
				break;
			}
		}*/
	}
	
	
}
