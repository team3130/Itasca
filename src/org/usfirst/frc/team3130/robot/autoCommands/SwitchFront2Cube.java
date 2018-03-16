package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Switch from the center position, 2 cubes :)
 */
public class SwitchFront2Cube extends CommandGroup {

	private AutoDriveStraightToPoint	driveForward;
	private AutoDriveStraightToPoint	driveToSwitch;
	private AutoTurn					turnToSwitch;
	private RunIntakeIn					intakeIn;
	private ElevatorToHeight			elevatorUp;
	private ElevatorToHeight			eleReleaseIntake;
	private RunIntakeOut				intakeOut;
	private AutoDriveStraightToPoint	driveBackward;
	private ElevatorToHeight			elevatorDown;
	private AutoTurn					turnToCubes;
	private AutoDriveStraightToPoint	toCubes;
	private RunIntakeIn				    intakeCube;
	private AutoDriveStraightToPoint	backFromCubes;
	private AutoTurn					turnBack;
	private ElevatorToHeight			elevatorUpAgain;
	private AutoDriveStraightToPoint	backToSwitch;
	private RunIntakeOut				intakeOutAgain;
	private char						side;
	
	public SwitchFront2Cube(char side) {
		requires(Chassis.GetInstance());
		requires(CubeIntake.GetInstance());
		requires(Elevator.GetInstance());
		
		this.side			= side;
		driveForward		= new AutoDriveStraightToPoint();
		turnToSwitch		= new AutoTurn();
		driveToSwitch		= new AutoDriveStraightToPoint();
		intakeIn			= new RunIntakeIn();
		elevatorUp			= new ElevatorToHeight(0);
		eleReleaseIntake    = new ElevatorToHeight(3);
		intakeOut			= new RunIntakeOut();
		driveBackward		= new AutoDriveStraightToPoint();
		elevatorDown		= new ElevatorToHeight(3);
		turnToCubes			= new AutoTurn();
		toCubes				= new AutoDriveStraightToPoint();
		intakeCube			= new RunIntakeIn();
		backFromCubes		= new AutoDriveStraightToPoint();
		turnBack			= new AutoTurn();
		elevatorUpAgain		= new ElevatorToHeight(0);
		backToSwitch		= new AutoDriveStraightToPoint();
		intakeOutAgain		= new RunIntakeOut();
		
		addSequential(eleReleaseIntake,1);
		addParallel(intakeIn, 1);
		addSequential(driveForward, 2);
		addSequential(turnToSwitch, 1.5);
		addParallel(elevatorUp, 3);
		addSequential(driveToSwitch, 4);
		addSequential(intakeOut,1);
		addSequential(driveBackward, 1.5);
		addParallel(elevatorDown, 2);
		addSequential(turnToCubes, 2.0);
		addParallel(intakeCube, 2);
		addSequential(toCubes, 2);
		addSequential(backFromCubes, 1.5);
		addSequential(turnBack, 1.5);
		addParallel(elevatorUpAgain, 3);
		addSequential(backToSwitch, 2);
		addSequential(intakeOutAgain, 1);
	}

	@Override
	protected void initialize(){
		intakeIn.SetParam(0.7);
		intakeCube.SetParam(0.8);
		intakeOut.SetParam(-0.7);
		intakeOutAgain.SetParam(-0.7);
		elevatorUp.setParam(40);
		elevatorUpAgain.setParam(40);
		driveForward.SetParam(
				30, 
				5,
				0.5,
				false
		);
		driveBackward.SetParam(
				-80, 
				2,
				0.5,
				false
		);
		toCubes.SetParam(
				40, 
				2,
				0.5,
				false
		);
		backFromCubes.SetParam(
				-40, 
				2,
				0.5,
				false
		);
		backToSwitch.SetParam(
				80, 
				2,
				0.5,
				false
		);
		if(side=='L'){
			turnToSwitch.setParam(-40, 2);
			driveToSwitch.SetParam(
					138,
					5,
					0.5, 
					false
				);
			turnToCubes.setParam(60, 2);
			turnBack.setParam(-60, 2);
		}else{
			turnToSwitch.setParam(50,2);
			driveToSwitch.SetParam(
					162,
					5,
					0.5, 
					false
				);
			turnToCubes.setParam(-60, 2);
			turnBack.setParam(60, 2);
		}
	}
}

