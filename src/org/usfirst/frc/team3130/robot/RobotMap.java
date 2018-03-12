/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3130.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	

	//Motors-CAN
	public static final int CAN_PNMMODULE = 1;
	public static final int CAN_LEFTMOTORFRONT = 2;
	public static final int CAN_LEFTMOTORREAR = 3;
	public static final int CAN_RIGHTMOTORFRONT = 4;
	public static final int CAN_RIGHTMOTORREAR = 5;
	public static final int CAN_ELEVATOR1 = 6;
	public static final int CAN_ELEVATOR2 = 7;
	public static final int CAN_INTAKE_L = 8;
	public static final int CAN_CLIMBWINCHL1 = 9; 
	public static final int CAN_CLIMBWINCHL2 = 10; 
	public static final int CAN_CLIMBWINCHR1 = 11; 
	public static final int CAN_CLIMBWINCHR2 = 12; 
	public static final int CAN_HDWINCH = 13;
	public static final int CAN_INTAKE_R = 14;
	
	//Gamepad Button List
	public static final int LST_BTN_A = 1;
	public static final int LST_BTN_B = 2;
	public static final int LST_BTN_X = 3;
	public static final int LST_BTN_Y = 4;
	public static final int LST_BTN_LBUMPER = 5;
	public static final int LST_BTN_RBUMPER = 6;
	public static final int LST_BTN_BACK = 7;
	public static final int LST_BTN_START = 8;
	public static final int LST_BTN_RJOYSTICKPRESS = 9;
	public static final int LST_BTN_LJOYSTICKPRESS = 10;

	//Gamepad Axis List
	public static final int LST_AXS_LJOYSTICKX = 0;
	public static final int LST_AXS_LJOYSTICKY = 1;
	public static final int LST_AXS_LTRIGGER = 2;
	public static final int LST_AXS_RTRIGGER = 3;
	public static final int LST_AXS_RJOYSTICKX = 4;
	public static final int LST_AXS_RJOYSTICKY = 5;

	//POV Degress List
	public static final int LST_POV_UNPRESSED = -1;
	public static final int LST_POV_N = 0;
	public static final int LST_POV_NE = 45;
	public static final int LST_POV_E = 90;
	public static final int LST_POV_SE = 135;
	public static final int LST_POV_S = 180;
	public static final int LST_POV_SW = 225;
	public static final int LST_POV_W = 270;
	public static final int LST_POV_NW = 315;
	
	//Cube Intake
	public static final int BTN_CUBEACTUATE = 3; //Joystick R
	public static final int PNM_CUBEACTUATEL = 0;
	public static final int PNM_CUBEACTUATER = 1;

	//Climber
	public static final int AXS_CLIMB1 = LST_AXS_LTRIGGER; 
	public static final int AXS_CLIMB2 = LST_AXS_RTRIGGER; 
	public static final int BTN_REV_CLIMB = 8; //Joystick L
	
	//Hook Deploy
	public static final int PNM_HOOKACTUATE = 4;
	public static final int BTN_HOOKACTUATE = LST_BTN_Y;
	public static final int AXS_HOOKDEPLOY = LST_AXS_LJOYSTICKY;
	
	//Wings
	public static final int PNM_WINGSDEPLOY = 3; 
	public static final int BTN_WINGSDEPLOY = LST_BTN_RBUMPER;
	
	//Chassis
	public static final int PNM_SHIFT = 2;
	public static final int BTN_SHIFT = 4; //Joystick L
	public static final int BTN_SHIFTDOWN = 5;	//Joystick L
	public static final int BTN_SHIFTUP = 6;	//Joystick L
	
	//Elevator Presets
	public static final int POV_ELEVATORMAX = LST_POV_N;
	public static final int POV_ELEVATORSCALELEVEL = LST_POV_W;
	public static final int POV_ELEVATORSCALELOWEST = LST_POV_E;
	public static final int BTN_ELEVATORSWITCHHIGHT = LST_BTN_Y;
	public static final int BTN_ELEVATORLIFT = LST_BTN_A;
}
