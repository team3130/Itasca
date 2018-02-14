package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.subsystems.Elevator;
import org.usfirst.frc.team3130.robot.util.Loop;


/**
 *
 */
public class HoldElevator implements Loop {
	static HoldElevator instance = new HoldElevator();
	public static double oldHeight = 0.0;
	public static double holdHeight = 0.0;
	public static boolean enableHold = false; //TODO: change to true when elevator gets encoder
	
	public static HoldElevator getInstance(){
		return instance;
	}
    public HoldElevator() {
        // Use requires() here to declare subsystem dependencies
    	
    }


	@Override
	public void onStart() {
		
		
	}

	@Override
	public void onLoop() {
		if (enableHold == true && holdHeight != oldHeight) {
			Elevator.GetInstance();
			Elevator.setHeight(holdHeight);
			oldHeight = holdHeight;
		}
		
	}

	@Override
	public void onStop() {
		
		
	}
}
