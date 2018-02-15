/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3130.robot;

import org.usfirst.frc.team3130.robot.commands.BasicActuate;
import org.usfirst.frc.team3130.robot.commands.BasicActuateToggle;
import org.usfirst.frc.team3130.robot.commands.BasicSpinMotor;
import org.usfirst.frc.team3130.robot.commands.ChangeDriveMode;
import org.usfirst.frc.team3130.robot.commands.ElevatorDown;
import org.usfirst.frc.team3130.robot.commands.ElevatorUp;
import org.usfirst.frc.team3130.robot.commands.HeightSetter;
import org.usfirst.frc.team3130.robot.commands.HookToggle;
import org.usfirst.frc.team3130.robot.commands.IntakeToggle;
import org.usfirst.frc.team3130.robot.commands.IntakeToggleL;
import org.usfirst.frc.team3130.robot.commands.IntakeToggleR;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.
	@SuppressWarnings("unused")
	private class JoystickTrigger extends Trigger{

		private Joystick stick;
		private int axis;
		private double threshold;
		
		private JoystickTrigger(Joystick stick, int axis){
			this.stick = stick;
			this.axis = axis;
			threshold = 0.1;
		}
		
		private JoystickTrigger(Joystick stick, int axis, double threshold){
			this.stick = stick;
			this.axis = axis;
			this.threshold = threshold;
		}
		
		@Override
		public boolean get() {
			return stick.getRawAxis(axis) > threshold;
		}
		
	}
	
	private class POVTrigger extends Trigger{

		private Joystick stick;
		private int POV;
		
		public POVTrigger(Joystick stick, int POV) {
			this.stick = stick;
			this.POV = POV;
		}
		
		@Override
		public boolean get() {
			return stick.getPOV(0)==POV;
		}
		
	}
	
	//Instance Handling
	private static OI m_pInstance;
    public static OI GetInstance()
    {
    	if(m_pInstance == null) m_pInstance = new OI();
    	return m_pInstance;
    }
    
    //~~~~~~~~~~~~~~~~~~Definitions~~~~~~~~~~~~~~~~~~~~
    //Joysticks 
    public static Joystick stickL;
	public static Joystick stickR;
	public static Joystick gamepad;
	
	//Buttons
	public static POVTrigger elevatorUp;
	public static POVTrigger elevatorDown;
	
	public static JoystickButton cubeIn;
	public static JoystickButton cubeOut;
	public static JoystickButton cubeActuate;
	public static JoystickButton cubeActuateL;
	public static JoystickButton cubeActuateR;
	
	public static JoystickButton hookActuate;
	
	public static JoystickButton wingsDeploy;

	public static JoystickButton changeDriveMode;
	

	private OI() {
		//~~~~~~~~~~~~~~~~~~~~~~Create Controls~~~~~~~~~~~~~~~~~~~~
		//Create Joysticks
		stickL = new Joystick(0);
		stickR = new Joystick(1);
		gamepad = new Joystick(2);
		
		//Create Joystick Buttons
		cubeIn = new JoystickButton(stickL, 1);
		cubeOut = new JoystickButton(stickR, 1);
		cubeActuate = new JoystickButton(stickR, 3);
		cubeActuateL = new JoystickButton(stickL, 7);
		cubeActuateR = new JoystickButton(stickR, 7);
		
		elevatorUp = new POVTrigger(stickR, 0);
		elevatorDown = new POVTrigger(stickR, 180);
		
		hookActuate = new JoystickButton(gamepad, RobotMap.BTN_HOOKACTUATE);
		
		changeDriveMode = new JoystickButton(stickL, 12);
		
		//Bind buttons to commands
		changeDriveMode.whenPressed(new ChangeDriveMode());
		
		elevatorUp.whenActive(new ElevatorUp());
		elevatorDown.whenActive(new ElevatorDown());
		
		cubeIn.whileHeld(new RunIntakeIn());
		cubeOut.whileHeld(new RunIntakeOut());
		cubeActuate.whenPressed(new IntakeToggle());
		//cubeActuateL.whenPressed(new IntakeToggleL());
		//cubeActuateR.whenPressed(new IntakeToggleR());
		
		hookActuate.whenPressed(new HookToggle());
		
		wingsDeploy.whileHeld(new BasicActuate(Robot.bcWingsDeploy));
	}
}

