package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.IntakeToggle;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Deposits a cube into the scale then the switch
 */
public class ScaleAndSwitch extends CommandGroup {
	private AutoDriveStraightToPoint	driveForward;
	private AutoDriveStraightToPoint	driveBack;
	private AutoTurn					turnToScale;
	private ElevatorToHeight			elevatorUp;
	private ElevatorToHeight			elevatorUpAgain;
	private ElevatorToHeight			elevatorDown;
	private RunIntakeIn					intakeIn;
	private RunIntakeOut				intakeOut;
	private AutoTurn					turnToCube;
	private RunIntakeIn					intakeCube;
	private AutoDriveStraightToPoint    driveToCube;
	private RunIntakeOut				depositCube;
	private AutoTurn					turnToSwitch;
	private IntakeToggle				openIntake;
	private IntakeToggle				closeIntake;
	private AutoDelay					delay;
	private AutoDelay					delay2;
	private AutoDriveStraightToPoint	toSwitch;
	private char						side;

	public ScaleAndSwitch(char side) {
		requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.side    = side;
		driveForward = new AutoDriveStraightToPoint();
		new AutoDriveStraightToPoint();
		driveBack    = new AutoDriveStraightToPoint();
		turnToScale  = new AutoTurn();
		elevatorUp   = new ElevatorToHeight(0);
		new ElevatorToHeight(8);
		intakeIn     = new RunIntakeIn();
		intakeOut    = new RunIntakeOut();
		elevatorDown = new ElevatorToHeight(3);
		turnToCube   = new AutoTurn();
		driveToCube  = new AutoDriveStraightToPoint();
		intakeCube   = new RunIntakeIn();
		elevatorUpAgain = new ElevatorToHeight(0);
		depositCube  = new RunIntakeOut();
		turnToSwitch = new AutoTurn();
		openIntake   = new IntakeToggle();
		closeIntake  = new IntakeToggle();
		delay        = new AutoDelay();
		delay2       = new AutoDelay();
		toSwitch     = new AutoDriveStraightToPoint();
		
		//addSequential(eleReleaseIntake, 1);
		addParallel(intakeIn,1);
		addSequential(driveForward,4.1);
		addSequential(elevatorUp,2);
		addSequential(turnToScale, 1.1);
		//addSequential(driveToScale,3);
		addSequential(intakeOut, 1);
		addSequential(driveBack,2);
		addParallel(elevatorDown, 3);
		addSequential(turnToCube, 1.5);
		addParallel(intakeCube, 5);
		addSequential(openIntake, 0.5);
		addSequential(driveToCube, 3);
		addSequential(delay, 0.5);
		addSequential(closeIntake, 0.5);
		addSequential(delay2, 0.5);
		addParallel(elevatorUpAgain, 1.5);
		addSequential(turnToSwitch, 1);
		addSequential(toSwitch, 1);
		addSequential(depositCube, 1);
    }
    
	@Override
    protected void initialize(){
    	//System.out.println("INIT SCALE AND SWITCH ________________");
    	//Always same
		intakeIn.SetParam(0.3);
		intakeCube.SetParam(0.6);
		intakeOut.SetParam(-0.9);
		depositCube.SetParam(-0.6);
		elevatorUp.setParam(98);
		elevatorDown.setParam(3.0);
		elevatorUpAgain.setParam(40.0);
		intakeOut.SetParam(-0.7);
		driveForward.SetParam(
			Preferences.getInstance().getDouble("ScaleSwitch Forward Dist", 430), 
			20, 
			0.55, 
			false
		);
		driveBack.SetParam(
				Preferences.getInstance().getDouble("ScaleSwitch Back Dist", -24), 
				5, 
				0.5, 
				false
		);
		driveToCube.SetParam(
				Preferences.getInstance().getDouble("ScaleSwitch ToCube Dist", 116),  
				5, 
				.7, 
				false
		);
		toSwitch.SetParam(
				Preferences.getInstance().getDouble("ScaleSwitch ToSwitch Dist", 20), 
				14, 
				.7, 
				false
		);
		//driveToScale.SetParam(12, 10, 0.4, false);
		if(side=='L'){		//Left Side Scale, Switch, and Start
			turnToScale.setParam(45, 5);
			turnToCube.setParam(Preferences.getInstance().getDouble("ScaleSwitch turnToCube Left Side", 85), 2);
			//turnToSwitch.setParam(20, 3);
		}else{				//Right Side Scale, Switch, and Start
			turnToScale.setParam(-55, 2);
			turnToCube.setParam(Preferences.getInstance().getDouble("ScaleSwitch turnToCube Right Side", -84), 2);
			//turnToSwitch.setParam(-20, 3);
		}
    }
}
