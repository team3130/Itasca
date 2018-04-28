package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Robot;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
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
	private ElevatorToHeight			eleUp;
	private RunIntakeIn					intakeIn;
	private RunIntakeOut				intakeOut;
	private char						side;
	
	private boolean sameSide;

	public ScaleOnly(char side) {
		requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.side    = side; //side of scale
		driveForward = new AutoDriveStraightToPoint();
		driveBehind  = new AutoDriveStraightToPoint();
		driveToScale = new AutoDriveStraightToPoint();
		driveBack    = new AutoDriveStraightToPoint();
		turnToScale  = new AutoTurn();
		turnBehind   = new AutoTurn();
		elevatorUp   = new ElevatorToHeight(0);
		eleUp 		 = new ElevatorToHeight(0);
		intakeIn     = new RunIntakeIn();
		intakeOut    = new RunIntakeOut();
		
		sameSide	 = Robot.startPos.getSelected().substring(0,1).equalsIgnoreCase(String.valueOf(side));
		
		addParallel(intakeIn,3);
		
		if(sameSide){
			addParallel(elevatorUp,3);
			addSequential(driveForward,4.5);
			addSequential(turnToScale, 1);
			addSequential(intakeOut, 1);
		}else{
			addParallel(eleUp, 3);
			addSequential(driveForward, 3);
			addSequential(turnBehind, 1.5);
			addSequential(driveBehind, 5);
			addParallel(elevatorUp,3);
			addSequential(turnToScale, 1);
			addSequential(driveToScale,1.5);
			addSequential(intakeOut,0.5);
		}
    }
    
	@Override
    protected void initialize(){
    	//Always same
		intakeIn.SetParam(0.5);
		intakeOut.SetParam(-0.4);
		elevatorUp.setParam(98);
		eleUp.setParam(6);
		
		if(sameSide){			//Scale is same side as start
			intakeOut.SetParam(-0.55);
			driveForward.SetParam(
					410, 
					10, 
					0.95, 
					false
				);
			driveToScale.SetParam(12, 10, 0.4, false);
			if(side=='L'){			//Scale is on left
				turnToScale.setParam(45, 2);
			}else{					//Scale is on right
				turnToScale.setParam(-45, 2);
			}
		}else{					//Scale is on opposite side of start
			intakeOut.SetParam(-0.5);
			driveForward.SetParam(
					344, 
					8, 
					0.8, 
					false
			);
			driveBehind.SetParam(
					408, 
					6, 
					.7, 
					false
			);
			driveToScale.SetParam(
					56, 
					6, 
					0.7, 
					false
			);
			if(side=='L'){			//Scale is on left
				turnBehind.setParam(-90, 0.5);
				turnToScale.setParam(126, 0.5);
			}else{					//Scale is on right
				turnBehind.setParam(90, 1);
				turnToScale.setParam(-126, 1);
			}
		}
    }
}
