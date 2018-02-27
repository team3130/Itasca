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

	private ContTurnHeading   turn1;
	private ContDrive 	      driveForward;
	private ContTurnHeading   turn2;
	private ContDrive         toSwitch;
	private RunIntakeIn	      intakeIn;
	private ElevatorToHeight  elevatorUp;
	private RunIntakeOut	  intakeOut;
	private char     		  side;
	private AutoDelay		  delay1;
	private AutoDelay		  delay2;
	private AutoDelay		  delay3;
	
    public SwitchFront(char side) {
    	requires(Chassis.GetInstance());
    	requires(CubeIntake.GetInstance());
    	requires(Elevator.GetInstance());
    	
    	this.side      	   = side;
    	driveForward       = new ContDrive();
    	turn1	   	  	   = new ContTurnHeading(driveForward);
    	delay1			   = new AutoDelay();
    	turn2   	   	   = new ContTurnHeading(turn1);
    	delay2			   = new AutoDelay();
    	toSwitch           = new ContDrive(turn2);
    	delay3			   = new AutoDelay();
    	intakeIn  	       = new RunIntakeIn();
    	elevatorUp	       = new ElevatorToHeight(40.0);
    	intakeOut  	       = new RunIntakeOut();

    	addParallel(intakeIn);
    	//addSequential(driveForward, 1.5);
    	addSequential(turn1, 1.5);
    	addSequential(delay1, 0.5);
    	addSequential(turn2, 1.5);
    	addSequential(delay2, 0.5);
       	addParallel(elevatorUp, 1);
       	addSequential(toSwitch, 2);
       	addSequential(delay3, 0.5);
    	addSequential(intakeOut, 2);
    }
    
    @Override
    protected void initialize(){
    	intakeIn.SetParam(0.3);
    	intakeOut.SetParam(-0.4);
    	driveForward.SetParam(
    			Preferences.getInstance().getDouble("AUTON Forward Speed", 0.4), 
    			(Constants.kWallToSwitch/2.0) - (Constants.kChassisBLength)
    	);
    	if(side == 'L'){
        	turn1.SetParam(0.3, -Math.PI / 3.0);
        	turn2.SetParam(0.3, Math.PI / 3.0);
    	}
    	else{
    		turn1.SetParam(0.3, Math.PI / 3.0);
        	turn2.SetParam(0.3, -Math.PI / 3.0);
    	}
    	toSwitch.SetParam(
    			Preferences.getInstance().getDouble("AUTON Forward Speed", 0.4), 
    			Constants.kWallToSwitch - ((Constants.kWallToSwitch/2.0) - (Constants.kChassisLength/2.0) +
    									   (Constants.kChassisBWidth/2.0))
    	);
    }
}
