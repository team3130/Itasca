package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Deposit cube into switch from the side
 */
public class SwitchSide extends CommandGroup {
	
	private AutoDriveStraightToPoint	driveForward;
	private AutoDriveStraightToPoint	driveToSwitch;
	private AutoDriveStraightToPoint	driveBack;
	private AutoTurn					turnToSwitch;
	private ElevatorToHeight			elevatorUp;
	private ElevatorToHeight			eleReleaseIntake;
	private RunIntakeIn					intakeIn;
	private RunIntakeOut				intakeOut;
	private char						side;
	
	public SwitchSide(char side) {
		requires(Chassis.GetInstance());
		requires(CubeIntake.GetInstance());
		requires(Elevator.GetInstance());
		
		this.side		= side;
		driveForward	= new AutoDriveStraightToPoint();
		driveToSwitch	= new AutoDriveStraightToPoint();
		driveBack		= new AutoDriveStraightToPoint();
		turnToSwitch	= new AutoTurn();
		elevatorUp		= new ElevatorToHeight(0);
		eleReleaseIntake = new ElevatorToHeight(3);
		intakeIn		= new RunIntakeIn();
		intakeOut		= new RunIntakeOut();
		
		addParallel(elevatorUp, 3);
		addParallel(intakeIn, 1);
		addSequential(driveForward, 3);
		addSequential(turnToSwitch, 2);
		addSequential(driveToSwitch, 2);
		addSequential(intakeOut, 1);
		addSequential(driveBack, 2);
	}
	
	@Override
	protected void initialize(){
		intakeIn.SetParam(0.3);
		elevatorUp.setParam(32);
		intakeOut.SetParam(-0.7);
		driveForward.SetParam(
				224, 
				5, 
				.85, 
				false
		);
		driveBack.SetParam(
				-40, 
				2, 
				0.6, 
				false
		);
		driveToSwitch.SetParam(
				40, 
				2, 
				0.6, 
				false
		);
		if(side=='L'){			//Switch and Start on left
			turnToSwitch.setParam(90, 1);
		}else{					//Switch and start on right
			turnToSwitch.setParam(-90, 1);
		}
	}
}
