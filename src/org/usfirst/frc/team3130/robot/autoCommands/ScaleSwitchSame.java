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
 * Auton to run scale switch when they are both on the same side
 */
public class ScaleSwitchSame extends CommandGroup {

	private	ScaleOnly					scale;
	private ElevatorToHeight			eleDown;
	private ElevatorToHeight			eleDown2;
	private ElevatorToHeight			eleUp;
	private ElevatorToHeight			eleUp2;
	private RunIntakeOut				depositCube2;
	private AutoTurn					turnToCube;
	private RunIntakeIn					intakeCube;
	private RunIntakeIn					intakeCube2;
	private AutoDriveStraightToPoint    driveToCube;
	private AutoDriveStraightToPoint	drive;
	private RunIntakeOut				depositCube;
	private AutoTurn					turnAway;
	private IntakeToggle				openIntake;
	private IntakeToggle				closeIntake;
	private AutoDelay					delay;
	private AutoDelay					delay2;
	private AutoDriveStraightToPoint	toSwitch;
	private AutoDriveStraightToPoint	toSwitch2;
	private AutoDriveStraightToPoint	back;
	private AutoDriveStraightToPoint	back2;
	private AutoDriveStraightToPoint	drive2;
	private AutoTurn					turnAway2;
	private AutoTurn					turnToSwitch;
	private char						side;

	public ScaleSwitchSame(char side) {
		requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.side	  = side;
		scale		  = new ScaleOnly(side);
		back   		  = new AutoDriveStraightToPoint();
		eleDown		  = new ElevatorToHeight(3);
		turnAway      = new AutoTurn();
		drive    	  = new AutoDriveStraightToPoint();
		turnToCube    = new AutoTurn();
		openIntake    = new IntakeToggle();
		intakeCube    = new RunIntakeIn();
		driveToCube   = new AutoDriveStraightToPoint();
		closeIntake   = new IntakeToggle();
		eleUp 		  = new ElevatorToHeight(40);
		delay         = new AutoDelay();
		toSwitch      = new AutoDriveStraightToPoint();
		depositCube   = new RunIntakeOut();
		back2    	  = new AutoDriveStraightToPoint();
		eleDown2 	  = new ElevatorToHeight(5);
		turnAway2	  = new AutoTurn();
		intakeCube2   = new RunIntakeIn();
		drive2     	  = new AutoDriveStraightToPoint();
		eleUp2 		  = new ElevatorToHeight(40);
		delay2        = new AutoDelay();
		turnToSwitch  = new AutoTurn();
		depositCube2  = new RunIntakeOut();
		
		addSequential(scale);
		addSequential(back, 2);
		addParallel(eleDown, 3);
		addSequential(turnAway, 1.5);
		addParallel(openIntake, 0.5);
		addSequential(drive, 1.5);
		addSequential(turnToCube, 1.5);
		addParallel(intakeCube, 3);
		addSequential(driveToCube, 0.75);
		addSequential(closeIntake, 0.5);
		addParallel(eleUp, 1.5);
		addSequential(delay, 0.5);
		addSequential(toSwitch, 1);
		addSequential(depositCube, 1);
		addSequential(back2, 1);
		addParallel(eleDown2, 2);
		addSequential(turnAway2, 1);
		addParallel(intakeCube2, 2.5);
		addSequential(drive2, 1.5);
		addParallel(eleUp2, 2);
		addSequential(delay2, 0.5);
		addSequential(turnToSwitch, 1);
		addSequential(depositCube2, 1);
	}
    
	@Override
    protected void initialize(){
		intakeCube.SetParam(0.8);
		intakeCube2.SetParam(0.8);
		depositCube.SetParam(-0.6);
		depositCube2.SetParam(-0.6);
		eleDown.setParam(0);
		eleDown2.setParam(0);
		eleUp.setParam(40.0);
		eleUp2.setParam(40.0);
		driveToCube.SetParam(//106
				30,  
				3, 
				.85, 
				false
		);
		drive.SetParam(
				95, 
				8, 
				.65, 
				false
		);
		toSwitch.SetParam(
				16, 
				4, 
				.7, 
				false
		);
		back.SetParam(
				-18, 
				5, 
				0.55, 
				false
		);
		back2.SetParam(
				-22, 
				8, 
				.7, 
				false
		);
		drive2.SetParam(
				46, 
				4, 
				.7, 
				false
		);
		if(side =='L'){		//Left Side Scale, Switch, and Start
			turnAway.setParam(82, 3);
			turnToCube.setParam(50, 2);
			turnAway2.setParam(-50, 3);
			turnToSwitch.setParam(30, 5);
		}else{				//Right Side Scale, Switch, and Start
			turnAway.setParam(-82, 3);
			turnToCube.setParam(-50, 2);
			turnAway2.setParam(50, 3);
			turnToSwitch.setParam(-30, 5);
		}
		
    }
}
