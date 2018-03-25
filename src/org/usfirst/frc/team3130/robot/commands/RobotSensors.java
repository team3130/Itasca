package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.sensors.Rangefinder;
import org.usfirst.frc.team3130.robot.subsystems.*;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

//similar to Hiawatha's RobotSensors.java
//feedback on motors' speeds, voltages, etc. displayed on the SMD (SmartDashboard)

/**
 *
 */
public class RobotSensors extends Command {

	private Timer timer = new Timer();
	
    public RobotSensors() {
    	this.setRunWhenDisabled(true);
    	this.setInterruptible(false);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.reset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// Time each execution cycle
    	SmartDashboard.putNumber("Scheduler cycle", timer.get());
    	timer.reset();
    	timer.start();
    	
    	//Elevator
    	Elevator.outputToSmartDashboard();

    	// Rangefinder readings are updated here. Do not disable this code. Do not call getDistance from anywhere else
    	Rangefinder rf = Rangefinder.GetInstance();
    	int distance = rf.getDistance();
    	rf.setStoredRange(distance);
   		SmartDashboard.putNumber("Lidar range", distance);
    	SmartDashboard.putBoolean("LIDAR Ready", rf.getDistanceReady());
    	SmartDashboard.putNumber("LIDAR status", rf.getDistanceStatus());
    	// Also let's see if LIDAR takes cpu cycles
    	SmartDashboard.putNumber("LIDAR Time", timer.get());
    	
    	SmartDashboard.putNumber("Velocity", Chassis.GetSpeed());

    	//SmartDashboard.putNumber("Left Index Current", Robot.btLeftIndex.getCurrent());
    	//SmartDashboard.putNumber("Right Index Current", Robot.btRightIndex.getCurrent());
    	
    	//Chassis
    	//SmartDashboard.putNumber("Front Left Wheel Speed", Chassis.GetSpeedL());
    	//SmartDashboard.putNumber("Front Right Wheel Speed", Chassis.GetSpeedR());
    	//SmartDashboard.putNumber("Front Left Voltage", Chassis.GetFrontVoltL());
    	//SmartDashboard.putNumber("Front Right Voltage", Chassis.GetFrontVoltR());
    	//SmartDashboard.putNumber("Rear Left Voltage", Chassis.GetRearVoltL());
    	//SmartDashboard.putNumber("Rear Right Voltage", Chassis.GetRearVoltR());
    	//SmartDashboard.putNumber("Front Left Current", Chassis.GetFrontCurrentL());
    	//SmartDashboard.putNumber("Front Right Current", Chassis.GetFrontCurrentR());
    	//SmartDashboard.putNumber("Rear Left Current", Chassis.GetRearCurrentL());
    	//SmartDashboard.putNumber("Rear Right Current", Chassis.GetRearCurrentR());
    	SmartDashboard.putBoolean("Shifted High", Chassis.GetShiftedUp());
    	SmartDashboard.putNumber("Angle", Chassis.GetAngle());
    	SmartDashboard.putNumber("Left Distance", Chassis.GetDistanceL());
    	SmartDashboard.putNumber("Right Distance", Chassis.GetDistanceR());
    	SmartDashboard.putNumber("Climber Direction", Climber.returnDir());
    	
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

