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
	private AutoDriveStraightToPoint driveForward;
	private AutoDriveCurve			 turnToSwitch;
	private AutoDriveStraightToPoint toSwitch;
	private RunIntakeIn	             intakeIn;
	private HeightSetter			 elevatorUp;
	private RunIntakeOut			 intakeOut;
	
    public SwitchFront(char side) {
    	requires(Chassis.GetInstance());
    	requires(CubeIntake.GetInstance());
    	requires(Elevator.GetInstance());
    	
    	toSwitch   	   = new AutoDriveStraightToPoint();
    	driveForward   = new AutoDriveStraightToPoint();
    	turnLeft	   = new AutoDriveCurve();
    	turnToSwitch   = new AutoDriveCurve();
    	intakeIn  	   = new RunIntakeIn();
    	elevatorUp	   = new HeightSetter(Direction.kUp);
    	intakeOut  	   = new RunIntakeOut();
    	
    	if(side == 'L'){
    		addParallel(intakeIn);
    		addSequential(turnLeft, 2);
    		addSequential(driveForward, 2);
        	addParallel(elevatorUp, 2);
    		addSequential(turnToSwitch, 2);
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
    	toSwitch.SetParam(
    		Constants.kWallToSwitch - (Constants.kChassisLength / 2.0), 
            3, 
            Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
            false
        );
    	intakeOut.SetParam(-0.4);
    }
}
