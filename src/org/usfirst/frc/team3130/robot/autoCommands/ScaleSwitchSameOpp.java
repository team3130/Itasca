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
 * Scale is on the same as start, but switch is opposite from scale
 */
public class ScaleSwitchSameOpp extends CommandGroup {

	private ScaleOnly 					scale;
	private AutoDriveStraightToPoint	back;
	private AutoDriveStraightToPoint	driveBehind;
	private AutoDriveStraightToPoint	toCube;
	private AutoDriveStraightToPoint	toSwitch;
	private AutoTurn					turnAway;
	private AutoTurn					turnToCube;
	private AutoTurn					turnDown;
	private AutoTurn					turnToSwitch;
	private ElevatorToHeight			eleDown;
	private ElevatorToHeight			eleUp;
	private RunIntakeIn					intake;
	private RunIntakeOut				intakeOut;
	private IntakeToggle				openIntake;
	private IntakeToggle				closeIntake;
	private char						swSide; //side of switch
	private char						scSide; //side of scale
	
    public ScaleSwitchSameOpp(char swSide, char scSide) {
    	requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.swSide 		= swSide;
		this.scSide			= scSide;
		scale 				= new ScaleOnly(scSide);
		eleDown				= new ElevatorToHeight(0);
		back				= new AutoDriveStraightToPoint();
		turnAway			= new AutoTurn();
		turnDown            = new AutoTurn();
		driveBehind			= new AutoDriveStraightToPoint();
		turnToCube			= new AutoTurn();
		intake				= new RunIntakeIn();
		openIntake			= new IntakeToggle();
		toCube				= new AutoDriveStraightToPoint();
		closeIntake			= new IntakeToggle();
		eleUp				= new ElevatorToHeight(0);
		turnToSwitch		= new AutoTurn();
		toSwitch			= new AutoDriveStraightToPoint();
		intakeOut			= new RunIntakeOut();
		
		addSequential(scale);
		addParallel(eleDown, 3);
		addSequential(turnAway, 1.5);
		addSequential(back, 2);
		addSequential(turnDown, 2);
		addSequential(driveBehind, 3);
		addParallel(openIntake, 0.5);
		addParallel(intake, 3);
		addSequential(turnToCube, 1.5);
		addSequential(toCube, 2);
		addSequential(closeIntake, 0.5);
		addParallel(eleUp, 2);
		addSequential(turnToSwitch, 1.5);
		addSequential(toSwitch, 1);
		addSequential(intakeOut, 1);
    }
    
    @Override
    protected void initialize(){
    	eleDown.setParam(0);
    	eleUp.setParam(40);
    	intake.SetParam(0.6);
    	intakeOut.SetParam(-0.5);
    	back.SetParam(
				-56, 
				4, 
				.6, 
				false
		);
    	driveBehind.SetParam(
				244, 
				5, 
				.7, 
				false
		);
    	toSwitch.SetParam(
				18, 
				2, 
				.5, 
				false
		);
    	toCube.SetParam(
				18, 
				2, 
				.5, 
				false
		);
    	if(swSide == 'R'){
    		turnAway.setParam(-45, 2);
    		turnDown.setParam(86, 2);
    		turnToCube.setParam(90, 2);
    		turnToSwitch.setParam(-15, 3);
    	}
    	else{
    		turnAway.setParam(-45, 2);
    		turnDown.setParam(-86, 2);
    		turnToCube.setParam(-90, 2);
    		turnToSwitch.setParam(15, 3);
    	}
    }
}
