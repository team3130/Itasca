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
	private static boolean changeHeight = false;
    public RunElevator() {
        requires(Elevator.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (OI.gamepad.getRawAxis(RobotMap.LST_AXS_RJOYSTICKY) >= 0.04 ){
    		HoldElevator.enableHold = false;
	    	double moveSpeed = (Preferences.getInstance().getDouble("ElevatorSpeed", 0.6)- 0.4) * -OI.gamepad.getRawAxis(RobotMap.LST_AXS_RJOYSTICKY);
	    	Elevator.runElevator(moveSpeed);
	    	changeHeight = true;
    	} else if(OI.gamepad.getRawAxis(RobotMap.LST_AXS_RJOYSTICKY)<= -0.04){
    		HoldElevator.enableHold = false;
	    	double moveSpeed = (Preferences.getInstance().getDouble("ElevatorSpeed", 0.6)) * -OI.gamepad.getRawAxis(RobotMap.LST_AXS_RJOYSTICKY);
	    	Elevator.runElevator(moveSpeed);
	    	changeHeight = true;
    	} else {
    		if (changeHeight == true){
    			HoldElevator.enableHold = true;
    			HoldElevator.holdHeight = Elevator.getHeight();
    			changeHeight = false;
    		}
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Elevator.runElevator(0.0);
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
