/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3130.robot;

import org.usfirst.frc.team3130.robot.autoCommands.AutoDriveStraightToPoint;
import org.usfirst.frc.team3130.robot.autoCommands.AutoTurn;
import org.usfirst.frc.team3130.robot.commands.BasicActuate;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.HookToggle;
import org.usfirst.frc.team3130.robot.commands.IntakeToggle;
import org.usfirst.frc.team3130.robot.commands.OpenIntake;
import org.usfirst.frc.team3130.robot.commands.ReverseClimb;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.commands.Shift;
import org.usfirst.frc.team3130.robot.commands.TestPIDCurve;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
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
	private static JoystickButton shiftUp;
	private static JoystickButton shiftDown;
	private static JoystickButton shift;

	public static POVTrigger elevatorMax;
	public static POVTrigger elevatorScaleLevel;
	public static POVTrigger elevatorScaleLowest;
	public static JoystickButton elevatorSwitch;
	public static JoystickButton elevatorLift;

	public static JoystickButton cubeIn;
	public static JoystickButton cubeOut;
	public static JoystickButton cubeActuate;
	public static JoystickButton cubeActuateL;
	public static JoystickButton cubeActuateR;
	
	public static JoystickButton hookActuate;
	
	public static JoystickButton wingsDeploy;

	public static JoystickButton changeDriveMode;
	public static JoystickButton testStraight;
	public static JoystickButton testCurve;
	
	public static JoystickButton revClimb;
	
	private static AutoDriveStraightToPoint testDrive;
	private static AutoTurn testTurn;

	private OI() {
		//~~~~~~~~~~~~~~~~~~~~~~Create Controls~~~~~~~~~~~~~~~~~~~~
		//Create Joysticks
		stickL = new Joystick(0);
		stickR = new Joystick(1);
		gamepad = new Joystick(2);
		
		//Create Joystick Buttons
		cubeIn = new JoystickButton(stickR, 1);
		cubeOut = new JoystickButton(stickL, 1);
		cubeActuate = new JoystickButton(stickR, RobotMap.BTN_CUBEACTUATE);
		cubeActuateL = new JoystickButton(stickL, 7);
		cubeActuateR = new JoystickButton(stickR, 7);
		
		hookActuate = new JoystickButton(gamepad, RobotMap.BTN_HOOKACTUATE);
		
		changeDriveMode = new JoystickButton(stickL, 12);
		shiftUp = new JoystickButton(stickR, RobotMap.BTN_SHIFTUP);
		shiftDown = new JoystickButton(stickL, RobotMap.BTN_SHIFTDOWN);
		shift = new JoystickButton(stickL, RobotMap.BTN_SHIFT);
		testStraight = new JoystickButton(stickL, 10);
		testCurve = new JoystickButton(stickR, 10);
		
		elevatorMax=new POVTrigger(gamepad, RobotMap.POV_ELEVATORMAX);
		elevatorScaleLevel=new POVTrigger(gamepad, RobotMap.POV_ELEVATORSCALELEVEL);
		elevatorScaleLowest=new POVTrigger(gamepad, RobotMap.POV_ELEVATORSCALELOWEST);
		elevatorSwitch = new JoystickButton(gamepad, RobotMap.BTN_ELEVATORSWITCHHIGHT);
		elevatorLift = new JoystickButton(gamepad, RobotMap.BTN_ELEVATORLIFT);
		
		wingsDeploy = new JoystickButton(gamepad, RobotMap.BTN_WINGSDEPLOY);
		
		revClimb = new JoystickButton(stickL, RobotMap.BTN_REV_CLIMB);
		
		testDrive=new AutoDriveStraightToPoint();
		testDrive.SetParam(6, 1, 1, false);
		
		testTurn=new AutoTurn();
		testTurn.setParam(90,2);
		
		//Bind buttons to commands
		//changeDriveMode.whenPressed(new ChangeDriveMode());
		shift.whenPressed(new Shift());
		//shiftUp.whenPressed(new DriveShiftUp());
		//shiftDown.whenPressed(new DriveShiftDown());
		//testStraight.whileHeld(testDrive);
		//testCurve.whenPressed(new TestPIDCurve());//testTurn);

		//TODO: Find good defaults
		elevatorMax.whenActive(new ElevatorToHeight(Preferences.getInstance().getDouble("Preset Elevator Max", 98)));
		elevatorScaleLevel.whenActive(new ElevatorToHeight(Preferences.getInstance().getDouble("Preset Elevator Scale Level", 82)));
		elevatorScaleLowest.whenActive(new ElevatorToHeight(Preferences.getInstance().getDouble("Preset Elevator Scale Lowest", 66)));
		elevatorSwitch.whenActive(new ElevatorToHeight(Preferences.getInstance().getDouble("Preset Elevator Switch", 32)));
		elevatorLift.whenActive(new ElevatorToHeight(Preferences.getInstance().getDouble("Preset Elevator Lift", 11.5)));
		
		cubeIn.whileHeld(new RunIntakeIn());
		cubeOut.whileHeld(new RunIntakeOut());
		//cubeActuate.whenPressed(new IntakeToggle());
		cubeActuate.whileHeld(new OpenIntake());
		//cubeActuateL.whenPressed(new IntakeToggleL());
		//cubeActuateR.whenPressed(new IntakeToggleR());
		
		hookActuate.whenPressed(new HookToggle());
		
		wingsDeploy.whenPressed(new BasicActuate(Robot.bcWingsDeploy));
		
		revClimb.whenPressed(new ReverseClimb());
	}
}

