package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.subsystems.UsbCameraInterface;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DetermineAuton extends Command {

    public DetermineAuton() {
       
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//Get field configuration
    	String gameData;
    	gameData = DriverStation.getInstance().getGameSpecificMessage();
    	StringBuilder st = new StringBuilder(gameData);
    	st.deleteCharAt(2);
    	String fieldInfo;
    	fieldInfo = st.toString();    	
    	//find robot starting pose
    	if (UsbCameraInterface.returnXPosition() >= 0.0){
    		//Start on right
    		if(fieldInfo == "LL"){
    			
    		}else if(fieldInfo == "LR"){
    			
    		}else if(fieldInfo == "RR"){
    			
    		}else if(fieldInfo == "RL"){
    			
    		}
    	}else{
    		if(fieldInfo == "LL"){
    			
    		}else if(fieldInfo == "LR"){
    			
    		}else if(fieldInfo == "RR"){
    			
    		}else if(fieldInfo == "RL"){
    			
    		}
    	}
    	
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
