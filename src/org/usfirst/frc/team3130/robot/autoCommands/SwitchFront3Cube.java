package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.IntakeToggle;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Switch from the center position, 3 cubes :)
 */
public class SwitchFront3Cube extends CommandGroup {

	private AutoDriveStraightToPoint	driveForward;
	private AutoDriveStraightToPoint	driveToSwitch;
	private AutoTurn					turnToSwitch;
	private RunIntakeIn					intakeIn;
	private ElevatorToHeight			elevatorUp;
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
	private IntakeToggle				intakeOpen;
	private IntakeToggle				intakeClose;
	private AutoDriveStraightToPoint	back;
	private ElevatorToHeight			eleDown;
	private AutoTurn					turnToCubes2;
	private IntakeToggle				intakeOpen2;
	private RunIntakeIn					intakeCube2;
	private AutoDriveStraightToPoint	toCubes2;
	private IntakeToggle 				intakeClose2;
	private AutoDriveStraightToPoint 	backFromCubes2;
	private ElevatorToHeight 			eleUp;
	private AutoTurn 					turnBack2;
	private AutoDriveStraightToPoint 	backToSwitch2;
	private RunIntakeOut 				intakeOut2;
	private char						side;
	
	public SwitchFront3Cube(char side) {
		requires(Chassis.GetInstance());
		requires(CubeIntake.GetInstance());
		requires(Elevator.GetInstance());
		
		this.side			= side;
		intakeIn			= new RunIntakeIn();
		driveForward		= new AutoDriveStraightToPoint();
		turnToSwitch		= new AutoTurn();
		driveToSwitch		= new AutoDriveStraightToPoint();
		elevatorUp			= new ElevatorToHeight(0);
		intakeOut			= new RunIntakeOut();
		driveBackward		= new AutoDriveStraightToPoint();
		elevatorDown		= new ElevatorToHeight(3);
		turnToCubes			= new AutoTurn();
		intakeOpen			= new IntakeToggle();
		intakeCube			= new RunIntakeIn();
		toCubes				= new AutoDriveStraightToPoint();
		intakeClose			= new IntakeToggle();
		backFromCubes		= new AutoDriveStraightToPoint();
		elevatorUpAgain		= new ElevatorToHeight(0);
		turnBack			= new AutoTurn();
		backToSwitch		= new AutoDriveStraightToPoint();
		intakeOutAgain		= new RunIntakeOut();
		back				= new AutoDriveStraightToPoint();
		eleDown				= new ElevatorToHeight(3);
		turnToCubes2		= new AutoTurn();
		intakeOpen2			= new IntakeToggle();
		intakeCube2			= new RunIntakeIn();
		toCubes2			= new AutoDriveStraightToPoint();
		intakeClose2		= new IntakeToggle();
		backFromCubes2		= new AutoDriveStraightToPoint();
		eleUp				= new ElevatorToHeight(0);
		turnBack2			= new AutoTurn();
		backToSwitch2		= new AutoDriveStraightToPoint();
		intakeOut2			= new RunIntakeOut();
		
		addParallel(intakeIn, 1);
		addSequential(driveForward, 2);
		addSequential(turnToSwitch, 1.5);
		addParallel(elevatorUp, 3);
		addSequential(driveToSwitch, 4);
		addSequential(intakeOut,0.5);
		addSequential(driveBackward, 1.5);
		addParallel(elevatorDown, 2);
		addSequential(turnToCubes, 2.0);
		addParallel(intakeOpen, 0.5);
		addParallel(intakeCube, 2);
		addSequential(toCubes, 2);
		addParallel(intakeClose, 0.5);
		addSequential(backFromCubes, 1.5);
		addParallel(elevatorUpAgain, 3);
		addSequential(turnBack, 1.5);
		addSequential(backToSwitch, 2.5);
		addParallel(intakeOutAgain, 0.5);
		addSequential(back, 1.5);
		addParallel(eleDown, 2);
		addSequential(turnToCubes2, 2.0);
		addParallel(intakeOpen2, 0.5);
		addParallel(intakeCube2, 2);
		addSequential(toCubes2, 2);
		addParallel(intakeClose2, 0.5);
		addSequential(backFromCubes2, 1.5);
		addParallel(eleUp, 3);
		addSequential(turnBack2, 1.5);
		addSequential(backToSwitch2, 2.5);
		addParallel(intakeOut2, 0.5);
	}

	@Override
	protected void initialize(){

		intakeIn.SetParam(0.7);
		intakeCube.SetParam(0.8);
		intakeCube2.SetParam(0.8);
		intakeOut.SetParam(-0.4);
		intakeOutAgain.SetParam(-0.5);
		intakeOut2.SetParam(-0.5);
		elevatorUp.setParam(40);
		elevatorUpAgain.setParam(40);
		eleUp.setParam(40);
		eleDown.setParam(0);
		elevatorDown.setParam(0);
		driveForward.SetParam(
				30, 
				5,
				0.85,
				false
		);
		driveBackward.SetParam(
				-80, 
				6,
				0.7,
				false
		);
		back.SetParam(
				-80, 
				6,
				0.7,
				false
		);
		backFromCubes.SetParam(
				-40, 
				6,
				0.8,
				false
		);
		backFromCubes2.SetParam(
				-40, 
				6,
				0.8,
				false
		);
		backToSwitch.SetParam(
				115, 
				6,
				0.85,
				false
		);
		backToSwitch2.SetParam(
				115, 
				6,
				0.85,
				false
		);
		if(side=='L'){			//Switch on left
			driveToSwitch.SetParam(
					150,
					6,
					0.8, 
					false
			);
			toCubes.SetParam(
					66, 
					4,
					0.7,
					false
			);
			toCubes2.SetParam(
					70, 
					4,
					0.7,
					false
			);
			turnToSwitch.setParam(-33, 2);
			turnToCubes.setParam(60, 2);
			turnToCubes2.setParam(40, 2);
			turnBack.setParam(-60, 2);
			turnBack.setParam(-50, 2);
		}else{					//Switch on right
			driveToSwitch.SetParam(
					156,
					6,
					0.85, 
					false
				);
			toCubes.SetParam(
					60, 
					4,
					0.7,
					false
			);
			toCubes2.SetParam(
					60, 
					4,
					0.7,
					false
			);
			turnToSwitch.setParam(36,2);
			turnToCubes.setParam(-45, 2);
			turnToCubes2.setParam(-35, 2);
			turnBack.setParam(50, 2);
			turnBack.setParam(40, 2);
		}
	}
}

