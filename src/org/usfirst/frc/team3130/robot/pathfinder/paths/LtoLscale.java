package org.usfirst.frc.team3130.robot.pathfinder.paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LtoLscale {
	
	public static Waypoint[]path = new Waypoint[] {
			new Waypoint(0.0, 0.0, Pathfinder.d2r(0.0)),
			new Waypoint(6.0, 0.0, Pathfinder.d2r(-3.0)),
            new Waypoint(7.0, -0.5, Pathfinder.d2r(-45))
	};
}
