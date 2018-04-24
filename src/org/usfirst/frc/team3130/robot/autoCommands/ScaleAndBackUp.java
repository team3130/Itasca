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
public class ScaleAndBackUp extends CommandGroup {
	
	private AutoDriveStraightToPoint	driveForward;
	private AutoDriveStraightToPoint	driveBehind;
	private AutoDriveStraightToPoint	driveToScale;
	private AutoDriveStraightToPoint	driveBack;
	private AutoDriveStraightToPoint	backUp;
	private AutoTurn					turnToScale;
	private AutoTurn					turnBehind;
	private AutoTurn					turnAway;
	private ElevatorToHeight			elevatorUp;
	private ElevatorToHeight			eleUp;
	private ElevatorToHeight			eleDown;
	private RunIntakeIn					intakeIn;
	private RunIntakeOut				intakeOut;
	private char						side;

	public ScaleAndBackUp(char side) {
		requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.side    = side; //side of scale
		driveForward = new AutoDriveStraightToPoint();
		driveBehind  = new AutoDriveStraightToPoint();
		driveToScale = new AutoDriveStraightToPoint();
		driveBack    = new AutoDriveStraightToPoint();
		backUp	     = new AutoDriveStraightToPoint();
		turnToScale  = new AutoTurn();
		turnBehind   = new AutoTurn();
		turnAway     = new AutoTurn();
		elevatorUp   = new ElevatorToHeight(0);
		eleUp 		 = new ElevatorToHeight(0);
		eleDown 	 = new ElevatorToHeight(0);
		intakeIn     = new RunIntakeIn();
		intakeOut    = new RunIntakeOut();
		
		addParallel(intakeIn,3);
		addParallel(elevatorUp,3);
		addSequential(driveForward,4.5);
		addSequential(turnToScale, 1);
		addSequential(intakeOut, 1);
		addParallel(eleDown, 3);
		addSequential(turnAway, 2);
		addSequential(backUp, 2);
    }
    
	@Override
    protected void initialize(){
    	//Always same
		intakeIn.SetParam(0.5);
		elevatorUp.setParam(98);
		eleUp.setParam(6);
		eleDown.setParam(0);
		intakeOut.SetParam(-0.7);
		driveForward.SetParam(
				410, 
				10, 
				0.95, 
				false
		);
		backUp.SetParam(
				-130, 
				10, 
				0.75, 
				false
		);
		driveToScale.SetParam(12, 10, 0.4, false);
		if(side=='L'){			//Scale is on left
			turnToScale.setParam(45, 5);
			turnAway.setParam(-45, 5);
		}else{					//Scale is on right
			turnToScale.setParam(-45, 5);
			turnAway.setParam(45, 5);
		}
		
    }
}
