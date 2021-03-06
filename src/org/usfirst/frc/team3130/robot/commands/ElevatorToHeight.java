package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.OI;
import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ElevatorToHeight extends Command {

	private double dist;
	
    public ElevatorToHeight(double dist) {
    	this.dist = dist;
        requires(Elevator.GetInstance());
    }

    public void setParam(double dist){
    	this.dist=dist;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	DriverStation.reportWarning("ElevatorToHeight.java command started", false);
    	Elevator.setHeight(dist-3.0); //distance to travel in inches
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Elevator.getHeight()-dist)<12 || 
        	   Math.abs(OI.gamepad.getRawAxis(RobotMap.LST_AXS_RJOYSTICKY)) > 0.1;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
