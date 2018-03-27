package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
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
	private ElevatorToHeight          elevatorUp;

    public PassBaseline() {
       requires(Chassis.GetInstance());
       requires(CubeIntake.GetInstance());
       requires(Elevator.GetInstance());
       
       driveForward = new AutoDriveStraightToPoint();
       elevatorUp   = new ElevatorToHeight(4.0);
       
       addSequential(elevatorUp, 0.5);
       addSequential(driveForward, 7);
    }
    
    @Override
	protected void initialize() {
    	
        driveForward.SetParam(
				Preferences.getInstance().getDouble("BaseDistance", 200.0),
				20, 
				Preferences.getInstance().getDouble("BaseForwardSpeed", .5),
				false);
        elevatorUp.setParam(12);
    }
	
}
