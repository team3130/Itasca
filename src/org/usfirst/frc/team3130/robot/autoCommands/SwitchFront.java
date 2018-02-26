package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.HeightSetter;
import org.usfirst.frc.team3130.robot.commands.HeightSetter.Direction;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.continuousDrive.ContDrive;
import org.usfirst.frc.team3130.robot.continuousDrive.ContTurnDist;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Deposit cube into switch from the front
 *  (from center starting config, directly in front of right switch)
 */
public class SwitchFront extends CommandGroup {

	private ContTurnDist      turnLeft;
	private ContDrive 	      driveForwardShort;
	private ContDrive 		  driveForwardLong;
	private ContTurnDist      turnToSwitch;
	private ContDrive         toSwitch;
	private RunIntakeIn	      intakeIn;
	private ElevatorToHeight  elevatorUp;
	private RunIntakeOut	  intakeOut;
	private char     		  side;
	
    public SwitchFront(char side) {
    	requires(Chassis.GetInstance());
    	requires(CubeIntake.GetInstance());
    	requires(Elevator.GetInstance());
    	
    	this.side      	   = side;
    	toSwitch   	       = new ContDrive();
    	driveForwardShort  = new ContDrive();
    	driveForwardLong   = new ContDrive();
    	turnLeft	   	   = new ContTurnDist();
    	turnToSwitch   	   = new ContTurnDist();
    	intakeIn  	       = new RunIntakeIn();
    	elevatorUp	       = new ElevatorToHeight(35.0);
    	intakeOut  	       = new RunIntakeOut();

    	addParallel(intakeIn);
    	
    	if(side == 'L'){
    		addSequential(driveForwardShort, 1.5);
    		addSequential(turnLeft, 1.5);
    		addSequential(driveForwardLong, 2);
        	addParallel(elevatorUp, 1.5);
    		addSequential(turnToSwitch, 1.5);
    	}
    	else{
        	addSequential(toSwitch, 3);
        	addSequential(elevatorUp, 2);
    	}
    	
    	addSequential(intakeOut, 2);
    }
    
    @Override
    protected void initialize(){
    	intakeIn.SetParam(0.3);
    	intakeOut.SetParam(-0.4);
    	driveForwardShort.SetParam(
    			Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
    			(Constants.kWallToSwitch/2.0) - (Constants.kChassisWidth/2.0)
    	);
    	turnLeft.SetParam(0.3, -Math.PI / 2.0);
    	driveForwardLong.SetParam(
    			Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
    			(Constants.kSwitchWidth) - (Constants.kSwitchSlotWidth)
    	);
    	turnToSwitch.SetParam(0.3, Math.PI / 2.0);
    	
    	if(side == 'L'){
    		toSwitch.SetParam(
    				Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
    				Constants.kWallToSwitch - ((Constants.kWallToSwitch/2.0) + (Constants.kChassisLength/2.0))
    	    );
    	}
    	else {
    		toSwitch.SetParam(
    				Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
    				Constants.kWallToSwitch - (Constants.kChassisLength / 2.0)
    	    );
    	}
    }
}
