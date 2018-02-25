package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.autoCommands.AutoDriveCurve;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.Chassis.TurnDirection;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Test the Chassis' PID values for driving a curve
 */
public class TestPIDCurve extends Command{

	private AutoDriveCurve drive;
	
    public TestPIDCurve() {
        requires(Chassis.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("TESTING PID CURVE");
    	Chassis.setTurnDir(TurnDirection.kStraight);
    	Chassis.SetPIDValues();
    	Chassis.HoldAngle(30.0);
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