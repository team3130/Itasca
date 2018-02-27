package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
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
 *  Deposit cube into switch from the side
 */
public class SwitchSide extends CommandGroup {

	private AutoDelay				 delay;
	private ContDrive                driveForward;
	private RunIntakeIn	             intakeIn;
	private ContTurnDist      		 turnToSwitch;
	private ContDrive				 toSwitch;
	private ElevatorToHeight		 elevatorUp;
	private RunIntakeOut			 intakeOut;
	private ContTurnDist      		 turnDownField;
	private ContDrive                driveDownField;
	private char					 side;
	
    public SwitchSide(char side) {
    	requires(Chassis.GetInstance());
    	requires(CubeIntake.GetInstance());
    	requires(Elevator.GetInstance());
    	
    	this.side      = side;
    	delay		   = new AutoDelay();
    	driveForward   = new ContDrive();
    	intakeIn  	   = new RunIntakeIn();
    	turnToSwitch   = new ContTurnDist();
    	//toSwitch 	   = new ContDrive();
    	elevatorUp 	   = new ElevatorToHeight(35.0);
    	intakeOut 	   = new RunIntakeOut();
    	turnDownField  = new ContTurnDist();
    	driveDownField   = new ContDrive();
    	
    	//addParallel(intakeIn);
    	addParallel(elevatorUp);
    	addSequential(driveForward, 3);
    	addSequential(turnToSwitch, 1);
    	//addSequential(toSwitch, 2);
    	addSequential(delay, 1);
    	addSequential(intakeOut, 1);
    	addSequential(turnDownField, 1);
    	addSequential(driveDownField, 2);
    	
    }
    
    @Override
    protected void initialize(){
    	intakeIn.SetParam(0.3);
    	driveForward.SetParam(
            Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
    		Constants.kWallToSwitch - (Constants.kChassisLength / 2.0)
        );
    	if(side == 'L'){
    		turnToSwitch.SetParam(0.31, 90*(Math.PI/180f));
        	turnDownField.SetParam(-0.6, 90*(Math.PI/180f));
    	}
    	else{
    		turnToSwitch.SetParam(0.31, -90*(Math.PI/180f));
        	turnDownField.SetParam(-0.6, -90*(Math.PI/180f));
    	}
    	intakeOut.SetParam(-0.4);
    	driveDownField.SetParam(0.7, 70.0);
    }
}
