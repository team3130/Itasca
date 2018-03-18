package org.usfirst.frc.team3130.robot.pathfinder.paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RtoLscale {
	
	public static Waypoint[]toScale = new Waypoint[] {
			new Waypoint(0.98, 1.18, Pathfinder.d2r(0.0)),
            new Waypoint(4.5, 1.18, Pathfinder.d2r(0.0)),
            new Waypoint(6.0, 2.4, Pathfinder.d2r(90.0)),
            new Waypoint(6.0, 5.15, Pathfinder.d2r(90.0)),
            new Waypoint(7.58, 6.15, Pathfinder.d2r(0.0))
	};
}
