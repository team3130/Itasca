package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.OI;
import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.subsystems.HookDeploy;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunHookWinch extends Command {

    public RunHookWinch() {
        requires(HookDeploy.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	HookDeploy.runWinch(OI.gamepad.getRawAxis(RobotMap.LST_AXS_LJOYSTICKY));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	HookDeploy.runWinch(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
