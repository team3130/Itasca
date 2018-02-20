package org.usfirst.frc.team3130.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.commands.LEDtest;

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
    
    public static void gotCube(){
    	for (int i = 0; i < Constants.kBlinkNumber; i++){
    		blinkin1.set(0.77);
	    	try{
	    		Thread.sleep(300);
	    	}catch(Exception e){}
	    	blinkin1.set(0.99);
	    	try{
	    		Thread.sleep(300);
	    	}catch(Exception e){}
    	}
    	defaultPattern();
    }
    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new LEDtest());
    }
}

