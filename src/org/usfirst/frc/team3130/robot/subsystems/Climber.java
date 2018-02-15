package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {
	
	private static WPI_TalonSRX winch1;
	private static WPI_TalonSRX winch2;
	
	//Instance Handling
	private static Climber m_pInstance;
	public static Climber GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new Climber();
		return m_pInstance;
	}
	
	private Climber(){
		winch1 = new WPI_TalonSRX(RobotMap.CAN_EVWINCH1);
		winch2 = new WPI_TalonSRX(RobotMap.CAN_EVWINCH2);
		winch1.setNeutralMode(NeutralMode.Brake);
		winch2.setNeutralMode(NeutralMode.Brake);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public static void climb(double w1, double w2){
    	winch1.set(w1);
    	winch2.set(w2);
    }
}

