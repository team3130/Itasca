/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3130.robot;

import org.usfirst.frc.team3130.robot.autoCommands.PassBaseline;
import org.usfirst.frc.team3130.robot.autoCommands.RunMotionProfiles;
import org.usfirst.frc.team3130.robot.autoCommands.ScaleOnly;
import org.usfirst.frc.team3130.robot.autoCommands.SwitchFront;
import org.usfirst.frc.team3130.robot.autoCommands.SwitchSide;
import org.usfirst.frc.team3130.robot.commands.LocationCollector;
import org.usfirst.frc.team3130.robot.commands.RobotSensors;
import org.usfirst.frc.team3130.robot.commands.TestPIDCurve;
import org.usfirst.frc.team3130.robot.commands.TestPIDStraight;
import org.usfirst.frc.team3130.robot.sensors.LocationCamera.Location;
import org.usfirst.frc.team3130.robot.subsystems.AndroidInterface;
import org.usfirst.frc.team3130.robot.subsystems.BasicCylinder;
import org.usfirst.frc.team3130.robot.subsystems.BlinkinInterface;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.Climber;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;
import org.usfirst.frc.team3130.robot.subsystems.HookDeploy;
import org.usfirst.frc.team3130.robot.vision.VisionProcessor;
import org.usfirst.frc.team3130.robot.vision.VisionServer;
import org.usfirst.frc.team3130.robot.util.Logger;
import org.usfirst.frc.team3130.robot.util.Looper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	
	Command autonomousCommand;
	private SendableChooser<String> chooser  = new SendableChooser<String>();
	public static SendableChooser<String> startPos = new SendableChooser<String>();
	RobotSensors robotSensors;
	private Location robotLocation;
	//VisionServer mVisionServer = VisionServer.getInstance();
	Command locationCollector = new LocationCollector(robotLocation);
	
	// Enabled looper is called at 10Hz whenever the robot is enabled, frequency can be changed in Constants.java: kLooperDt
    Looper mEnabledLooper = new Looper();
    // Disabled looper is called at 10Hz whenever the robot is disabled
    Looper mDisabledLooper = new Looper();
    
    public static BasicCylinder bcWingsDeploy;
	//public static Compressor compressor = new Compressor(1);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		robotSensors = new RobotSensors();
		robotSensors.start();
		Logger.logMatchInfo();
		Logger.logRobotStartup();
		//compressor.start();
		
		bcWingsDeploy = new BasicCylinder(RobotMap.PNM_WINGSDEPLOY, "Wings", "Wings Deploy");
		
		OI.GetInstance();
		Chassis.GetInstance();
		CubeIntake.GetInstance();
		Elevator.GetInstance();
		Climber.GetInstance();
		BlinkinInterface.GetInstance();
		HookDeploy.GetInstance();
		
		//Vision operation
//		AndroidInterface.GetInstance();
//		AndroidInterface.GetInstance().reset();
//		VisionServer.getInstance();
//		VisionServer.getInstance().requestAppStart();
//		mVisionServer.addVisionUpdateReceiver(VisionProcessor.getInstance());
		
//		mEnabledLooper.register(VisionProcessor.getInstance());
		
		//Auton command to run chooser
		chooser.addObject("No Auton", null);
		chooser.addObject("Test MP", "Test MP");
		chooser.addObject("Pass Baseline", "Pass Baseline");
		chooser.addObject("Side", "Side");
		chooser.addObject("Switch Front", "Switch Front");
		SmartDashboard.putData("Auto mode", chooser);
		

		//Starting configuration for autons
		//If hardcoding required, manually choose fieldSide below
		startPos.addDefault("Left Starting Position", "Left");
		startPos.addObject("Right Starting Position", "Right");
		SmartDashboard.putData("Starting Position", startPos);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		Logger.logRobotDisabled();
		CubeIntake.reset();
		HookDeploy.reset();
		Chassis.ResetEncoders();
		Chassis.ReleaseAngle();
		bcWingsDeploy.actuate(false);
		mEnabledLooper.stop();
        mDisabledLooper.start();
		//locationCollector.start();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		Logger.logAutonInit();
		locationCollector.cancel();

		Elevator.holdHeight();
		Chassis.ReleaseAngle();
		
		determineAuton();

		//Hardcoding here
		//autonomousCommand = new SwitchSide(fieldInfo.charAt(0));
		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
        mDisabledLooper.stop();
        mEnabledLooper.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		Logger.logTeleopInit();
		locationCollector.cancel();
		
		Elevator.holdHeight();

		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
        mDisabledLooper.stop();
        mEnabledLooper.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	private void determineAuton(){
		String gameData;
    	gameData = DriverStation.getInstance().getGameSpecificMessage();
    	StringBuilder st = new StringBuilder(gameData);
    	st.deleteCharAt(2);
    	String fieldInfo = st.toString();    	
    	//find robot starting pose
    	

    	String start = startPos.getSelected();
    	
		switch(chooser.getSelected()){
		case "Test MP":
			autonomousCommand = new RunMotionProfiles();
			break;
		case "Pass Baseline":
			autonomousCommand = new PassBaseline();
			break;
		case "Side":
			if(start == "Left"){
				if(fieldInfo == "LR"){
					System.out.println("Switch Side Left");
					autonomousCommand = new SwitchSide('L');
				}
				else if(fieldInfo == "LL"){
					System.out.println("Switch Side Left");
					autonomousCommand = new SwitchSide('L');
				}
				else if(fieldInfo == "RL"){
					System.out.println("Scale Left");
					autonomousCommand = new ScaleOnly('L');
				}
				else{
					autonomousCommand = new PassBaseline();
					System.out.println("Cancelling Switch Side");
				}
			}else{
				if(fieldInfo == "RL"){
					System.out.println("Switch Side Right");
					autonomousCommand = new SwitchSide('R');
				}
				else if(fieldInfo == "RR"){
					System.out.println("Switch Side Right");
					autonomousCommand = new SwitchSide('R');
				}
				else if(fieldInfo == "LR"){
					System.out.println("Scale Right");
					autonomousCommand = new ScaleOnly('R');
				}
				else{
					autonomousCommand = new PassBaseline();
					System.out.println("Cancelling Switch Side");
				}
			}
			break;
		case "Switch Front":
			autonomousCommand = new SwitchFront(fieldInfo.charAt(0));
			break;
		case "No Auto":
			autonomousCommand = null;
			break;
		default:
			autonomousCommand = null;
		}
	}
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
