package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.commands.RunHookWinch;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class HookDeploy extends Subsystem {

	private static WPI_TalonSRX winch;
	private static Solenoid actuator;
	private static boolean actuated;

	//Instance Handling
	private static HookDeploy m_pInstance;
	public static HookDeploy GetInstance()
		{
		if(m_pInstance == null) m_pInstance = new HookDeploy();
		return m_pInstance;
	}
		
	private HookDeploy(){
		winch = new WPI_TalonSRX(RobotMap.CAN_HDWINCH);
		winch.setNeutralMode(NeutralMode.Brake);
		//winch.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
		actuator = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_HOOKACTUATE);
		actuated = false;
	}
	
    public void initDefaultCommand() {
    	setDefaultCommand(new RunHookWinch());
    }
    
    public static void toggleActuation(){
    	actuated = !actuated;
    	actuator.set(actuated);
    }
    
    public static void runWinch(double speed){
    	winch.set(speed);
    }
    

    public static void reset(){
    	actuated = false;
    	actuator.set(actuated);
    }
}

