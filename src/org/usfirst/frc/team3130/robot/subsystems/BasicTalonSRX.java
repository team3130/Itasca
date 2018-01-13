package org.usfirst.frc.team3130.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Basic structure for a motor controlled by a TalonSRX
 */
public class BasicTalonSRX extends Subsystem {

	// Put methods for controlling this subsystem
    // here. Call these from Commands.

	private WPI_TalonSRX mc_spinnyMotor;
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    /**
     * @param CAN_id     ID of the TalonSRX
     * @param subsystem  Subsystem name Talon belongs to
     * @param item		 Name of the mechanism the Talon controls
     */
    public BasicTalonSRX(int CAN_id, String subsystem, String item)
    {
    	mc_spinnyMotor = new WPI_TalonSRX(CAN_id);
    }
    
    /**
     * Spins a motor
     * <p>Controls the speed of the motor, taking a value from -1 to 1 as a percentage of available power to pass on</p>
     * @param percentage the percentage of the voltage being fed to the controllers to pass on to the motors.
     */
    public void spinMotor(double percentage)
    {
    	mc_spinnyMotor.set(percentage);
    }
    
    /**
     * Returns the speed of the motor
     * 
     * <p>Returns the same value that is set by spinMotor, the percentage of voltage getting through the CANTalon</p>
     * @return the percentage of the voltage being fed to the controller that is passed to the motor.
     */
    public double getPercent()
    {
    	return mc_spinnyMotor.get();
    }
    
    /**
     * Returns the current of the motor
     * 
     *
     * @return the percentage of the current being fed to the controller that is passed to the motor.
     */
    public double getCurrent()
    {
    	return mc_spinnyMotor.getOutputCurrent();
    }
}

