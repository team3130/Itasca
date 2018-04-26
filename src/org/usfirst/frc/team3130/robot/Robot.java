/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3130.robot;

import org.usfirst.frc.team3130.robot.autoCommands.PassBaseline;
import org.usfirst.frc.team3130.robot.autoCommands.Scale2CubeOpp;
import org.usfirst.frc.team3130.robot.autoCommands.Scale2CubeSame;
import org.usfirst.frc.team3130.robot.autoCommands.ScaleAndBackUp;
import org.usfirst.frc.team3130.robot.autoCommands.ScaleAndSwitch;
import org.usfirst.frc.team3130.robot.autoCommands.ScaleOnly;
import org.usfirst.frc.team3130.robot.autoCommands.ScaleSwitchOpp;
import org.usfirst.frc.team3130.robot.autoCommands.ScaleSwitchOppSame;
import org.usfirst.frc.team3130.robot.autoCommands.ScaleSwitchSame;
import org.usfirst.frc.team3130.robot.autoCommands.ScaleSwitchSameOpp;
import org.usfirst.frc.team3130.robot.autoCommands.SwitchFront;
import org.usfirst.frc.team3130.robot.autoCommands.SwitchFront2Cube;
import org.usfirst.frc.team3130.robot.autoCommands.SwitchFront3Cube;
import org.usfirst.frc.team3130.robot.autoCommands.SwitchSide;
import org.usfirst.frc.team3130.robot.commands.RobotSensors;
import org.usfirst.frc.team3130.robot.subsystems.BasicCylinder;
import org.usfirst.frc.team3130.robot.subsystems.BlinkinInterface;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.Climber;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;
import org.usfirst.frc.team3130.robot.subsystems.HookDeploy;

import edu.wpi.first.wpilibj.CameraServer;
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
	//VisionServer mVisionServer = VisionServer.getInstance();
	
	// Enabled looper is called at 10Hz whenever the robot is enabled, frequency can be changed in Constants.java: kLooperDt
    //Looper mEnabledLooper = new Looper();
    // Disabled looper is called at 10Hz whenever the robot is disabled
    //Looper mDisabledLooper = new Looper();
    
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
		//compressor.start();
		
		bcWingsDeploy = new BasicCylinder(RobotMap.PNM_WINGSDEPLOY, "Wings", "Wings Deploy");
		
		OI.GetInstance();
		Chassis.GetInstance();
		CubeIntake.GetInstance();
		Elevator.GetInstance();
		Climber.GetInstance();
		BlinkinInterface.GetInstance();
		HookDeploy.GetInstance();
		CameraServer.getInstance().startAutomaticCapture();
		
		//Vision operation
//		AndroidInterface.GetInstance();
//		AndroidInterface.GetInstance().reset();
//		VisionServer.getInstance();
//		VisionServer.getInstance().requestAppStart();
//		mVisionServer.addVisionUpdateReceiver(VisionProcessor.getInstance());
		
//		mEnabledLooper.register(VisionProcessor.getInstance());
		
		//Auton command to run chooser
		chooser.addObject("Pass Baseline", "Pass Baseline");
		chooser.addObject("Side", "Side");
		chooser.addObject("Switch Front", "Switch Front");
		chooser.addObject("Scale", "Scale");
		chooser.addObject("Switch x2", "Switch x2");
		chooser.addObject("Switch x3", "Switch x3");
		chooser.addObject("Scale Switch", "Scale Switch");
		chooser.addObject("Scale x2", "Scale x2");
		chooser.addObject("Scale Back", "Scale Back");
		chooser.addDefault("No Auton", null);
		SmartDashboard.putData("Auto mode", chooser);
		

		//Starting configuration for autons
		//If hardcoding required, manually choose fieldSide below
		startPos.addDefault("Left Start Pos", "Left");
		startPos.addObject("Right Start Pos", "Right");
		SmartDashboard.putData("Start Pos", startPos);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		Elevator.resetElevator();
		CubeIntake.reset();
		HookDeploy.reset();
		//Chassis.ResetEncoders();
		Chassis.ReleaseAngle();
		Climber.resetClimbDir();
		bcWingsDeploy.actuate(false);
		//mEnabledLooper.stop();
        //mDisabledLooper.start();
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
		Elevator.resetElevator();
		//Elevator.holdHeight();
		Chassis.ReleaseAngle();
		
		determineAuton();

		//Hardcoding here
		//autonomousCommand = new SwitchSide(fieldInfo.charAt(0));
		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
       // mDisabledLooper.stop();
       // mEnabledLooper.start();
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
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		Elevator.resetElevator();
		Climber.resetClimbDir();
		Elevator.holdHeight();
        //mDisabledLooper.stop();
        //mEnabledLooper.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	private void determineAuton(){
		autonomousCommand = null;
		String gameData;
    	gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
    	String fieldInfo = gameData.substring(0, 2);    	
    	//find robot starting pose
    	//System.out.print(fieldInfo);

    	String start = startPos.getSelected();
    	if(start == null) {
    		DriverStation.reportError("startPos selector returned NULL", false);
    		return;
    	}
    	String chosenOne = chooser.getSelected();
    	if(chosenOne == null) {
    		DriverStation.reportError("Auton chooser returned NULL", false);
    		return;
    	}

    	String c1 = "LR";
    	String c2 = "LL";
    	String c3 = "RL";
    	String c4 = "RR";
    	
    	char switcH = fieldInfo.charAt(0);
    	char scale  = fieldInfo.charAt(1);

    	switch(chosenOne){
		case "Pass Baseline":
			autonomousCommand = new PassBaseline();
			break;
		case "Side":
			if(start.equals("Left")){
				if(fieldInfo.equals(c1)){
					//System.out.println("Switch Side Left");
					autonomousCommand = new SwitchSide('L');
				}
				else if(fieldInfo.equals(c2)){
					//System.out.println("Switch Side Left");
					autonomousCommand = new SwitchSide('L');
				}
				else if(fieldInfo.equals(c3)){
					//System.out.println("Scale Left");
					autonomousCommand = new ScaleOnly('L');
				}
				else{
					//System.out.println("Scale Right");
					autonomousCommand = new ScaleOnly('R');
				}
			}else{
				if(fieldInfo.equals(c3)){
					//System.out.println("Switch Side Right");
					autonomousCommand = new SwitchSide('R');
				}
				else if(fieldInfo.equals(c4)){
					//System.out.println("Switch Side Right");
					autonomousCommand = new SwitchSide('R');
				}
				else if(fieldInfo.equals(c1)){
					//System.out.println("Scale Right");
					autonomousCommand = new ScaleOnly('R');
				}
				else{
					//mSystem.out.println("Scale Left");
					autonomousCommand = new ScaleOnly('L');
				}
			}
			break;
		/*case "Switch Front":
			autonomousCommand = new SwitchFront(switcH);
			break;*/
		/* "Scale":
			autonomousCommand = new ScaleOnly(String.valueOf(gameData).charAt(1));
			break;*/
		case "Switch x2":
			autonomousCommand = new SwitchFront2Cube(switcH);
			break;
		case "Switch x3":
			autonomousCommand = new SwitchFront3Cube(switcH);
			break;
		case "Scale Switch":
			if(start.equals("Left") && scale == 'L' && switcH == 'L'){
				autonomousCommand = new ScaleSwitchSame(scale);
			}
			else if(start.equals("Left") && scale == 'L' && switcH == 'R'){
				autonomousCommand = new ScaleSwitchSameOpp('R', 'L');
			}
			else if(start.equals("Left") && scale == 'R' && switcH == 'R'){
				autonomousCommand = new ScaleSwitchOppSame(scale);
			}
			else if(start.equals("Left") && scale == 'R' && switcH == 'L'){
				autonomousCommand = new ScaleSwitchOpp(scale);
			}
			else if(start.equals("Right") && scale == 'R' && switcH == 'R'){
				autonomousCommand = new ScaleSwitchSame(scale);
			}
			else if(start.equals("Right") && scale == 'R' && switcH == 'L'){
				autonomousCommand = new ScaleSwitchSameOpp('L', 'R');
			}
			else if(start.equals("Right") && scale == 'L' && switcH == 'R'){
				autonomousCommand = new ScaleSwitchOpp(scale);
			}
			else if(start.equals("Right") && scale == 'L' && switcH == 'L'){
				autonomousCommand = new ScaleSwitchOppSame(scale);
			}
			//autonomousCommand = new ScaleAndSwitch(start, fieldInfo);
			break;
		case "Scale x2":
			boolean sameSide = (start.equals("Left") && fieldInfo.charAt(1) == 'L') ||
							   (start.equals("Right") && fieldInfo.charAt(1) == 'R');
			if(sameSide){
				autonomousCommand = new Scale2CubeSame(fieldInfo.charAt(1));
			}
			else {
				autonomousCommand = new Scale2CubeOpp(fieldInfo.charAt(1));
			}
			break;
		case "Scale Back":
			boolean sameSideSc = (start.equals("Left") && scale == 'L') ||
			   					 (start.equals("Right") && scale == 'R');
			boolean sameSideSw = (start.equals("Left") && switcH == 'L') ||
  					 			 (start.equals("Right") && switcH == 'R');
			if(sameSideSc){
				autonomousCommand = new ScaleAndBackUp(scale);
			}
			else if(sameSideSw){
				autonomousCommand = new ScaleAndBackUp(switcH);
			}
			else{
				autonomousCommand = new PassBaseline();
			}
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
