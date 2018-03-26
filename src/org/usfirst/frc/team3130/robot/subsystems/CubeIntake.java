package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.commands.ContinuousIntake;

import com.ctre.phoenix.motorcontrol.NeutralMode;
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
	public static WPI_TalonSRX intakeL;
	public static WPI_TalonSRX intakeR;

	public static Solenoid actuateL;
	public static Solenoid actuateR;
	public static boolean open;
	public static boolean openL;
	public static boolean openR;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	//setDefaultCommand(new ContinuousIntake());
    }
    
    //constructor
    private CubeIntake()
    {
    	intakeL = new WPI_TalonSRX(RobotMap.CAN_INTAKE_L);
    	intakeR = new WPI_TalonSRX(RobotMap.CAN_INTAKE_R);

		intakeL.setNeutralMode(NeutralMode.Brake);
		intakeR.setNeutralMode(NeutralMode.Brake);
		
		actuateL = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_CUBEACTUATEL);
		actuateR = new Solenoid(RobotMap.CAN_PNMMODULE, RobotMap.PNM_CUBEACTUATER);
		open = false;
		openL = false;
		openR = false;
		
    }
    
    public static void runIntake(double speed){
    	intakeL.set(speed);
    	intakeR.set(speed);
    }
    
    public static void toggleIntake(){
    	open = !open;
    	actuateL.set(open);
    	actuateR.set(open);
    	openL = open;
    	openR = open;
    }
    
    public static void openIntake(){
    	open = true;
    	actuateL.set(open);
    	actuateR.set(open);
    }
    
    public static void closeIntake(){
    	open = false;
    	actuateL.set(open);
    	actuateR.set(open);
    }
    
    public static void toggleIntakeL(){
    	openL = !openL;
    	actuateL.set(openL);
    	if(openL==openR) open = openL;
    }
    
    public static void toggleIntakeR(){
    	openR = !openR;
    	actuateR.set(openR);
    	if(openL==openR) open = openR;
    }

    public static void reset(){
    	open = false;
    	openL = false;
		openR = false;
		actuateL.set(open);
		actuateR.set(open);
    }
}

