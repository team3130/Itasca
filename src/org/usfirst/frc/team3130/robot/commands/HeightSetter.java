package org.usfirst.frc.team3130.robot.commands;



public class HeightSetter {
	
	public static int acc = 0;
	
	private static HeightSetter m_pInstance;
	public static HeightSetter GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new HeightSetter();
		return m_pInstance;
	}
	
	public void increaseHeight() {
		if (acc <= 5){
			acc += 1;
			if(acc == 0){
				HoldElevator.holdHeight = 0.0;
			}
			if(acc == 1){
				HoldElevator.holdHeight = 0.0;
			}
		}
		
	}
	public void decreaseHeight(){
		if(acc >= 0){
			acc = acc - 1;
			if(acc == 0){
				HoldElevator.holdHeight = 0.0;
			}
			if(acc == 1){
				HoldElevator.holdHeight = 0.0;
			}
		}
	}
	
	
}