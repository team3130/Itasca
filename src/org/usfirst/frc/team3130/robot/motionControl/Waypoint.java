package org.usfirst.frc.team3130.robot.motionControl;

/**
 * Represents a point in space with an x-coordinate, y-coordinate, and
 * exit heading (angle the bot should exit the point with)
 * 
 * @author Ashley
 */
public class Waypoint {

	//x-coordinate, y-coordinate, and heading
    double x, y, h;
	
	private Waypoint(){
		this.x = 0.0;
		this.y = 0.0;
		this.h = 360.0;
	}
	
	private Waypoint(double x, double y){
		this.x = x;
		this.y = y;
		this.h = 360.0;
	}
	
	private Waypoint(double x, double y, double h){
		this.x = x;
		this.y = y;
		this.h = h;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getH() {
		return h;
	}
	
	/**
	 * Gives the Waypoint new values
	 * 
	 * @param x  x-coordinate in inches
	 * @param y  y-coordinate in inches
	 * @param h  heading in radians
	 */
	public void set(double x, double y, double h){
		this.x = x;
		this.y = y;
		this.h = h;
	}
	
	@Override
	public String toString(){
		return "X: " + this.x + ", Y: " + this.y + ", H: " + this.h;
	}
}
