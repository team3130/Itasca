package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.Robot;
import org.usfirst.frc.team3130.robot.commands.HeightSetter;
import org.usfirst.frc.team3130.robot.commands.HeightSetter.Direction;
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
	
	private AutoDriveStraightToPoint driveForward;
	private AutoDriveStraightToPoint behindSwitch;
	private AutoDriveCurve           turnToScale;
	private AutoDriveCurve           turnLeft;
	private AutoDriveCurve			 turnRight;
	private HeightSetter			 elevatorUp;
	private RunIntakeIn				 intakeIn;
	private RunIntakeOut			 intakeOut;
	private char					 side;

    public ScaleOnly(char side) {
    	requires(Chassis.GetInstance());
    	requires(Elevator.GetInstance());
    	requires(CubeIntake.GetInstance());
    	
    	this.side    = side;
    	driveForward = new AutoDriveStraightToPoint();
    	behindSwitch = new AutoDriveStraightToPoint();
    	turnToScale  = new AutoDriveCurve();
    	turnLeft     = new AutoDriveCurve();
    	turnRight    = new AutoDriveCurve();
    	elevatorUp   = new HeightSetter(Direction.kUp);
    	intakeIn     = new RunIntakeIn();
    	intakeOut    = new RunIntakeOut();
    	
    	if(Robot.startPos.getSelected() == "Left"){
    		if(side == 'L'){
    			
    		}
    		else{
    			
    		}
    	}
    	else{
    		if(side == 'R'){
    			
    		}
    		else{
    			
    		}
    	}
    }
    
    protected void intialize(){
    	intakeIn.SetParam(0.3);
    	intakeOut.SetParam(-0.4);
    	turnLeft.SetParam(
    			Constants.kChassisWidth / 2.0,
    			1.0, 
    			Math.PI / 2.0,
    			true);
    	turnRight.SetParam(
    			Constants.kChassisWidth / 2.0,
    			1.0, 
    			- Math.PI / 2.0,
    			true);
    	//behindSwitch.SetParam(
    			//setpoint, threshold, speed, shiftHigh);
    	if(Robot.startPos.getSelected() == "Left"){
    		if(side == 'L'){
    			driveForward.SetParam(
    					Constants.kWallToScale - (Constants.kChassisLength/2.0),
    					3.0, 
    					Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7),
    					true
    			);
    		}
    		else{
    			driveForward.SetParam(
    					Constants.kWallToScale + Constants.kSwitchDepth,
    					3.0, 
    					Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7),
    					true
    			);
    		}
    	}
    	else{
    		if(side == 'R'){
    			driveForward.SetParam(
    					Constants.kWallToScale - (Constants.kChassisLength/2.0),
    					3.0, 
    					Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7),
    					true
    			);
    		}
    		else{
    			driveForward.SetParam(
    					Constants.kWallToScale + Constants.kSwitchDepth,
    					3.0, 
    					Preferences.getInstance().getDouble("AUTON Forward Speed", 0.7),
    					true
    			);
    		}
    	}
    }
}
