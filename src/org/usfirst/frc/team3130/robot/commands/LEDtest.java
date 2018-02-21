package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.sensors.Rangefinder;
import org.usfirst.frc.team3130.robot.subsystems.BlinkinInterface;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class LEDtest extends Command {

    public LEDtest() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(BlinkinInterface.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Rangefinder rf = Rangefinder.GetInstance();
    	int distance = Rangefinder.getDistance();
    	if(distance>=0) {
    		BlinkinInterface.showRange(distance);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
