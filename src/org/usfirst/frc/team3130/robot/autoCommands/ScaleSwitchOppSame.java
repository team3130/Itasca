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
 * ScaleSwitch for when the scale is opposite of start, but the switch is
 * on the same side as the scale
 */
public class ScaleSwitchOppSame extends CommandGroup {

	private ScaleOnly					scale;
	private AutoDriveStraightToPoint	back;
	private AutoDriveStraightToPoint	back2;
	private AutoDriveStraightToPoint	toCube;
	private AutoDriveStraightToPoint	toCube2;
	private AutoTurn					turnToSwitch;
	private AutoTurn					turnToSwitch2;
	private AutoTurn					turnToCube;
	private AutoTurn					turnToCube2;
	private ElevatorToHeight			eleUp;
	private ElevatorToHeight			eleUp2;
	private ElevatorToHeight			eleDown;
	private ElevatorToHeight			eleDown2;
	private RunIntakeIn					intakeCube;
	private RunIntakeIn					intakeCube2;
	private RunIntakeOut				intakeOut;
	private RunIntakeOut				intakeOut2;
	private IntakeToggle				openIntake;
	private IntakeToggle				closeIntake;
	private AutoDelay					delay;
	private AutoDelay					delay2;
	private char						side;
	
    public ScaleSwitchOppSame(char side) {
    	requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.side     = side; //side of scale
		scale		  = new ScaleOnly(side);
		eleDown 	  = new ElevatorToHeight(3);
		back	      = new AutoDriveStraightToPoint();
		intakeCube    = new RunIntakeIn();
		openIntake	  = new IntakeToggle();
		turnToCube    = new AutoTurn();
		toCube		  = new AutoDriveStraightToPoint();
		delay		  = new AutoDelay();
		closeIntake	  = new IntakeToggle();
		eleUp 		  = new ElevatorToHeight(40);
		delay         = new AutoDelay();
		turnToSwitch  = new AutoTurn();
		intakeOut     = new RunIntakeOut();
		back2    	  = new AutoDriveStraightToPoint();
		eleDown2 	  = new ElevatorToHeight(5);
		turnToCube2	  = new AutoTurn();
		intakeCube2   = new RunIntakeIn();
		toCube2       = new AutoDriveStraightToPoint();
		eleUp2 		  = new ElevatorToHeight(40);
		delay2        = new AutoDelay();
		turnToSwitch2 = new AutoTurn();
		intakeOut2    = new RunIntakeOut();
		
    	addSequential(scale);
		addParallel(eleDown, 3);
		addSequential(back, 1.5);
		addParallel(intakeCube, 6);
		addSequential(turnToCube, 1.5);
		addParallel(openIntake, 0.5);
		addSequential(toCube, 3);
		addParallel(closeIntake, 0.5);
		addSequential(delay, 0.25);
		addParallel(eleUp, 1.5);
		addSequential(turnToSwitch, 1);
		addSequential(intakeOut, 1);
		addSequential(back2, 1);
		addParallel(eleDown2, 2);
		addSequential(turnToCube2, 1);
		addParallel(intakeCube2, 2.5);
		addSequential(toCube2, 1.5);
		addParallel(eleUp2, 2);
		addSequential(delay2, 0.25);
		addSequential(turnToSwitch2, 1);
		addSequential(intakeOut2, 1);
    }
    
    @Override
    protected void initialize(){
    	eleDown.setParam(0);
    	eleDown2.setParam(0);
    	eleUp.setParam(40);
    	eleUp2.setParam(40);
		intakeCube.SetParam(0.8);
		intakeCube2.SetParam(0.8);
		intakeOut.SetParam(-0.6);
		intakeOut2.SetParam(-0.6);
		back.SetParam(
				-24, 
				4, 
				0.7, 
				false
		);
		toCube.SetParam(
				120, 
				3,
				0.9, 
				false
		);
		back2.SetParam(
				-22, 
				8, 
				.7, 
				false
		);
		toCube2.SetParam(
				50, 
				4, 
				.7, 
				false
		);
		if(side == 'L'){
			turnToCube.setParam(125, 2);
			turnToSwitch.setParam(-60, 4);
			turnToCube2.setParam(-45, 3);
			turnToSwitch.setParam(-45, 4);
		}
		else{
			turnToCube.setParam(-125, 2);
			turnToSwitch.setParam(60, 4);
			turnToCube2.setParam(45, 3);
			turnToSwitch.setParam(45, 4);
		}
    }
}
