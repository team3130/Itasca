package org.usfirst.frc.team3130.robot.commands;

import java.util.prefs.PreferenceChangeEvent;

import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ContinuousIntake extends Command {

    public ContinuousIntake() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(CubeIntake.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	CubeIntake.runIntake(Preferences.getInstance().getDouble("Idle Intake", 0.2));
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
