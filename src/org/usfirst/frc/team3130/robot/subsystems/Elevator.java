package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.commands.RunElevator;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Elevator extends Subsystem {
	
	private static WPI_TalonSRX elevator;
	
	//Instance Handling
	private static Elevator m_pInstance;
	public static Elevator GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new Elevator();
		return m_pInstance;
	}
	
	private Elevator() {
		elevator = new WPI_TalonSRX(RobotMap.CAN_ELEVATOR);
	}

    public void initDefaultCommand() {
    	setDefaultCommand(new RunElevator());
    }
    
    public static void runElevator(double percent) {
    	elevator.set(percent);
    }
}

