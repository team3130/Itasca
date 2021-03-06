package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.OI;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DefaultDrive extends Command {

    public DefaultDrive() {
    	requires(Chassis.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Chassis.ReleaseAngle();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//TODO: Do not invert inputs in commands, do it in subsystem if it's universal.
    	double moveSpeed = -OI.stickL.getY(); //joystick's y axis is inverted
    	double turnSpeed = -OI.stickR.getX(); //arcade drive has left as positive, but we want right to be positive
    	double moveL = OI.stickL.getY();
    	double moveR = OI.stickR.getY();
    	double turnThrottle = (0.5 * OI.stickR.getRawAxis(3)) - 0.5;
    	if (Chassis.getArcade()){
    		Chassis.DriveArcade(moveSpeed, turnSpeed * turnThrottle, true);
    	}
    	else {
    		Chassis.DriveTank(moveL, moveR);
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
