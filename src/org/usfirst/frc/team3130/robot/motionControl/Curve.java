package org.usfirst.frc.team3130.robot.motionControl;


import java.awt.List;
import java.util.ArrayList;

/**
 * Creates a monotonic cubic spline path from inputed waypoints
 * Represents the physical positional path the robot will take
 * 
 * @author Ashley
 */
public class Curve {

	private ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
	private ArrayList<Segment> path = new ArrayList<Segment>();
	private SplineInterpolator spline = null;
	private double numberSegments = 50.0;
	private boolean isFlipped = false;
	
	public Curve(Waypoint w1, Waypoint w2, Waypoint w3, boolean isF){
		waypoints.add(w1);
		waypoints.add(w2);
		waypoints.add(w3);
		isFlipped = isF;
		System.out.println("Waypoint 1 - " + w1.toString());
		System.out.println("Waypoint 2 - " + w2.toString());
		System.out.println("Waypoint 3 - " + w3.toString());
	}
	
	public Curve(Waypoint w1, Waypoint w2, Waypoint w3, Waypoint w4, boolean isF){
		waypoints.add(w1);
		waypoints.add(w2);
		waypoints.add(w3);
		waypoints.add(w4);
		System.out.println("Waypoint 1 - " + w1.toString());
		System.out.println("Waypoint 2 - " + w2.toString());
		System.out.println("Waypoint 3 - " + w3.toString());
		System.out.println("Waypoint 4 - " + w4.toString());
		isFlipped = isF;
	}
	
	public Curve(Waypoint w1, Waypoint w2, Waypoint w3, Waypoint w4, Waypoint w5, boolean isF){
		waypoints.add(w1);
		waypoints.add(w2);
		waypoints.add(w3);
		waypoints.add(w4);
		waypoints.add(w5);
		System.out.println("Waypoint 1 - " + w1.toString());
		System.out.println("Waypoint 2 - " + w2.toString());
		System.out.println("Waypoint 3 - " + w3.toString());
		System.out.println("Waypoint 4 - " + w4.toString());
		System.out.println("Waypoint 5 - " + w5.toString());
		isFlipped = isF;
	}
	
	public ArrayList<Segment> getPath() {
		return path;
	}
	
	public boolean isFlipped() {
		return isFlipped;
	}
	
	/**
	 * Calculates the length of the spline
	 */
	/*
	public void calculateLength() {
		double xDistance = waypoints.get(waypoints.size() - 1).getX() - waypoints.get(0).getX(); 
		double xStep = xDistance / numberSegments;
		double currentX = waypoints.get(0).getX();
		double length = 0; 
		for(int i = 0; i < numberSegments - 1; i++){
			double y1 = spline.interpolate(currentX);
			double y2 = spline.interpolate(currentX + xStep);
			double yLength = y2 - y1;
			double l = Math.hypot(xStep, yLength);
			length += l;
			currentX += xStep;
		}
		this.length = length;
	}
	*/

	/**
	 * Generates a monotonic cubic spline through the supplied waypoints
	 */
	public void generateSpline(){
		ArrayList<Double> waypointsX = new ArrayList<Double>();
		ArrayList<Double> waypointsY = new ArrayList<Double>();
		
		for(int i = 0; i < waypoints.size(); i++){
			waypointsX.add(waypoints.get(i).getX());
			waypointsY.add(waypoints.get(i).getY());
		}
		SplineInterpolator generatedSpline = null;
		generatedSpline = SplineInterpolator.createMonotoneCubicSpline(waypointsX, waypointsY);
		spline = generatedSpline;
	}
	
	/**
	 * Creates an ArrayList of segments to create a path according to the generated spline
	 */
	public void generatePath() {
		generateSpline();
		double xStep = (waypoints.get(waypoints.size() - 1).getX() - waypoints.get(0).getX()) / numberSegments;
		double currentX = waypoints.get(0).getX();
		for(int i = 0; i < numberSegments; i++){
			double x = currentX;
			double y = spline.interpolate(x);
			double h;
			double slope;
			if(i == 0){
				double xNext = currentX + xStep;
				double yNext = spline.interpolate(xNext);
				slope = (yNext - y) / (xNext - x);
				h = -Math.tan(slope);
			}
			else if (i == numberSegments - 1){
				double xPrev = path.get(i - 1).getX();
				double yPrev = path.get(i - 1).getY();
				slope = (y - yPrev) / (x - xPrev);
				h = -Math.tan(slope);
			}
			else {
				double xNext = currentX + xStep;
				double yNext = spline.interpolate(xNext);
				double xPrev = path.get(i - 1).getX();
				double yPrev = path.get(i - 1).getY();
				double slope1 = (y - yPrev) / (x - xPrev);
				double slope2 = (yNext - y) / (xNext - x);
				slope = (slope1 + slope2) / 2;
				h = -Math.tan(slope);
			}
			Segment s = new Segment(x, y, h);
			System.out.println("Segment #" + (i+1) + " :" + s.toString());
			path.add(s);
			currentX += xStep;
		}
		
	}
}

