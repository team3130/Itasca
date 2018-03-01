package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.continuousDrive.ContDrive;
import org.usfirst.frc.team3130.robot.continuousDrive.ContTurnDist;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Auton to drop cube in the scale
 */
public class ScaleOnly extends CommandGroup {
	
	private ContDrive     	  driveForward;
	private ContDrive     	  behindSwitch;
	private ContDrive     	  toScale;
	private ContDrive		  backUp;
	private ContTurnDist  	  turnToScale;
	private ContTurnDist  	  turnLeft;
	private ContTurnDist      turnRight;
	private ElevatorToHeight  elevatorUpSmall;
	private ElevatorToHeight  elevatorUp;
	private ElevatorToHeight  elevatorDown;
	private RunIntakeIn	 	  intakeIn;
	private RunIntakeOut  	  intakeOut;
	private AutoDelay	 	  delay1;
	private AutoDelay	 	  delay2;
	private AutoDelay	 	  delay3;
	private AutoDelay	 	  delay4;
	private AutoDelay	 	  delay5;
	private char		  	  start;
	private char		  	  side;

    public ScaleOnly(char start, char side) {
    	requires(Chassis.GetInstance());
    	requires(Elevator.GetInstance());
    	requires(CubeIntake.GetInstance());
    	
    	this.side       = side;
    	this.start      = start;
    	driveForward    = new ContDrive();
    	behindSwitch    = new ContDrive();
    	backUp		    = new ContDrive();
    	toScale			= new ContDrive();
    	turnToScale     = new ContTurnDist();
    	turnLeft        = new ContTurnDist();
    	turnRight       = new ContTurnDist();
    	elevatorUpSmall = new ElevatorToHeight(10.0);
    	elevatorUp      = new ElevatorToHeight(80.0);
    	elevatorDown    = new ElevatorToHeight(10.0);
    	intakeIn        = new RunIntakeIn();
    	intakeOut       = new RunIntakeOut();
    	delay1          = new AutoDelay();
    	delay2          = new AutoDelay();
    	delay3          = new AutoDelay();
    	delay4          = new AutoDelay();
    	delay5          = new AutoDelay();
    	

		addParallel(intakeIn, 1.0);
		addParallel(elevatorUpSmall, 1.0);
		
    	if(start == 'L'){
    		if(side == 'L'){
    			addSequential(driveForward, 5.0);
    			addSequential(delay1, 0.5);
    			addSequential(turnRight, 1.0);
    		}
    		else{
    			addSequential(driveForward, 3.0);
    			addSequential(delay1, 0.5);
    			addSequential(turnRight, 1.0);
    			addSequential(delay2, 0.5);
    			addSequential(behindSwitch, 4.0);
    			addSequential(delay3, 0.5);
    			addSequential(turnLeft, 1.0);
    			addSequential(delay4, 0.5);
    			addSequential(toScale, 1.0);
    			addSequential(delay5, 0.5);
    			addSequential(turnToScale, 1.0);
    		}
    	}
    	else{
    		if(side == 'R'){
    			addSequential(driveForward, 5.0);
    			addSequential(delay1, 0.5);
    			addSequential(turnLeft, 1.0);
    		}
    		else{
    			addSequential(driveForward, 3.0);
    			addSequential(delay1, 0.5);
    			addSequential(turnLeft, 1.0);
    			addSequential(delay2, 0.5);
    			addSequential(behindSwitch, 4.0);
    			addSequential(delay3, 0.5);
    			addSequential(turnRight, 1.0);
    			addSequential(delay4, 0.5);
    			addSequential(toScale, 1.0);
    			addSequential(delay5, 0.5);
    			addSequential(turnToScale, 1.0);
    		}
    	}

		addSequential(elevatorUp, 3.0);
		addSequential(intakeOut, 1.0);
		addParallel(elevatorDown, 3.0);
		addSequential(backUp, 1.0);
    }
    
    protected void intialize(){
    	intakeIn.SetParam(0.6);
    	intakeOut.SetParam(-0.4);
    	turnLeft.SetParam(0.6, -Math.PI/2.0);
    	turnRight.SetParam(0.6, Math.PI / 2.0); 
    	behindSwitch.SetParam(0.7, Constants.kAllianceWallWidth - (Constants.kChassisBLength));
    	toScale.SetParam(0.7, 
    			Constants.kWallToScale - (0.5 * (Constants.kWallToSwitchRL + 
    			Constants.kWallToSwitchBL) + Constants.kSwitchDepth + 13.0));
    	backUp.SetParam(-0.4, 10.0);
    	if(start == 'L'){
    		if(side == 'L'){
    			driveForward.SetParam(0.7, Constants.kWallToScale - (Constants.kChassisBLength/2.0));
    		}
    		else{
    			driveForward.SetParam(0.7, 
    					0.5 * (Constants.kWallToSwitchRL + Constants.kWallToSwitchBL) + 
    					Constants.kSwitchDepth + 13.0);
    			turnToScale.SetParam(0.6, -Math.PI/2.0);
    		}
    	}
    	else{
    		if(side == 'R'){
    			driveForward.SetParam(0.7, Constants.kWallToScale - (Constants.kChassisLength/2.0));
    		}
    		else{
    			driveForward.SetParam(0.7, 
    					0.5 * (Constants.kWallToSwitchRR + Constants.kWallToSwitchBR) + 
    					Constants.kSwitchDepth + 13.0);
    			turnToScale.SetParam(0.6, Math.PI/2.0);
    		}
    	}
    }
}
