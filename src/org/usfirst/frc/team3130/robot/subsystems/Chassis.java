package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.commands.DefaultDrive;

import java.io.File;

import org.usfirst.frc.team3130.robot.Constants;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

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
	
	//Create and define all standard data types needed
	private static boolean m_bShiftedHigh;
	private static boolean m_bNavXPresent;

	private static double prevSpeedLimit;
	
	public static final double InchesPerRev = ((Constants.kLWheelDiameter + Constants.kRWheelDiameter)/ 2.0) * Math.PI;
	public static double moveSpeed;
	
	//PID Preferences Defaults
	private static final double SUBSYSTEM_STRAIGHT_HIGH_P_DEFAULT = 0.018;
	private static final double SUBSYSTEM_STRAIGHT_HIGH_I_DEFAULT = 0.0;
	private static final double SUBSYSTEM_STRAIGHT_HIGH_D_DEFAULT = 0.062;

	private static final double SUBSYSTEM_STRAIGHT_LOW_P_DEFAULT = 0.018;
	private static final double SUBSYSTEM_STRAIGHT_LOW_I_DEFAULT = 0.001;
	private static final double SUBSYSTEM_STRAIGHT_LOW_D_DEFAULT = 0.1;
	
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
			m_navX = new AHRS(SPI.Port.kMXP);
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
    public static void ShiftDown(boolean shiftDown)
    {
    	m_shifter.set(shiftDown); //On practice robot false shiftUp is high gear
    	m_bShiftedHigh = !shiftDown;
    }
    
    /**
     * Returns the current shift of the robot
     * @return Current shift of the robot
     */
    public static boolean GetShiftedUp(){return m_bShiftedHigh;}
    
    protected double returnPIDInput() {
    	return GetAngle();
    }

    protected void usePIDOutput(double bias) {
    	//Chassis ramp rate is the limit on the voltage change per cycle to prevent skidding.
    	final double speedLimit = prevSpeedLimit + Preferences.getInstance().getDouble("ChassisRampRate", 0.25);
    	if (bias >  speedLimit) bias = speedLimit;
    	if (bias < -speedLimit) bias = -speedLimit;
    	double speed_L = moveSpeed+bias;
    	double speed_R = moveSpeed-bias;
    	DriveTank(speed_L, speed_R); 
    	prevSpeedLimit = Math.abs(speedLimit);
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
		m_leftMotorFront.set(ControlMode.PercentOutput, 0);
		m_rightMotorFront.set(ControlMode.PercentOutput, 0);
	}
	
	/**
	 * 
	 * @return Current distance of the front left motor in inches
	 */
	public static double GetDistanceL()
	{
		return -1*(m_leftMotorFront.getSensorCollection().getQuadraturePosition()/Constants.kDriveCodesPerRev) * InchesPerRev ;
	}
	
	/**
	 * 
	 * @return Current distance of the front right motor in inches
	 */
	public static double GetDistanceR()
	{
		return (m_rightMotorFront.getSensorCollection().getQuadraturePosition()/Constants.kDriveCodesPerRev) * InchesPerRev;
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
			GetInstance().getPIDController().setPID(
				Preferences.getInstance().getDouble("ChassisHighP",SUBSYSTEM_STRAIGHT_HIGH_P_DEFAULT),
				Preferences.getInstance().getDouble("ChassisHighI",SUBSYSTEM_STRAIGHT_HIGH_I_DEFAULT),
				Preferences.getInstance().getDouble("ChassisHighD",SUBSYSTEM_STRAIGHT_HIGH_D_DEFAULT)
			);
		}else{
			GetInstance().getPIDController().setPID(
				Preferences.getInstance().getDouble("ChassisLowP",SUBSYSTEM_STRAIGHT_LOW_P_DEFAULT),
				Preferences.getInstance().getDouble("ChassisLowI",SUBSYSTEM_STRAIGHT_LOW_I_DEFAULT),
				Preferences.getInstance().getDouble("ChassisLowD",SUBSYSTEM_STRAIGHT_LOW_D_DEFAULT)
			);
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
		GetInstance().getPIDController().setSetpoint(GetAngle() + workingAngle);
		GetInstance().getPIDController().enable();
		
		System.out.println("Holding Angle");
	}
	
	/**
	 * 
	 * @param angle  in radians
	 */
	public static void HoldAbsAngle(double angle)
	{
		double workingAngle = (180/Math.PI)*angle;
		SetPIDValues();
		GetInstance().getPIDController().setSetpoint(workingAngle);
		GetInstance().getPIDController().enable();
	}
	
	public static void DriveStraight(double move){ 
		moveSpeed = move; 
	}
	
	public static void toMotionProfileMode(){
		
	}
	
	public void resetGyro() {
		m_navX.reset();
	}
	
	private boolean pathFinished = false;
	
	public void resetForPath() {
		pathFinished = false;
		ResetEncoders();
		resetGyro();
	}
	public boolean isPathFinished() {
		return pathFinished;
	}
	
	 public double generateHashCode(Waypoint[] path) {
	        double hash = 1.0;
	        for (int i = 0; i < path.length; i++) {
	            hash += ((path[i].x * 3) + (path[i].y * 7) + (path[i].angle * 11));
	        }
	        return (int) Math.abs(hash * 1000) * path.length;
	    }
	 
	public EncoderFollower[] pathSetup(Waypoint[] path) {
        EncoderFollower left = new EncoderFollower();
        EncoderFollower right = new EncoderFollower();
        Trajectory.Config cfg = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH,
                PathConstants.dt, PathConstants.max_velocity, PathConstants.max_acceleration, PathConstants.max_jerk);
        String pathHash = String.valueOf(generateHashCode(path));
        SmartDashboard.putString("Path Hash", pathHash);
        Trajectory toFollow;// = Pathfinder.generate(path, cfg);
        File trajectory = new File("/home/lvuser/paths/" + pathHash + ".csv");
        if (!trajectory.exists()) {
            toFollow = Pathfinder.generate(path, cfg);
            Pathfinder.writeToCSV(trajectory, toFollow);
            System.out.println(pathHash + ".csv not found, wrote to file");
        } else {
            System.out.println(pathHash + ".csv read from file");
            toFollow = Pathfinder.readFromCSV(trajectory);
        }

        TankModifier modifier = new TankModifier(toFollow).modify(PathConstants.wheel_base_width);
        PathConstants.last_gyro_error = 0.0;
        left = new EncoderFollower(modifier.getLeftTrajectory());
        right = new EncoderFollower(modifier.getRightTrajectory());
        left.configureEncoder(m_leftMotorFront.getSelectedSensorPosition(0), PathConstants.ticks_per_rev, PathConstants.wheel_diameter_L);
        right.configureEncoder(m_rightMotorFront.getSelectedSensorPosition(0), PathConstants.ticks_per_rev, PathConstants.wheel_diameter_R);
        left.configurePIDVA(PathConstants.kp, PathConstants.ki, PathConstants.kd, PathConstants.kv, PathConstants.ka);
        right.configurePIDVA(PathConstants.kp, PathConstants.ki, PathConstants.kd, PathConstants.kv, PathConstants.ka);
        return new EncoderFollower[]{
                left, // 0
                right, // 1
        };
    }
	
	public void pathFollow(EncoderFollower[] followers, boolean reverse) {
        EncoderFollower left = followers[0];
        EncoderFollower right = followers[1];
        double l;
        double r;
        double localGp = PathConstants.gp;
        if (!reverse) {
            localGp *= -1;
            l = left.calculate(-m_leftMotorFront.getSelectedSensorPosition(0));
            r = right.calculate(-m_rightMotorFront.getSelectedSensorPosition(0));
        } else {
            l = left.calculate(m_leftMotorFront.getSelectedSensorPosition(0));
            r = right.calculate(m_rightMotorFront.getSelectedSensorPosition(0));
        }

        double gyro_heading = reverse ? GetAngle() - PathConstants.path_angle_offset : -GetAngle() + PathConstants.path_angle_offset;
        double angle_setpoint = Pathfinder.r2d(left.getHeading());
        SmartDashboard.putNumber("Angle setpoint", angle_setpoint);
        double angleDifference = Pathfinder.boundHalfDegrees(angle_setpoint - gyro_heading);
        SmartDashboard.putNumber("Angle difference", angleDifference);

        double turn = localGp * angleDifference + (PathConstants.gd *
                ((angleDifference - PathConstants.last_gyro_error) / PathConstants.dt));

        PathConstants.last_gyro_error = angleDifference;

        if (left != null && !left.isFinished()) {
            //SmartDashboard.putNumber("Left diff", left.getSegment().x + this.getEncoderDistanceMetersLeft());
            SmartDashboard.putNumber("Left set vel", left.getSegment().velocity);
            SmartDashboard.putNumber("Left set pos", left.getSegment().x);
            SmartDashboard.putNumber("Left calc voltage", l);
            SmartDashboard.putNumber("Commanded seg heading", left.getHeading());
            SmartDashboard.putNumber("Left + turn", l + turn);
            SmartDashboard.putNumber("Left seg acceleration", left.getSegment().acceleration);
            SmartDashboard.putNumber("Path angle offset", PathConstants.path_angle_offset);
            SmartDashboard.putNumber("Angle offset w/ new path angle offset", angleDifference + PathConstants.path_angle_offset);
        }
        if (!reverse) {
            DriveTank(l + turn, r - turn);
        } else {
        	DriveTank(-l + turn, -r - turn);
        }

        if (left.isFinished() && right.isFinished()) {
            pathFinished = true;
            PathConstants.path_angle_offset = 0.0;
        }
    }
	
	
	public static class PathConstants {
		
		//ALL PATHFINDER CONSTANTS IN METERS
        //TODO: TUNE CONSTANTS
        public static double kp = 1.0; 
        public static double kd = 0.0;
        //TODO: TUNE THESE OR USE JACI's MATH
        public static double gp = 0.0375;
        public static double gd = 0.0;
        
        public static double ki = 0.0;

        //Gyro logging for motion profiling
        public static double last_gyro_error = 0.0;

        public static double path_angle_offset = 0.0;
        public static final double max_velocity = 4.0; //TODO: calculate
        public static final double kv = 1.0 / max_velocity; 
        public static final double max_acceleration = 3.8; 
        public static final double ka = 0.05; //0.015
        public static final double max_jerk = 10.0;
        public static final double wheel_diameter_L = Constants.kLWheelDiameter * 0.0254;
        public static final double wheel_diameter_R = Constants.kRWheelDiameter * 0.0254;

        public static final double wheel_base_width = Constants.kChassisWidth * 0.0254;
        public static final int ticks_per_rev = 4096; // CTRE Mag Encoder
        public static final double dt = 0.02; //TODO: find
	}
}
