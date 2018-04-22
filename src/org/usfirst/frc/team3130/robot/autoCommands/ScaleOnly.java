package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Robot;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
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
	private ElevatorToHeight			eleUp;
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
		eleUp = new ElevatorToHeight(3);
		intakeIn     = new RunIntakeIn();
		intakeOut    = new RunIntakeOut();
		
		sameSide	 =Robot.startPos.getSelected().substring(0,1).equalsIgnoreCase(String.valueOf(side));
		
		addParallel(intakeIn,1);
		
		if(sameSide){
			addParallel(elevatorUp,3);
			addSequential(driveForward,4);
			addSequential(turnToScale, 1);
			addSequential(driveToScale,3);
			addSequential(intakeOut, 1);
		}else{
			addSequential(driveForward,4);
			addSequential(turnBehind, 2);
			addSequential(driveBehind,5);
			addSequential(turnToScale,1);
			addSequential(elevatorUp,3);
			addSequential(driveToScale,1.5);
			addSequential(intakeOut,1);
			addSequential(driveBack,2);
		}
    }
    
	@Override
    protected void initialize(){
    	//System.out.println("INIT SCALE ________________");
    	//Always same
		intakeIn.SetParam(0.3);
		intakeOut.SetParam(-0.5);
		elevatorUp.setParam(98);
		
		if(sameSide){			//Scale is same side as start
			intakeOut.SetParam(-0.7);
			driveForward.SetParam(
					388, 
					10, 
					Preferences.getInstance().getDouble("ScaleForwardSpeed", .85), 
					false
				);
			driveToScale.SetParam(12, 10, 0.4, false);
			if(side=='L'){			//Scale is on left
				turnToScale.setParam(45, 5);
			}else{					//Scale is on right
				turnToScale.setParam(-55, 5);
			}
		}else{					//Scale is on opposite side of start
			intakeOut.SetParam(-0.5);
			driveForward.SetParam(
					388, 
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
			if(side=='L'){			//Scale is on left
				turnBehind.setParam(-90, 1);
				driveBehind.SetParam(
						340, 
						5, 
						.5, 
						false
					);
				turnToScale.setParam(82, 1);
				driveToScale.SetParam(
						50, 
						5, 
						0.3, 
						false
					);
			}else{					//Scale is on right
				turnBehind.setParam(90, 1);
				driveBehind.SetParam(
						340, 
						5, 
						.5, 
						false
					);
				turnToScale.setParam(-87, 1);
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
