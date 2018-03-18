package org.usfirst.frc.team3130.robot.pathfinder.paths;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LtoRscale {
	
	public static Waypoint[]toScale = new Waypoint[] {
			new Waypoint(0.98, 7.05, Pathfinder.d2r(0.0)),
            new Waypoint(4.5, 7.05, Pathfinder.d2r(0.0)),
            new Waypoint(6.0, 6.0, Pathfinder.d2r(-90.0)),
            new Waypoint(6.0, 3.1, Pathfinder.d2r(-90.0)),
            new Waypoint(7.58, 2.1, Pathfinder.d2r(0.0))
	};
}
