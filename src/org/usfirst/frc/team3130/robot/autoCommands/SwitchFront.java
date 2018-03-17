package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Deposit cube into switch from the front
 *  (from center starting config, directly in front of right switch)
 */
public class SwitchFront extends CommandGroup {

	private AutoDriveStraightToPoint	driveForward;
	private AutoDriveStraightToPoint	driveToSwitch;
	private AutoTurn					turnToSwitch;
	private RunIntakeIn				intakeIn;
	private ElevatorToHeight		elevatorUp;
	private RunIntakeOut			intakeOut;
	private char					side;
	
	public SwitchFront(char side) {
		requires(Chassis.GetInstance());
		requires(CubeIntake.GetInstance());
		requires(Elevator.GetInstance());
		
		this.side			= side;
		driveForward		= new AutoDriveStraightToPoint();
		turnToSwitch		= new AutoTurn();
		driveToSwitch		= new AutoDriveStraightToPoint();
		intakeIn			= new RunIntakeIn();
		elevatorUp			= new ElevatorToHeight(35.0);
		intakeOut			= new RunIntakeOut();
		

		addParallel(intakeIn, 1);
		addSequential(driveForward, 2);
		addSequential(turnToSwitch, 1.5);
		addParallel(elevatorUp, 3);
		addSequential(driveToSwitch, 4);
		addSequential(intakeOut,1);
	}

	@Override
	protected void initialize(){
		intakeIn.SetParam(0.3);
		intakeOut.SetParam(-0.4);
		
		driveForward.SetParam(
				30, 
				5,
				0.5,
				false
			);
		
		
		if(side=='L'){
			turnToSwitch.setParam(-40, 2);
			driveToSwitch.SetParam(
					150,
					5,
					0.5, 
					false
				);
		}else{
			turnToSwitch.setParam(50,2);
			driveToSwitch.SetParam(
					174,
					5,
					0.5, 
					false
				);
		}
	}
}
