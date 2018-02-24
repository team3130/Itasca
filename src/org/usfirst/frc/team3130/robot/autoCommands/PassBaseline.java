package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.commands.HeightSetter;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.HeightSetter.Direction;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Colin Drive system to pass the baseline
 */
public class PassBaseline extends CommandGroup {
	
	private AutoDriveStraightToPoint  driveForward;
	private RunIntakeIn               runIntake;
	private HeightSetter              elevatorUp;

    public PassBaseline() {
       requires(Chassis.GetInstance());
       requires(CubeIntake.GetInstance());
       requires(Elevator.GetInstance());
       
       driveForward = new AutoDriveStraightToPoint();
       runIntake    = new RunIntakeIn();
       elevatorUp   = new HeightSetter(Direction.kUp);
       
       addParallel(runIntake);
       addSequential(driveForward, 3);
       addSequential(elevatorUp, 2);
       System.out.println("Running PB");
    }
    
    @Override
	protected void initialize() {
    	driveForward.SetParam(
    		Constants.kWallToSwitch, 
    		3, 
    		Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
    		false);
    		runIntake.SetParam(Preferences.getInstance().getDouble("AUTON Intake Speed", 0.3)
    	);
    }
	
}
