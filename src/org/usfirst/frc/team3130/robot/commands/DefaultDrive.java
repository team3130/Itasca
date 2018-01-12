package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.OI;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DefaultDrive extends Command {

    public DefaultDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Chassis.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//TODO: Do not invert inputs in commands, do it in subsystem if it's universal.
    	double moveSpeed = OI.stickL.getY();
    	double turnSpeed = OI.stickR.getX();
    	double turnThrottle = (0.5 * OI.stickR.getRawAxis(3)) - 0.5;
    	Chassis.DriveArcade(moveSpeed, turnSpeed * turnThrottle, true);
    	System.out.println("Drive");
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
