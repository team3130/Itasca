package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class HeightSetter extends Command {

	public enum Direction { kUp, kDown };
	private Direction myDir; 
	public static int acc = 0;

	public HeightSetter(Direction dir) {
		myDir = dir;
	}

	@Override
	public void initialize() {
		switch (myDir) {
		case kUp:
			increaseHeight();
			break;
		case kDown:
			decreaseHeight();
			break;
		}
	}

	public void increaseHeight() {
		if (acc <= 4){
			acc += 1;
			setHeight();
		}
	}

	public void decreaseHeight(){
		if(acc >= 0){
			acc = acc - 1;
			setHeight();
		}
	}

	public void setHeight() {
		if(acc == 0){
			Elevator.setHeight(0.0);
		}
		if(acc == 1){
			Elevator.setHeight(10.0);
		}
		if(acc == 2){
			Elevator.setHeight(57.0);
		}
		if(acc == 3){
			Elevator.setHeight(65.0);
		}
		if(acc == 4){
			Elevator.setHeight(75.0);
		}
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}