package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.commands.Climb;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {
	
	private static WPI_TalonSRX winchL1;
	private static WPI_TalonSRX winchL2;
	private static WPI_TalonSRX winchR1;
	private static WPI_TalonSRX winchR2;
	private static int climbDirection;
	
	//Instance Handling
	private static Climber m_pInstance;
	public static Climber GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new Climber();
		return m_pInstance;
	}
	
	private Climber(){
		winchL1 = new WPI_TalonSRX(RobotMap.CAN_CLIMBWINCHL1);
		winchL2 = new WPI_TalonSRX(RobotMap.CAN_CLIMBWINCHL2);
		winchR1 = new WPI_TalonSRX(RobotMap.CAN_CLIMBWINCHR1);
		winchR2 = new WPI_TalonSRX(RobotMap.CAN_CLIMBWINCHR2);
		winchL1.setNeutralMode(NeutralMode.Brake);
		winchL2.setNeutralMode(NeutralMode.Brake);
		winchR1.setNeutralMode(NeutralMode.Brake);
		winchR2.setNeutralMode(NeutralMode.Brake);
		winchL2.set(ControlMode.Follower, RobotMap.CAN_CLIMBWINCHL1);
    	winchR2.set(ControlMode.Follower, RobotMap.CAN_CLIMBWINCHR1);
    	climbDirection = 1;
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Climb());
    }
    
    public static void climb(double wL, double wR){
    	winchL1.set(wL);//climbDirection * w1);
    	winchR1.set(wR);//climbDirection * w2);
    	//System.out.println("Running L winch at: " + w1);
    	//System.out.println("Running R winch at: " + w2);
    }
    
    public static void reverseDir(){
    	climbDirection = -climbDirection;
    }
    
    public static int returnDir(){
    	return climbDirection;
    }
    
    public static void resetClimbDir(){
    	climbDirection = 1;
    }
}

