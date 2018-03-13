package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.subsystems.Chassis;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoTurn extends Command {

	private double angle;
	private double thresh;
	
    public AutoTurn() {
    	requires(Chassis.GetInstance());
    }

    public void setParam(double angle, double threshold){
    	this.angle=angle;
    	thresh=threshold;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	Chassis.HoldAngle(angle);
    	Chassis.GetInstance().setAbsoluteTolerance(thresh);
    	Chassis.DriveStraight(0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Chassis.GetInstance().onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Chassis.ReleaseAngle();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
