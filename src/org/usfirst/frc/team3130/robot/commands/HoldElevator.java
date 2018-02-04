package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.subsystems.Elevator;
import org.usfirst.frc.team3130.robot.util.Loop;


/**
 *
 */
public class HoldElevator implements Loop {
	static HoldElevator instance = new HoldElevator();
	public static double holdHeight = 0.0;
	public static boolean enableHold = false; //TODO: change to true when elevator gets encoder
	
	public static HoldElevator getInstance(){
		return instance;
	}
    public HoldElevator() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    }


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoop() {
		System.out.println("HoldElevator Running..........................");
		if (enableHold == true){
			Elevator.GetInstance();
			Elevator.setHeight(holdHeight);
		}
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}
}
