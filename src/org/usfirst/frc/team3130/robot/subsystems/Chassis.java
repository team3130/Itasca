package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.commands.DefaultDrive;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

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
}
