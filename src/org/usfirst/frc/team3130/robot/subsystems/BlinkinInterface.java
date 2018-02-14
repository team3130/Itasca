package org.usfirst.frc.team3130.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.*;

/**
 *
 */
public class BlinkinInterface extends Subsystem {
	private static Spark blinkin1;
	
	
    private static BlinkinInterface m_pInstance;
    public static BlinkinInterface GetInstance(){
    	if(m_pInstance == null) m_pInstance = new BlinkinInterface();
		return m_pInstance;
    }
    
    private BlinkinInterface(){
    	blinkin1 = new Spark(0);
    	blinkin1.set(0.67);
    }
    
    public static void defaultPattern(){
    	blinkin1.set(0.67);
    }
    public static void setPattern(double pattern){
    	blinkin1.set(pattern);
    }
    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

