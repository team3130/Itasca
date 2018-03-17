package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Robot;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Deposits a cube into the scale then the switch
 */
public class ScaleAndSwitch extends CommandGroup {
	private AutoDriveStraightToPoint	driveForward;
	private AutoDriveStraightToPoint	driveToScale;
	private AutoDriveStraightToPoint	driveBack;
	private AutoTurn					turnToScale;
	private ElevatorToHeight			elevatorUp;
	private ElevatorToHeight			elevatorUpAgain;
	private ElevatorToHeight			elevatorDown;
	private ElevatorToHeight			eleReleaseIntake;
	private RunIntakeIn					intakeIn;
	private RunIntakeOut				intakeOut;
	private AutoTurn					turnToCube;
	private RunIntakeIn					intakeCube;
	private AutoDriveStraightToPoint    driveToCube;
	private RunIntakeOut				depositCube;
	private char						side;

	public ScaleAndSwitch(char side) {/*
		requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());*/
		
		this.side    = side;
		driveForward = new AutoDriveStraightToPoint();
		driveToScale = new AutoDriveStraightToPoint();
		driveBack    = new AutoDriveStraightToPoint();
		turnToScale  = new AutoTurn();
		elevatorUp   = new ElevatorToHeight(0);
		eleReleaseIntake = new ElevatorToHeight(3);
		intakeIn     = new RunIntakeIn();
		intakeOut    = new RunIntakeOut();
		elevatorDown = new ElevatorToHeight(3);
		turnToCube   = new AutoTurn();
		driveToCube  = new AutoDriveStraightToPoint();
		intakeCube   = new RunIntakeIn();
		elevatorUpAgain = new ElevatorToHeight(0);
		depositCube  = new RunIntakeOut();
		
		addSequential(eleReleaseIntake, 1);
		addParallel(intakeIn,1);
		addSequential(driveForward,4.1);
		addSequential(elevatorUp,3);
		addSequential(turnToScale, 1);
		addSequential(driveToScale,3);
		addSequential(intakeOut, 1);
		addSequential(driveBack,2);
		addParallel(elevatorDown, 3);
		addSequential(turnToCube, 1.5);
		addParallel(intakeCube, 4);
		addSequential(driveToCube, 3);
		addSequential(elevatorUpAgain, 1.5);
		addSequential(depositCube, 1);
    }
    
	@Override
    protected void initialize(){
    	System.out.println("INIT SCALE AND SWITCH ________________");
    	//Always same
		intakeIn.SetParam(0.3);
		intakeCube.SetParam(0.6);
		intakeOut.SetParam(-0.7);
		depositCube.SetParam(-0.7);
		elevatorUp.setParam(98);
		elevatorDown.setParam(3.0);
		elevatorUpAgain.setParam(40.0);
		intakeOut.SetParam(-0.7);
		driveForward.SetParam(
			525, 
			20, 
			Preferences.getInstance().getDouble("ScaleForwardSpeed", .5), 
			false
		);
		driveBack.SetParam(
				-50, 
				5, 
				Preferences.getInstance().getDouble("ScaleForwardSpeed", .5), 
				false
		);
		driveToCube.SetParam(
				100, 
				5, 
				0., 
				false
		);
		driveToScale.SetParam(12, 10, 0.4, false);
		if(side=='L'){
			turnToScale.setParam(90, 5);
			turnToCube.setParam(40, 2);
		}else{
			turnToScale.setParam(-95, 5);
			turnToCube.setParam(-40, 2);
		}
    }
}
