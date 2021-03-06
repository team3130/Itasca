package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunIntakeOut extends Command {

	private double speed;
	
    public RunIntakeOut() {
        requires(CubeIntake.GetInstance());
        speed = Preferences.getInstance().getDouble("Cube Intake Out", -0.4);
    }
    
    public void SetParam(double speed){
    	this.speed = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	DriverStation.reportWarning("RunIntakeOut.java command started", false);
    	CubeIntake.runIntake(speed);
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
    	CubeIntake.runIntake(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
