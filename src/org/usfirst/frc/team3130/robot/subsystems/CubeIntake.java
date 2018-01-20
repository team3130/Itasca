package org.usfirst.frc.team3130.robot.subsystems;

import org.usfirst.frc.team3130.robot.RobotMap;

import com.ctre.CANTalon;

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
	public static BasicTalonSRX btCubeIntakeLeft;
	public static BasicTalonSRX btCubeIntakeRight;
	public static BasicCylinder bcCubeActuate;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    //constructor
    private CubeIntake()
    {
    	btCubeIntakeLeft = new BasicTalonSRX(6, "Cube Intake", "Left");
		btCubeIntakeRight = new BasicTalonSRX(7, "Cube Intake", "Right");
		bcCubeActuate = new BasicCylinder(RobotMap.PNM_CUBEACTUATE, "Cube Intake", "Cube Actuate");
    }
}

