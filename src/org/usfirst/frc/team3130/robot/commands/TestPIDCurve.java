package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.autoCommands.AutoDriveCurve;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Test the Chassis' PID values for driving a curve
 */
public class TestPIDCurve extends CommandGroup {

	private AutoDriveCurve drive;
	
    public TestPIDCurve() {
        requires(Chassis.GetInstance());
        drive = new AutoDriveCurve();
        addSequential(drive, 5);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("Testing PID Straight");
    	drive.SetParam(30, 2, (3.14159/6), false);
    }
}