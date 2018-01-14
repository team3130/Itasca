package org.usfirst.frc.team3130.robot.motionControl;

public class Segment {

	 private double dt, x, y, h, pos, vel, acc, jerk;

     public Segment(double dt, double x, double y, double h, double pos, double vel, double acc, double jerk) {
         this.dt = dt;
         this.x = x;
         this.y = y;
         this.h = h;
         this.pos = pos;
         this.vel = vel;
         this.acc = acc;
         this.jerk = jerk;
     }
	
     public Segment(double x, double y, double h) {
         this.dt = 0.0;
         this.x = x;
         this.y = y;
         this.h = h;
         this.pos = 0.0;
         this.vel = 0.0;
         this.acc = 0.0;
         this.jerk = 0.0;
     }
     
     public Segment() {
         this.dt = 0.0;
         this.x = 0.0;
         this.y = 0.0;
         this.h = 0.0;
         this.pos = 0.0;
         this.vel = 0.0;
         this.acc = 0.0;
         this.jerk = 0.0;
     }
     
     public double getX(){
    	 return x;
     }
     
     public double getY(){
    	 return y;
     }
     
     public double getH(){
    	 return h;
     }
     
     public double getDt(){
    	 return dt;
     }
     
     public double getPos(){
    	 return pos;
     }
     
     public double getVel(){
    	 return vel;
     }
     
     public double getAcc(){
    	 return acc;
     }
     
     public double getJerk(){
    	 return jerk;
     }
}
