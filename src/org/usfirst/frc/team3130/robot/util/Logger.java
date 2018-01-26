package org.usfirst.frc.team3130.robot.util;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Log information to the Driver Station so that we may refer to the logs when things go sideways
 * @author Ashley
 */
public class Logger {

	public static void logRobotStartup(){
		System.out.println("robot startup");
	}
	
	public static void logTeleopInit(){
		System.out.println("teleop init");
	}
	
	public static void logAutonInit(){
		System.out.println("auton init");
	}
	
	public static void logRobotDisabled(){
		System.out.println("robot disabled");
	}
	
	public static void logMatchInfo(){
		try {
			System.out.println("Event: " + DriverStation.getInstance().getEventName());
		} catch (Exception e) {
			System.out.println("Event: Unknown");
		}
		try {
			System.out.println("Match Type: " + DriverStation.getInstance().getMatchType());
		} catch (Exception e){
			System.out.println("Match Type: Unknown");
		}
		try {
			System.out.println("Match Number: " + DriverStation.getInstance().getMatchNumber());
		} catch (Exception e){
			System.out.println("Match Number: Unknown");
		}
		try {
			System.out.println("Alliance: " + DriverStation.getInstance().getAlliance());
		} catch (Exception e){
			System.out.println("Alliance: Unknown");
		}
	}
}
