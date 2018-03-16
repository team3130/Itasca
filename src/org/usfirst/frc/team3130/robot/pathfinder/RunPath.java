package org.usfirst.frc.team3130.robot.pathfinder;

import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunPath extends Command {

	Waypoint[] path;
	EncoderFollower[] followers;
	
    public RunPath(Waypoint[] path) {
    	requires(Chassis.GetInstance());
    	this.path = path;
    	setInterruptible(false);
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Chassis.DriveTank(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
