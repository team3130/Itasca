package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunIntakeOut extends Command {

    public RunIntakeOut() {
        requires(CubeIntake.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	CubeIntake.runIntake(Preferences.getInstance().getDouble("Cube Intake Out L", -0.5),
    						 Preferences.getInstance().getDouble("Cube Intake Out R", -0.5));
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
    	CubeIntake.runIntake(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}