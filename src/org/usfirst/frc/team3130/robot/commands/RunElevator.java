package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.OI;
import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunElevator extends Command {

    public RunElevator() {
        requires(Elevator.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	HoldElevator.enableHold = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double moveSpeed = Preferences.getInstance().getDouble("ElevatorSpeed", 0.6) * OI.gamepad.getRawAxis(RobotMap.LST_AXS_RJOYSTICKY);
    	Elevator.runElevator(moveSpeed);
    	System.out.println("Running elevator at " + moveSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Elevator.runElevator(0.0);
    	/*HoldElevator.enableHold = true;
    	HoldElevator.holdHeight = Elevator.getHeight();*/
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
