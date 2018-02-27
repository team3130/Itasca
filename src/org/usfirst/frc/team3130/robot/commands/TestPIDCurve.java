package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Test the Chassis' PID values for driving a curve
 */
public class TestPIDCurve extends Command{
	
    public TestPIDCurve() {
        requires(Chassis.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("TESTING PID CURVE");
    	Chassis.HoldAngle(Math.PI/2.0);
    }
    
    protected void execute(){
    	
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