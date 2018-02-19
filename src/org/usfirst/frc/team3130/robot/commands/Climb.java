package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.OI;
import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Climb extends Command {

    Compressor c = new Compressor();
    
    public Climb() {
        requires(Climber.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double winchLSpeed = OI.gamepad.getRawAxis(RobotMap.CAN_CLIMBWINCHL1);
    	double winchRSpeed = OI.gamepad.getRawAxis(RobotMap.CAN_CLIMBWINCHR1);
    	if(winchLSpeed > 0 || winchRSpeed > 0){
    		//c.stop();
    	}
    	else{
    		//c.start();
    	}
    	Climber.climb(winchLSpeed, winchRSpeed);
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
}
