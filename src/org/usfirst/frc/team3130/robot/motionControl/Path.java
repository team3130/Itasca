package org.usfirst.frc.team3130.robot.motionControl;

import java.util.ArrayList;

/**
 * Creates a monotonic cubic spline path from inputed waypoints
 * Represents the physical positional path the robot will take
 * 
 * @author Ashley
 */
public class Path {

	private ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
	private ArrayList<Segment> path = new ArrayList<Segment>();
	private double numberSegments = 1000.0;
	private double length;
	private SplineInterpolator spline;
	private boolean isFlipped = false;
	
	public Path(Waypoint w1, Waypoint w2){
		waypoints.add(w1);
		waypoints.add(w2);
	}
	
	public Path(Waypoint w1, Waypoint w2, Waypoint w3){
		waypoints.add(w1);
		waypoints.add(w2);
		waypoints.add(w3);
	}
	
	public Path(Waypoint w1, Waypoint w2, Waypoint w3, Waypoint w4){
		waypoints.add(w1);
		waypoints.add(w2);
		waypoints.add(w3);
		waypoints.add(w4);
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
	
	/**
	 * Generates a monotonic cubic spline through the supplied waypoints
	 */
	public void generateSpline(){
		ArrayList<Double> waypointsX = new ArrayList<Double>();
		ArrayList<Double> waypointsY = new ArrayList<Double>();
		ArrayList<Double> waypointsH = new ArrayList<Double>();
		
		for(int i = 0; i < waypoints.size(); i++){
			waypointsX.add(waypoints.get(i).getX());
			waypointsY.add(waypoints.get(i).getY());
			waypointsH.add(waypoints.get(i).getH());
		}
		
		SplineInterpolator generatedSpline = null;
		generatedSpline = SplineInterpolator.createMonotoneCubicSpline(waypointsX, waypointsY);
		double[] slopes = spline.getSlopes();
		for(int i = 0; i < waypoints.size(); i++) {
			if (Math.abs(slopes[i] - (- Math.tan(waypointsH.get(i)))) > 0.1 && waypointsH.get(i) < (2 * Math.PI)){
				slopes[i] = - Math.tan(waypointsH.get(i));
			}
		}
		
		SplineInterpolator splineWithHeadings = new SplineInterpolator(waypointsX, waypointsY, slopes);
		spline = splineWithHeadings;
	}
	
	/**
	 * Creates an ArrayList of segments to create a path according to the generated spline
	 */
	public void generatePath() {
		generateSpline();
		calculateLength();
		double xStep = (waypoints.get(waypoints.size() - 1).getX() - waypoints.get(0).getX()) / numberSegments;
		double currentX = waypoints.get(0).getX();
		for(int i = 0; i < numberSegments; i++){
			double x = currentX;
			double y = spline.interpolate(x);
			double slope;
			if(i == 0){
				double xNext = currentX + xStep;
				double yNext = spline.interpolate(xNext);
				slope = (yNext - y) / (xNext - x);
			}
			else if (i == numberSegments - 1){
				double xPrev = path.get(i - 1).getX();
				double yPrev = path.get(i - 1).getY();
				slope = (y - yPrev) / (x - xPrev);
			}
			else{
				double xNext = currentX + xStep;
				double yNext = spline.interpolate(xNext);
				double xPrev = path.get(i - 1).getX();
				double yPrev = path.get(i - 1).getY();
				double slope1 = (y - yPrev) / (x - xPrev);
				double slope2 = (yNext - y) / (xNext - x);
				slope = (slope1 + slope2) / 2;
			}
			Segment s = new Segment(x, y, slope);
			path.add(s);
			currentX += xStep;
		}
	}
}
