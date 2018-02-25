package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.commands.HeightSetter;
import org.usfirst.frc.team3130.robot.commands.HeightSetter.Direction;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Deposit cube into switch from the front
 *  (from center starting config, directly in front of right switch)
 */
public class SwitchFront extends CommandGroup {

	private AutoDriveCurve           turnLeft;
	private AutoDriveStraightToPoint driveForwardShort;
	private AutoDriveStraightToPoint driveForwardLong;
	private AutoDriveCurve			 turnToSwitch;
	private AutoDriveStraightToPoint toSwitch;
	private RunIntakeIn	             intakeIn;
	private HeightSetter			 elevatorUp;
	private RunIntakeOut			 intakeOut;
	private char					 side;
	
    public SwitchFront(char side) {
    	requires(Chassis.GetInstance());
    	requires(CubeIntake.GetInstance());
    	requires(Elevator.GetInstance());
    	
    	this.side      	   = side;
    	toSwitch   	       = new AutoDriveStraightToPoint();
    	driveForwardShort  = new AutoDriveStraightToPoint();
    	driveForwardLong   = new AutoDriveStraightToPoint();
    	turnLeft	   	   = new AutoDriveCurve();
    	turnToSwitch   	   = new AutoDriveCurve();
    	intakeIn  	       = new RunIntakeIn();
    	elevatorUp	       = new HeightSetter(Direction.kUp);
    	intakeOut  	       = new RunIntakeOut();
    	
    	if(side == 'L'){
    		addParallel(intakeIn);
    		addSequential(driveForwardShort, 2);
    		addSequential(turnLeft, 1.5);
    		addSequential(driveForwardLong, 2);
        	addParallel(elevatorUp, 1.5);
    		addSequential(turnToSwitch, 1.5);
        	addSequential(intakeOut, 2);
    	}
    	else{
        	addParallel(intakeIn);
        	addSequential(toSwitch, 3);
        	addSequential(elevatorUp, 2);
        	addSequential(intakeOut, 2);
    	}
    }
    
    @Override
    protected void initialize(){
    	intakeIn.SetParam(0.3);
    	intakeOut.SetParam(-0.4);
    	driveForwardShort.SetParam(
    			(Constants.kWallToSwitch/2.0) - (Constants.kChassisWidth/2.0),
    			3.0,
    			Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
                false);
    	turnLeft.SetParam(
    			Constants.kChassisWidth / 2.0,
    			2.0,
    			Math.PI / 2.0,
    			false);
    	driveForwardLong.SetParam(
    			(Constants.kSwitchWidth) - (Constants.kSwitchSlotWidth),
    			3.0,
    			Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
                false);
    	turnToSwitch.SetParam(
    			Constants.kChassisWidth / 2.0,
    			2.0,
    			- Math.PI / 2.0,
    			false);
    	if(side == 'L'){
    		toSwitch.SetParam(
    				Constants.kWallToSwitch - ((Constants.kWallToSwitch/2.0) + (Constants.kChassisLength/2.0)), 
    				3, 
    				Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
    				false
    	    );
    	}
    	else {
    		toSwitch.SetParam(
    				Constants.kWallToSwitch - (Constants.kChassisLength / 2.0), 
    				3, 
    				Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
    				false
    	    );
    	}
    }
}
