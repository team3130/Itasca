package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.subsystems.Chassis;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ChangeDriveMode extends Command {

    public ChangeDriveMode() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		if (Chassis.getArcade()){
    		Chassis.setArcade(false);
    	}
    	else {
    		Chassis.setArcade(true);
    	}
    	//System.out.println("Changing drive mode...");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
