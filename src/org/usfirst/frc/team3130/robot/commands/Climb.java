package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.OI;
import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.PIDCommand;

/**
 *
 */
public class Climb extends PIDCommand {
    
	private double pvbusClimb;
	
    public Climb() {
    	//TODO: tune pid
    	super(.1,0,0);
        requires(Climber.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	getPIDController().reset();
    	getPIDController().setSetpoint(Chassis.getRoll());
    	setPID();
    	getPIDController().enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	pvbusClimb=OI.gamepad.getRawAxis(RobotMap.LST_AXS_CLIMB1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Climber.climb(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

	@Override
	protected double returnPIDInput() {
		return Chassis.getRoll();
	}

	@Override
	protected void usePIDOutput(double output) {
		//Use a similar method to holdAngle in chassis to stabalize the robot
		//TODO: check output polarity
		double speedL=pvbusClimb+output;
		double speedR=pvbusClimb-output;
		Climber.climb(speedL, speedR);
	}
	
	private void setPID(){
		//TODO: tune PID
		getPIDController().setPID(.1, 0, 0);
	}
}
