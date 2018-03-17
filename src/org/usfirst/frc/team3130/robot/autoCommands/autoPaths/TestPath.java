package org.usfirst.frc.team3130.robot.autoCommands.autoPaths;

import org.usfirst.frc.team3130.robot.pathfinder.RunPath;
import org.usfirst.frc.team3130.robot.pathfinder.paths.PathTest;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TestPath extends CommandGroup {

	private RunPath testCmd;
    public TestPath() {
    	testCmd = new RunPath(PathTest.path);
    	
    	
        addSequential(testCmd);
    }
}
