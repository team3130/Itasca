package org.usfirst.frc.team3130.robot.pathfinder.paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RtoRScale {
	
	public static Waypoint[]toScale = new Waypoint[] {
			new Waypoint(0.0, 0.0, Pathfinder.d2r(0.0)),
			new Waypoint(7.11, 0.0, Pathfinder.d2r(0.0)),
            new Waypoint(7.65, -0.69, Pathfinder.d2r(45))
	};
}
