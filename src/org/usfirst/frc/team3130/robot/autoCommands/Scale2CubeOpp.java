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
 * 2 cube scale
 */
public class Scale2CubeOpp extends CommandGroup {

	private AutoDriveStraightToPoint	drive;
	private AutoDriveStraightToPoint	drive2;
	private AutoDriveStraightToPoint	back;
	private AutoDriveStraightToPoint	back2;
	private AutoDriveStraightToPoint	back3;
	private AutoDriveStraightToPoint	driveToCube;
	private AutoDriveStraightToPoint	driveBehind;
	private AutoDriveStraightToPoint	toScale;
	private AutoDriveStraightToPoint	toScale2;
	private AutoDriveStraightToPoint	toCube;
	private AutoDriveStraightToPoint	toCube2;
	private AutoTurn					turnToScale;
	private AutoTurn					turnToScale2;
	private AutoTurn					turnAway;
	private AutoTurn					turnToCube;
	private AutoTurn					turnToCube2;
	private AutoTurn					turnBehind;
	private ElevatorToHeight			eleUp;
	private ElevatorToHeight			eleUp2;
	private ElevatorToHeight			eleUp3;
	private ElevatorToHeight			eleDown;
	private RunIntakeIn					intakeIn;
	private RunIntakeIn					intakeCube;
	private RunIntakeIn					intakeCube2;
	private RunIntakeOut				intakeOut;
	private RunIntakeOut				intakeOut2;
	private IntakeToggle				openIntake;
	private IntakeToggle				closeIntake;
	private IntakeToggle				openIntake2;
	private IntakeToggle				closeIntake2;
	private AutoDelay					delay;
	private AutoDelay					delay2;
	private char						side;
	
    public Scale2CubeOpp(char side) {
    	requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.side    = side;
		eleUp   	 = new ElevatorToHeight(0);
		intakeIn     = new RunIntakeIn();
		drive 		 = new AutoDriveStraightToPoint();
		turnBehind   = new AutoTurn();
		driveBehind	 = new AutoDriveStraightToPoint();
		eleUp2   	 = new ElevatorToHeight(0);
		turnToScale  = new AutoTurn();
		toScale		 = new AutoDriveStraightToPoint();
		intakeOut    = new RunIntakeOut();
		eleDown 	 = new ElevatorToHeight(3);
		back	     = new AutoDriveStraightToPoint();
		intakeCube   = new RunIntakeIn();
		openIntake	 = new IntakeToggle();
		turnToCube   = new AutoTurn();
		toCube		 = new AutoDriveStraightToPoint();
		delay		 = new AutoDelay();
		closeIntake	 = new IntakeToggle();
		back2	     = new AutoDriveStraightToPoint();
		turnToScale2 = new AutoTurn();
		eleUp3       = new ElevatorToHeight(0);
		toScale2     = new AutoDriveStraightToPoint();
		intakeOut2   = new RunIntakeOut();
		back3		 = new AutoDriveStraightToPoint();
		intakeCube2  = new RunIntakeIn();
		openIntake2	 = new IntakeToggle();
		turnToCube2  = new AutoTurn();
		toCube2		 = new AutoDriveStraightToPoint();
		delay2		 = new AutoDelay();
		closeIntake2	 = new IntakeToggle();
		
    	addParallel(eleUp,3);
		addParallel(intakeIn,1);
		addSequential(drive,2.0);
		addSequential(turnBehind, 1.5);
		addSequential(driveBehind,5);
		addParallel(eleUp2,3);
		addSequential(turnToScale,1);
		addSequential(toScale,1.5);
		addSequential(intakeOut,1);
		addParallel(eleDown, 3);
		addSequential(back,2);
		addParallel(intakeCube, 4);
		addParallel(openIntake, 0.5);
		addSequential(turnToCube, 2);
		addSequential(toCube, 2.0);
		addSequential(delay, 0.5);
		addSequential(closeIntake, 0.5);
		addSequential(back2, 1.5);
		addSequential(turnToScale2, 1);
		addParallel(eleUp3, 3);
		addSequential(toScale2, 1.5);
		addSequential(intakeOut2, 1);
		addSequential(back3,2);
		addParallel(intakeCube2, 4);
		addParallel(openIntake2, 0.5);
		addSequential(turnToCube2, 2);
		addSequential(toCube2, 2.0);
		addSequential(delay2, 0.5);
		addSequential(closeIntake2, 0.5);
    }
    
    @Override
    protected void initialize(){
    	intakeIn.SetParam(0.3);
    	intakeOut.SetParam(-0.6);
    	intakeOut2.SetParam(-0.6);
		intakeCube.SetParam(0.8);
		intakeCube2.SetParam(0.8);
		eleDown.setParam(0.0);
		eleUp.setParam(40.0);
		eleUp2.setParam(98.0);
		drive.SetParam(
				348,
				10, 
				1.00, 
				false
		);
		driveBehind.SetParam(
				330, 
				10, 
				.7, 
				false
		);
		back.SetParam(
				-22, 
				4, 
				0.55, 
				false
		);
		drive2.SetParam(
				76, 
				14, 
				.7, 
				false
		);
		back2.SetParam(
				22, 
				4, 
				0.55, 
				false
		);
		toScale.SetParam(
				36, 
				10, 
				0.6, 
				false
		);
		toCube.SetParam(
				95, 
				3,
				0.7, 
				false
		);
		toScale2.SetParam(
				80, 
				10, 
				0.6, 
				false
		);
		back3.SetParam(
				-22, 
				4, 
				0.55, 
				false
		);
		toCube2.SetParam(
				95, 
				3,
				0.7, 
				false
		);
		if(side == 'L'){ //Left Side Scale
			turnBehind.setParam(-90, 1);
			turnToScale.setParam(110, 2);
			turnToCube.setParam(140, 2);
			turnToCube2.setParam(140, 2);
			turnToScale2.setParam(-140, 2);
		}else{	//Right Side Scale
			turnBehind.setParam(90, 1);
			turnToScale.setParam(-110, 2);
			turnToCube.setParam(-140, 2);
			turnToCube2.setParam(-140, 2);
			turnToScale2.setParam(140, 2);
		}
    }
}
