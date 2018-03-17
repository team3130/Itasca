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
		intakeIn		= new RunIntakeIn();
		intakeOut		= new RunIntakeOut();
		
		addParallel(intakeIn, 1);
		addSequential(driveForward, 3);
		addSequential(elevatorUp, 3);
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
				20, 
				Preferences.getInstance().getDouble("ScaleForwardSpeed", .5), 
				false
			);
		driveBack.SetParam(
				-24, 
				5, 
				0.5, 
				false);
		driveToSwitch.SetParam(40, 10, 0.4, false);
		if(side=='L'){
			turnToSwitch.setParam(90, 5);
		}else{
			turnToSwitch.setParam(-95, 5);
		}
	}
}
