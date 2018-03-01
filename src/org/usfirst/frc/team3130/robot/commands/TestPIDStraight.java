package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.autoCommands.ContDrive;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Test the Chassis' PID values for driving straight
 */
public class TestPIDStraight extends Command {

	private ContDrive drive;
	
	public TestPIDStraight() {
        requires(Chassis.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("TESTING PID STRAIGHT");
    	Chassis.HoldAngle(Math.PI/2);
    }
    
    protected void execute(){
    	Chassis.DriveStraight(0);
    }
    
    protected boolean isFinished() {
    	return false;
    }
    
    protected void end(){
    	Chassis.ReleaseAngle();
    	Chassis.DriveTank(0, 0);
    }
    
    protected void interrupted(){
    	end();
    }

}
