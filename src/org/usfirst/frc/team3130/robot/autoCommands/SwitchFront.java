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
import org.usfirst.frc.team3130.robot.continuousDrive.ContTurnHeading;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Deposit cube into switch from the front
 *  (from center starting config, directly in front of right switch)
 */
public class SwitchFront extends CommandGroup {

	private ContTurnDist      turn1;
	private ContDrive 	      driveForward;
	private ContDrive		  driveBetween;
	private ContTurnDist      turn2;
	private ContDrive         toSwitch;
	private RunIntakeIn	      intakeIn;
	private ElevatorToHeight  elevatorUp;
	private RunIntakeOut	  intakeOut;
	private char     		  side;
	private AutoDelay		  delay1;
	private AutoDelay		  delay2;
	private AutoDelay		  delay3;
	private AutoDelay		  delay4;
	private ContDrive		  backUp;
	
    public SwitchFront(char side) {
    	requires(Chassis.GetInstance());
    	requires(CubeIntake.GetInstance());
    	requires(Elevator.GetInstance());
    	
    	this.side      	   = side;
    	driveForward       = new ContDrive();
    	turn1	   	  	   = new ContTurnDist(driveForward);
    	delay1			   = new AutoDelay();
    	driveBetween	   = new ContDrive();
    	delay2			   = new AutoDelay();
    	turn2   	   	   = new ContTurnDist(turn1);
    	delay3			   = new AutoDelay();
    	toSwitch           = new ContDrive(turn2);
    	delay4			   = new AutoDelay();
    	intakeIn  	       = new RunIntakeIn();
    	elevatorUp	       = new ElevatorToHeight(35.0);
    	intakeOut  	       = new RunIntakeOut();
    	backUp			   = new ContDrive();

    	addParallel(intakeIn);
       	addParallel(elevatorUp, 1);
    	addSequential(driveForward, 1.5);
    	addSequential(turn1, 4);
    	addSequential(delay1, 0.5);
    	addSequential(driveBetween, 0.5);
    	addSequential(delay2, 0.5);
    	addSequential(turn2, 4);
       	addSequential(toSwitch, 2);
       	addSequential(delay4, 0.7);
    	addSequential(intakeOut, 2);
    	//addSequential(backUp, 2);
    }
    
    @Override
    protected void initialize(){
    	intakeIn.SetParam(0.5);
    	intakeOut.SetParam(-0.4);
    	driveForward.SetParam(
    			Preferences.getInstance().getDouble("AUTON Forward Speed", 0.4), 
    			(Constants.kWallToSwitchRL/2.0) - (Constants.kChassisBLength + 10.0)
    	);
    	if(side =='L'){
    	driveBetween.SetParam(
    			Preferences.getInstance().getDouble("AUTON Forward Speed", 0.4), 
    			26.0
    	);
    	}else{
    		driveBetween.SetParam(
        			Preferences.getInstance().getDouble("AUTON Forward Speed", 0.4), 
        			18.0);
    	}
    	if(side == 'L'){
        	turn1.SetParam(0.6, -85.0*(Math.PI / 180.0));
        	turn2.SetParam(0.6, 75.0*(Math.PI / 180.0));
    	}
    	else{
    		turn1.SetParam(0.6, 90.0*(Math.PI / 180.0));
        	turn2.SetParam(0.6, -80.0*(Math.PI / 180.0));
    	}
    	toSwitch.SetParam(
    			Preferences.getInstance().getDouble("AUTON Forward Speed", 0.4), 
    			Constants.kWallToSwitchRL - ((Constants.kWallToSwitchRL/2.0) - (Constants.kChassisLength/2.0) +
    									   (Constants.kChassisBWidth/2.0))
    	);
    	/*backUp.SetParam(
    			Preferences.getInstance().getDouble("AUTON Forward Speed", -0.4), 
    			1.0
    	);*/
    }
}
