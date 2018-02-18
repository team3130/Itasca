package org.usfirst.frc.team3130.robot.motionControl;


public class Segment {

	 private double t, x, y, h, vel, acc, pos, rvel;

     public Segment(double t, double x, double y, double h, double pos, double vel, double acc) {
         this.t = t;
         this.x = x;
         this.y = y;
         this.h = h;
         this.pos = pos;
         this.vel = vel;
         this.acc = acc;
     }
	
     public Segment(double x, double y, double h) {
         this.t = 0.0;
         this.x = x;
         this.y = y;
         this.h = h;
         this.pos = 0.0;
         this.vel = 0.0;
         this.acc = 0.0;
         this.rvel = 0.0;
     }
     
     public Segment(double x){
    	 this.t = 0.0;
    	 this.x = x;
    	 this.y = 0.0;
    	 this.h = 0.0;
    	 this.pos = 0.0;
    	 this.vel = 0.0;
    	 this.acc = 0.0;
         this.rvel = 0.0;
     }
     
     public Segment() {
         this.t = 0.0;
         this.x = 0.0;
         this.y = 0.0;
         this.h = 0.0;
         this.pos = 0.0;
         this.vel = 0.0;
         this.acc = 0.0;
         this.rvel = 0.0;
     }
     
     @Override
     public String toString(){
    	 return "x: " + x + ", y: " + y + ", h: " + h + ", vel: " + vel + ", acc: " + acc;
     }
     
     public double getX(){
    	 return x;
     }
     
     public void setX(double x){
    	 this.x = x; 
     }
     
     public double getY(){
    	 return y;
     }
     
     public void setY(double y){
    	 this.y = y; 
     }
     
     public double getH(){
    	 return h;
     }
     
     public void setH(double h){
    	 this.h = h; 
     }
     
     public double getT(){
    	 return t;
     }
     
     public void setT(double t){
    	 this.t = t; 
     }
     
     public double getPos(){
    	 return pos;
     }
     
     public void setPos(double pos){
    	 this.pos = pos; 
     }
     
     public double getVel(){
    	 return vel;
     }
     
     public void setVel(double vel){
    	 this.vel = vel; 
     }
     
     public double getrVel(){
    	 return rvel;
     }
     
     public void setrVel(double r){
    	 this.rvel = r;
     }
     
     public double getAcc(){
    	 return acc;
     }
     
     public void setAcc(double acc){
    	 this.acc = acc; 
     }
}
