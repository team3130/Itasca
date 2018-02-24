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
 *  Deposit cube into switch from the side
 */
public class SwitchSide extends CommandGroup {

	private AutoDriveStraightToPoint driveForward;
	private RunIntakeIn	             intakeIn;
	private AutoDriveCurve			 turnToSwitch;
	private AutoDriveStraightToPoint toSwitch;
	private HeightSetter			 elevatorUp;
	private RunIntakeOut			 intakeOut;
	
    public SwitchSide() {
    	requires(Chassis.GetInstance());
    	requires(CubeIntake.GetInstance());
    	requires(Elevator.GetInstance());
    	
    	driveForward   = new AutoDriveStraightToPoint();
    	intakeIn  	   = new RunIntakeIn();
    	turnToSwitch   = new AutoDriveCurve();
    	toSwitch 	   = new AutoDriveStraightToPoint();
    	elevatorUp 	   = new HeightSetter(Direction.kUp);
    	intakeOut 	   = new RunIntakeOut();
    	
    	addParallel(intakeIn);
    	addSequential(driveForward, 3);
    	addSequential(turnToSwitch, 2);
    	addSequential(toSwitch, 2);
    	addSequential(elevatorUp, 2);
    	addSequential(intakeOut, 2);
    }
    
    @Override
    protected void initialize(){
    	intakeIn.SetParam(0.3);
    	driveForward.SetParam(
    		Constants.kWallToSwitch - (Constants.kChassisLength / 2.0), 
            3, 
            Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7), 
            false
        );
    	intakeOut.SetParam(-0.4);
    }
}
