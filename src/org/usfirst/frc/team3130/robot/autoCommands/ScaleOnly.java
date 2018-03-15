package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.Robot;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.HeightSetter;
import org.usfirst.frc.team3130.robot.commands.HeightSetter.Direction;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Auton to drop cube in the scale
 */
public class ScaleOnly extends CommandGroup {
	
	private AutoDriveStraightToPoint	driveForward;
	private AutoDriveStraightToPoint	driveBehind;
	private AutoDriveStraightToPoint	driveToScale;
	private AutoDriveStraightToPoint	driveBack;
	private AutoTurn					turnToScale;
	private AutoTurn					turnBehind;
	private ElevatorToHeight			elevatorUp;
	private RunIntakeIn					intakeIn;
	private RunIntakeOut				intakeOut;
	private char						side;
	
	private boolean sameSide;

	public ScaleOnly(char side) {/*
		requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());*/
		
		this.side    = side;
		driveForward = new AutoDriveStraightToPoint();
		driveBehind = new AutoDriveStraightToPoint();
		driveToScale = new AutoDriveStraightToPoint();
		driveBack    = new AutoDriveStraightToPoint();
		turnToScale  = new AutoTurn();
		turnBehind   = new AutoTurn();
		elevatorUp   = new ElevatorToHeight(0);
		intakeIn     = new RunIntakeIn();
		intakeOut    = new RunIntakeOut();
		
		sameSide	 =Robot.startPos.getSelected().substring(0,1).equalsIgnoreCase(String.valueOf(side));
		
		addParallel(intakeIn,1);
		addSequential(driveForward,4.1);
		
		if(sameSide){
			addSequential(elevatorUp,3);
			addSequential(turnToScale, 1);
			addSequential(driveToScale,3);
			addSequential(intakeOut, 1);
		}else{
			addSequential(turnBehind, 2);
			addSequential(driveBehind,5);
			addParallel(turnToScale,2);
			addSequential(elevatorUp,3);
			addSequential(driveToScale,3);
			addSequential(intakeOut,1);
			addSequential(driveBack,2);
		}
    }
    
	@Override
    protected void initialize(){
    	System.out.println("INIT SCALE ________________");
    	//Always same
		intakeIn.SetParam(0.3);
		elevatorUp.setParam(98);
		

		
		if(sameSide){
			intakeOut.SetParam(-0.7);
			driveForward.SetParam(
					525, 
					20, 
					Preferences.getInstance().getDouble("ScaleForwardSpeed", .5), 
					false
				);
			driveToScale.SetParam(12, 10, 0.4, false);
			if(side=='L'){
				turnToScale.setParam(90, 5);
			}else{
				turnToScale.setParam(-95, 5);
			}
		}else{
			intakeOut.SetParam(-0.5);
			driveForward.SetParam(
					432-50, 
					5, 
					Preferences.getInstance().getDouble("ScaleForwardSpeed", .5), 
					false
				);
			driveBack.SetParam(
					-50, 
					5, 
					0.3, 
					false
				);
			if(side=='L'){
				turnBehind.setParam(-95, 1);
				driveBehind.SetParam(
						340, 
						5, 
						.5, 
						false
					);
				turnToScale.setParam(88, 1);
				driveToScale.SetParam(
						50, 
						5, 
						0.3, 
						false
					);
			}else{
				turnBehind.setParam(90, 1);
				driveBehind.SetParam(
						310, 
						5, 
						.5, 
						false
					);
				turnToScale.setParam(-102, 1);
				driveToScale.SetParam(
						50, 
						5, 
						0.3, 
						false
					);
			}
		}
    }
}
