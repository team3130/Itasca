package org.usfirst.frc.team3130.robot.motionControl;

public class Segment {

	 private double dt, x, y, h, pos, vel, acc;

     public Segment(double dt, double x, double y, double h, double pos, double vel, double acc) {
         this.dt = dt;
         this.x = x;
         this.y = y;
         this.h = h;
         this.pos = pos;
         this.vel = vel;
         this.acc = acc;
     }
	
     public Segment(double x, double y, double h) {
         this.dt = 0.0;
         this.x = x;
         this.y = y;
         this.h = h;
         this.pos = 0.0;
         this.vel = 0.0;
         this.acc = 0.0;
     }
     
     public Segment() {
         this.dt = 0.0;
         this.x = 0.0;
         this.y = 0.0;
         this.h = 0.0;
         this.pos = 0.0;
         this.vel = 0.0;
         this.acc = 0.0;
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
     
     public double getDt(){
    	 return dt;
     }
     
     public void setDt(double dt){
    	 this.dt = dt; 
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
     
     public double getAcc(){
    	 return acc;
     }
     
     public void setAcc(double acc){
    	 this.acc = acc; 
     }
}
