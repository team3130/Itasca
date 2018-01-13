/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3130.robot;

import org.usfirst.frc.team3130.robot.commands.BasicSpinMotor;

import edu.wpi.first.wpilibj.Joystick;
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
	public static JoystickButton cubeInL;
	public static JoystickButton cubeInR;
	public static JoystickButton cubeOutL;
	public static JoystickButton cubeOutR;

	private OI() {
		//~~~~~~~~~~~~~~~~~~~~~~Create Controls~~~~~~~~~~~~~~~~~~~~
		//Create Joysticks
		stickL = new Joystick(0);
		stickR = new Joystick(1);
		gamepad = new Joystick(2);
		
		//Create Joystick Buttons
		cubeInL = new JoystickButton(stickL, 1);
		cubeInR = new JoystickButton(stickL, 1);
		cubeOutL = new JoystickButton(stickR, 1);
		cubeOutR = new JoystickButton(stickR, 1);
		
		//Bind buttons to commands
		cubeInL.whileHeld(new BasicSpinMotor(Robot.btCubeIntakeLeft, -0.5));
		cubeInR.whileHeld(new BasicSpinMotor(Robot.btCubeIntakeRight, -0.5));
		cubeOutL.whileHeld(new BasicSpinMotor(Robot.btCubeIntakeLeft, 0.5));
		cubeOutR.whileHeld(new BasicSpinMotor(Robot.btCubeIntakeRight, 0.5));
	}
}

