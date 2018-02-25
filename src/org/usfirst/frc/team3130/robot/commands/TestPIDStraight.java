package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.autoCommands.AutoDriveStraightToPoint;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Test the Chassis' PID values for driving straight
 */
public class TestPIDStraight extends CommandGroup {

	private AutoDriveStraightToPoint drive;
	
    public TestPIDStraight() {
        requires(Chassis.GetInstance());
        drive = new AutoDriveStraightToPoint();
        addSequential(drive, 5);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("Testing PID Straight");
    	drive.SetParam(36, 2, 0.7, false);
    }
}
