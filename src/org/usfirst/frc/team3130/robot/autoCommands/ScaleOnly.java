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
		turnToScale  = new AutoTurn();
		turnBehind   = new AutoTurn();
		elevatorUp   = new ElevatorToHeight(0);
		intakeIn     = new RunIntakeIn();
		intakeOut    = new RunIntakeOut();
		
		sameSide	 =Robot.startPos.getSelected().substring(0,1).equalsIgnoreCase(String.valueOf(side));
		
		addParallel(intakeIn,1);
		addSequential(driveForward);
		
		if(sameSide){
			addSequential(elevatorUp,3);
			addSequential(turnToScale, 3);
			addSequential(driveToScale,3);
		}else{
			addSequential(turnBehind, 3);
			addSequential(driveBehind,5);
			addSequential(elevatorUp);
			addSequential(turnToScale);
			addSequential(driveToScale);
		}
		
		addSequential(intakeOut, 1);
    }
    
	@Override
    protected void initialize(){
    	System.out.println("INIT SCALE ________________");
    	//Always same
		intakeIn.SetParam(0.3);
		intakeOut.SetParam(-0.7);
		elevatorUp.setParam(98);
		

		
		if(sameSide){
			driveForward.SetParam(
					525, 
					20, 
					Preferences.getInstance().getDouble("ScaleForwardSpeed", .5), 
					false
				);
			driveToScale.SetParam(12, 10, 0.4, false);
			if(side=='L'){
				turnToScale.setParam(110, 5);
			}else{
				turnToScale.setParam(-110, 5);
			}
		}else{
			driveForward.SetParam(
					432, 
					10, 
					Preferences.getInstance().getDouble("ScaleForwardSpeed", .5), 
					false
				);
			if(side=='L'){
				turnBehind.setParam(90, 1);
				driveBehind.SetParam(
						408, 
						10, 
						.5, 
						false
					);
				turnToScale.setParam(-90, 1);
				driveToScale.SetParam(
						24, 
						10, 
						0.4, 
						false
					);
			}
		}
    }
}
