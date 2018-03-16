package org.usfirst.frc.team3130.robot.pathfinder;

import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunReversePath extends Command {

	Waypoint[] path;
	EncoderFollower[] followers;
	
    public RunReversePath(Waypoint[] path) {
    	requires(Chassis.GetInstance());
    	this.path = path;
    	setInterruptible(false);
    	followers = Chassis.GetInstance().pathSetup(path);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Chassis.GetInstance().resetForPath();
    	Chassis.GetInstance().pathFollow(followers, true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Chassis.GetInstance().pathFollow(followers, true);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Chassis.GetInstance().isPathFinished();
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
