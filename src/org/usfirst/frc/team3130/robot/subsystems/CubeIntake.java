package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CubeIntake extends Subsystem {

	//Instance Handling
		private static CubeIntake m_pInstance;
		public static CubeIntake GetInstance()
		{
			if(m_pInstance == null) m_pInstance = new CubeIntake();
			return m_pInstance;
		}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	//creation of required objects
	public static WPI_TalonSRX intakeLeft;
	public static WPI_TalonSRX intakeRight;
	public static Solenoid actuate;
	public static boolean open;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    //constructor
    private CubeIntake()
    {
    	intakeLeft = new WPI_TalonSRX(RobotMap.CAN_INTAKELEFT);
		intakeRight = new WPI_TalonSRX(RobotMap.CAN_INTAKERIGHT);
		actuate = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_CUBEACTUATE);
		open = false;
    }
    
    public static void runIntake(double left, double right){
    	intakeLeft.set(left);
    	intakeRight.set(right);
    	System.out.println("Running cube intake...left: " + left + ", right: " + right);
    }
    
    public static void toggleIntake(){
    	open = !open;
    	actuate.set(open);
    }
}

