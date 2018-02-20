/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3130.robot;

import org.usfirst.frc.team3130.robot.autoCommands.RunMotionProfiles;
import org.usfirst.frc.team3130.robot.commands.RobotSensors;
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

import edu.wpi.first.wpilibj.Compressor;
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
	SendableChooser<String> chooser = new SendableChooser<>();
	RobotSensors robotSensors;
	VisionServer mVisionServer = VisionServer.getInstance();
	
	// Enabled looper is called at 10Hz whenever the robot is enabled, frequency can be changed in Constants.java: kLooperDt
    Looper mEnabledLooper = new Looper();
    // Disabled looper is called at 10Hz whenever the robot is disabled
    Looper mDisabledLooper = new Looper();
    
    public static BasicCylinder bcWingsDeploy;
	public static Compressor compressor = new Compressor(1);

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
		compressor.start();
		
		//bcWingsDeploy = new BasicCylinder(RobotMap.PNM_WINGSDEPLOY, "Wings", "Wings Deploy");
		
		OI.GetInstance();
		Chassis.GetInstance();
		CubeIntake.GetInstance();
		Elevator.GetInstance();
		Climber.GetInstance();
		BlinkinInterface.GetInstance();
		HookDeploy.GetInstance();
		
		//Vision operation
		//LocationCamera.enable();
		AndroidInterface.GetInstance();
		AndroidInterface.GetInstance().reset();
		VisionServer.getInstance();
		VisionServer.getInstance().requestAppStart();
		mVisionServer.addVisionUpdateReceiver(VisionProcessor.getInstance());
		
		mEnabledLooper.register(VisionProcessor.getInstance());
		
		chooser.addDefault("No Auton", "No Auto");
		chooser.addObject("Test MP", "Run MP");
		SmartDashboard.putData("Auto mode", chooser);
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
		mEnabledLooper.stop();
        mDisabledLooper.start();
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

		switch(chooser.getSelected()){
		case "Run MP":
			autonomousCommand = new RunMotionProfiles();
			break;
		case "No Auto":
			autonomousCommand = null;
			break;
		default:
			autonomousCommand = null;
		}
			
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
