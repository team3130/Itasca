package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class HeightSetter extends Command {

	public enum Direction { kUp, kDown };
	private Direction myDir; 
	private double[] heights = {0, 5.0, 20, 57.0, 65.0, 75.0};

	public HeightSetter(Direction dir) {
		myDir = dir;
		requires(Elevator.GetInstance());
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
		double height = Elevator.getHeight();
		for (double h: heights) {
			if (height + 3 < h) {
				Elevator.setHeight(h);
				return;
			}
		}
	}

	public void decreaseHeight(){
		double height = Elevator.getHeight();
		double previous = 0;
		for (double h: heights) {
			if (height > h) {
				Elevator.setHeight(previous);
				return;
			}
			previous = h;
		}
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}