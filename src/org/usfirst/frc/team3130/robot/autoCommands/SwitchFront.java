package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.HeightSetter;
import org.usfirst.frc.team3130.robot.commands.HeightSetter.Direction;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.continuousDrive.ContDrive;
import org.usfirst.frc.team3130.robot.continuousDrive.ContTurnDist;
import org.usfirst.frc.team3130.robot.continuousDrive.ContTurnHeading;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Deposit cube into switch from the front
 *  (from center starting config, directly in front of right switch)
 */
public class SwitchFront extends CommandGroup {

	private ContDrive			driveForward;
	private ContDrive			driveToSwitch;
	private ContTurnDist		turnToSwitch;
	private RunIntakeIn			intakeIn;
	private ElevatorToHeight	elevatorUp;
	private RunIntakeOut		intakeOut;
	private char				side;
	
	public SwitchFront(char side) {
		requires(Chassis.GetInstance());
		requires(CubeIntake.GetInstance());
		requires(Elevator.GetInstance());
		
		this.side			= side;
		driveForward		= new ContDrive();
		turnToSwitch		= new ContTurnDist(driveForward);
		driveToSwitch		= new ContDrive(turnToSwitch);
		intakeIn			= new RunIntakeIn();
		elevatorUp			= new ElevatorToHeight(35.0);
		intakeOut			= new RunIntakeOut();

		addParallel(intakeIn, 1);
		addSequential(driveForward, 2);
		addParallel(turnToSwitch, 1);
		addSequential(elevatorUp, 2);
		addSequential(driveToSwitch, 4);
		addSequential(intakeOut,1);
	}

	@Override
	protected void initialize(){
		intakeIn.SetParam(0.3);
		intakeOut.SetParam(-0.4);
		
		driveForward.SetParam(
				0.5, 
				20, 
				false
			);
		driveToSwitch.SetParam(
				0.5, 
				100, 
				false
			);
		
		if(side=='L'){
			turnToSwitch.SetParam(
					0.5, 
					30, 
					false
				);
		}else{
			turnToSwitch.SetParam(
					0.5, 
					-30, 
					false
				);
		}
	}
}
