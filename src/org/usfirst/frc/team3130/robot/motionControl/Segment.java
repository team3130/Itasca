package org.usfirst.frc.team3130.robot.motionControl;

public class Segment {

	 private double dt, x, y, h, pos, vel, acc, jerk;

     private Segment(double dt, double x, double y, double h, double pos, double vel, double acc, double jerk) {
         this.dt = dt;
         this.x = x;
         this.y = y;
         this.h = h;
         this.pos = pos;
         this.vel = vel;
         this.acc = acc;
         this.jerk = jerk;
     }
	
}
