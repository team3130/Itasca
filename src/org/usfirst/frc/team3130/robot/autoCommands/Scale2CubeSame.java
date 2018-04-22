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

	private AutoDriveStraightToPoint	drive;
	private AutoDriveStraightToPoint	drive2;
	private AutoDriveStraightToPoint	back;
	private AutoDriveStraightToPoint	back2;
	private AutoDriveStraightToPoint	back3;
	private AutoDriveStraightToPoint	driveToCube;
	private AutoTurn					turnToScale;
	private AutoTurn					turnToScale2;
	private AutoTurn					turnAway;
	private AutoTurn					turnFromSwitch;
	private AutoTurn					turnToCube;
	private ElevatorToHeight			eleUp;
	private ElevatorToHeight			eleUp2;
	private ElevatorToHeight			eleDown;
	private RunIntakeIn					intakeIn;
	private RunIntakeIn					intakeCube;
	private RunIntakeOut				intakeOut;
	private RunIntakeOut				intakeOut2;
	private IntakeToggle				openIntake;
	private IntakeToggle				closeIntake;
	private char						side;
	
    public Scale2CubeSame(char side) {
    	requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.side    = side;
		eleUp   	 = new ElevatorToHeight(0);
		intakeIn     = new RunIntakeIn();
		drive 		 = new AutoDriveStraightToPoint();
		back	     = new AutoDriveStraightToPoint();
		turnToScale  = new AutoTurn();
		intakeOut    = new RunIntakeOut();
		eleDown 	 = new ElevatorToHeight(3);
		turnAway     = new AutoTurn();
		drive2 		 = new AutoDriveStraightToPoint();
		openIntake	 = new IntakeToggle();
		turnToCube   = new AutoTurn();
		intakeCube   = new RunIntakeIn();
		driveToCube  = new AutoDriveStraightToPoint();
		closeIntake	 = new IntakeToggle();
		back2	     = new AutoDriveStraightToPoint();
		turnFromSwitch= new AutoTurn();
		eleUp2   	 = new ElevatorToHeight(0);
		back3	     = new AutoDriveStraightToPoint();
		turnToScale2 = new AutoTurn();
		intakeOut2   = new RunIntakeOut();
		
    	addParallel(eleUp,3);
		addParallel(intakeIn,1);
		addSequential(drive,4.5);
		addSequential(turnToScale, 1.5);
		addSequential(intakeOut, 1);
		addSequential(back, 2);
		addParallel(eleDown, 3);
		//addSequential(turnAway, 1.5);
		//addSequential(drive2, 1.5);
		//addSequential(openIntake, 0.5);
		addSequential(turnToCube, 2);
		addParallel(intakeCube, 6);
		addSequential(driveToCube, 3);
		//addSequential(closeIntake, 1);
		addParallel(eleUp2, 3);
		addSequential(back2, 3);
		//addSequential(turnFromSwitch, 1.5);
		//addSequential(back3, 3);
		addSequential(turnToScale2, 2);
		addSequential(intakeOut2, 1);
    }
    
    @Override
    protected void initialize(){
    	intakeIn.SetParam(0.3);
    	intakeOut.SetParam(-0.6);
		intakeCube.SetParam(0.8);
		eleDown.setParam(3.0);
		eleUp.setParam(98.0);
		eleUp2.setParam(98.0);
		drive.SetParam(
				406,
				10, 
				0.95, 
				false
		);
		back.SetParam(
				-22, 
				4, 
				0.55, 
				false
		);
		drive2.SetParam(
				84, 
				14, 
				.7, 
				false
		);
		driveToCube.SetParam(
				140,  
				3, 
				.6, 
				false
		);
		back2.SetParam(
				-85, 
				4, 
				0.55, 
				false
		);
		back3.SetParam(
				-66, 
				4, 
				0.55, 
				false
		);
		if(side == 'L'){ //Left Side Scale
			turnToScale.setParam(45, 5);
			turnAway.setParam(80, 3);
			turnToCube.setParam(100, 2);
			turnFromSwitch.setParam(-10, 2);
			turnToScale2.setParam(-90, 2);
		}else{	//Right Side Scale
			turnToScale.setParam(-55, 2);
			turnAway.setParam(-80, 3);
			turnToCube.setParam(-100, 2);
			turnFromSwitch.setParam(10, 2);
			turnToScale2.setParam(90, 2);
		}
    }
}
