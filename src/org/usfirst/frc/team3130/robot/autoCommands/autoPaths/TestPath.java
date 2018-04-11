package org.usfirst.frc.team3130.robot.autoCommands.autoPaths;

import org.usfirst.frc.team3130.robot.commands.Shift;
import org.usfirst.frc.team3130.robot.pathfinder.RunPath;
import org.usfirst.frc.team3130.robot.pathfinder.paths.LtoLscale;
import org.usfirst.frc.team3130.robot.pathfinder.paths.PathTest;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TestPath extends CommandGroup {

	//private RunPath testCmd;
	private Shift shiftDown;
    public TestPath() {
    	shiftDown = new Shift();
    	Command testCmd = new RunPath(PathTest.path);
    	
    	//addSequential(shiftDown);
        addSequential(testCmd);
    }
}
