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
    	c.stop();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double winch1Speed = OI.gamepad.getRawAxis(RobotMap.CAN_WINCH1);
    	double winch2Speed = OI.gamepad.getRawAxis(RobotMap.CAN_WINCH2);
    	Climber.climb(winch1Speed, winch2Speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Climber.climb(0,0);
    	c.start();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
