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
public class Scale2CubeSame extends CommandGroup {

	private ScaleOnly					scale;
	private AutoDriveStraightToPoint	back;
	private AutoDriveStraightToPoint	back2;
	private AutoDriveStraightToPoint	driveToCube;
	private AutoDriveStraightToPoint	driveToCube2;
	private AutoDriveStraightToPoint	toScale;
	private AutoDriveStraightToPoint	toScale2;
	private AutoTurn					turnToScale;
	private AutoTurn					turnToScale2;
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
	private IntakeToggle				openIntake2;
	private IntakeToggle				closeIntake;
	private IntakeToggle				closeIntake2;
	private AutoDelay					delay;
	private AutoDelay					delay2;
	private char						side;
	
    public Scale2CubeSame(char side) {
    	requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.side    = side;
		scale		 = new ScaleOnly(side);
		eleDown 	 = new ElevatorToHeight(3);
		turnToCube   = new AutoTurn();
		intakeCube   = new RunIntakeIn();
		openIntake   = new IntakeToggle();
		driveToCube  = new AutoDriveStraightToPoint();
		delay		 = new AutoDelay();
		closeIntake  = new IntakeToggle();
		back	     = new AutoDriveStraightToPoint();
		eleUp    	 = new ElevatorToHeight(0);
		back2	     = new AutoDriveStraightToPoint();
		turnToScale  = new AutoTurn();
		intakeOut    = new RunIntakeOut();
		eleDown2 	 = new ElevatorToHeight(3);
		turnToCube2  = new AutoTurn();
		intakeCube2  = new RunIntakeIn();
		openIntake2  = new IntakeToggle();
		driveToCube2 = new AutoDriveStraightToPoint();
		delay2		 = new AutoDelay();
		closeIntake2 = new IntakeToggle();
		//back3	     = new AutoDriveStraightToPoint();
		eleUp2    	 = new ElevatorToHeight(0);
		//back4	     = new AutoDriveStraightToPoint();
		turnToScale2 = new AutoTurn();
		intakeOut2   = new RunIntakeOut();
		toScale		 = new AutoDriveStraightToPoint();
		toScale2	 = new AutoDriveStraightToPoint();
		
		addSequential(scale);
		addParallel(eleDown, 3);
		addSequential(toScale, 1);
		addSequential(turnToCube, 1.5);
		addParallel(intakeCube, 6);
		addSequential(openIntake, 0.5);
		addSequential(driveToCube, 2);
		//addSequential(delay, 0.25);
		addSequential(closeIntake, 0.5);
		addParallel(eleUp, 3);
		addSequential(back, 3);
		addSequential(turnToScale, 1.5);
		addSequential(toScale2, 1);
		addSequential(intakeOut, 1);
		addParallel(eleDown2, 3);
		addSequential(turnToCube2, 2);
		addParallel(intakeCube2, 6);
		addSequential(openIntake2, 0.5);
		addSequential(driveToCube2, 2);
		addSequential(delay2, 0.25);
		addSequential(closeIntake2, 0.5);
		addParallel(eleUp2, 3);
		addSequential(back2, 3);
		addSequential(turnToScale2, 2);
		addSequential(intakeOut2, 1);
    }
    
    @Override
    protected void initialize(){
		intakeCube.SetParam(0.8);
		intakeOut.SetParam(-0.6);
		intakeCube2.SetParam(0.8);
		intakeOut2.SetParam(-0.6);
		eleDown.setParam(3.0);
		eleDown2.setParam(3.0);
		eleUp.setParam(98.0);
		eleUp2.setParam(98.0);
		driveToCube.SetParam(
				140,  
				3, 
				.4, 
				false
		);
		driveToCube2.SetParam(
				180,  
				3, 
				.7, 
				false
		);
		back.SetParam(
				-102, 
				4, 
				0.55, 
				false
		);
		back2.SetParam(
				-130, 
				4, 
				0.7, 
				false
		);
		toScale.SetParam(
				20, 
				4, 
				0.7, 
				false
		);
		toScale2.SetParam(
				20, 
				4, 
				0.7, 
				false
		);
		if(side == 'L'){ //Left Side Scale
			turnToCube.setParam(110, 2);
			turnToScale.setParam(-110, 2);
			turnToCube2.setParam(90, 2);
			turnToScale2.setParam(-90, 2);
		}else{	//Right Side Scale
			turnToCube.setParam(-110, 2);
			turnToScale.setParam(110, 2);
			turnToCube2.setParam(-90, 2);
			turnToScale2.setParam(90, 2);
		}
    }
}
