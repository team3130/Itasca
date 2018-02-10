package org.usfirst.frc.team3130.robot.motionControl;

import java.util.ArrayList;

/**
 * Generates a trapezoidal motion profile and trajectory for a linear motion
 * @author Ashley
 */
public class LinearTrapTraj {

	/**
	 * total distance the trajectory will travel (inches)
	 */
	private double distance;
	/**
	 * initial velocity (in/sec)
	 */
	private double v0;
	/**
	 * final velocity (in/sec)
	 */
	private double vf;
	/**
	 * maximum velocity of the system (in/sec)
	 */
	private double vmax;
	/**
	 * maximum acceleration of the system (in/sec^2)
	 */
	private double amax;
	/**
	 * time spent accelerating (seconds)
	 */
	private double ta;
	/**
	 * time spent at cruise velocity (seconds)
	 */
	private double tv;
	/**
	 * time spent decelerating (seconds)
	 */
	private double td;
	/**
	 * ArrayList of Segments to store info about trajectory
	 */
	private ArrayList<Segment> segments = new ArrayList<Segment>();
	/**
	 * how far apart segments will be in time (in seconds)
	 */
	private double tStep;
	
	/**
	 * 
	 * @param d      distance to travel in inches
	 * @param v0     initial velocity in in/s
	 * @param vf     final velocity in in/s
	 * @param vmax   top velocity of the system in in/s
	 * @param amax   top acceleration of the system in in/s^2
	 * @param tStep  step size to create segments in ms
	 */
	public LinearTrapTraj(double d, double v0, double vf, double vmax, double amax, int tStep){
		this.distance = d;
		this.v0 = v0;
		this.vf = vf;
		this.vmax = vmax;
		this.amax = amax;
		this.ta = 0;
		this.tv = 0;
		this.td = 0;
		this.tStep = tStep/1000.0;
	}
	
	public ArrayList<Segment> getSeg() {
		return segments;
	}
	
	public void findTimes(){
		if(v0 == vf){
			//try with systems' mechanical maximums
			double ta = (vmax - v0) / (amax);
			double xa = (v0 * ta) + ((amax/2) * (ta*ta));
			double totalDist = 2*xa;
			if(totalDist == distance){
				this.ta = ta;
				this.td = ta;
				tv = 0;
			}
			else if(totalDist < distance){
				//if total distance not traveled with acceleration and deceleration alone, add some time
				//at cruising velocity (vmax)
				double xrem = distance - totalDist;
				this.ta = ta;
				this.td = ta;
				tv = xrem / vmax;
			}
			else{
				//solve for new vmax
				double a = 1;
				double b = v0;
				double c = (-distance)*amax;
				vmax = (-b + Math.sqrt((b * b) - (4 * a * c))) / (2 * a);
				this.ta = vmax/amax;
				this.td = vmax/amax;
				tv = 0;
			}
		}
		else{
			//try with systems' mechanical maximums
			double ta = (vmax - v0) / (amax);
			double td = (vf - vmax) / -amax;
			double xa = (v0 * ta) + ((amax/2) * (ta*ta));
			double xd = (vmax * td) - ((amax/2) * (td*td));
			double totalDist = xa + xd;
			if(totalDist == distance){
				this.ta = ta;
				this.td = td;
				tv = 0;
			}
			else if(totalDist < distance){
				//if total distance not traveled with acceleration and deceleration alone, add some time
				//at cruising velocity (vmax)
				double xrem = distance - totalDist;
				this.ta = ta;
				this.td = td;
				tv = xrem / vmax;
			}
			else{
				//solve for new amax and vmax
				amax = (distance - v0*(ta + td)) / (((ta*ta)/2) + (ta*td) - ((td*td)/2));
				vmax = Math.sqrt(((2*amax*distance) + (3*v0*v0) + (vf*vf))/2);
				this.ta = (vmax - v0)/amax;
				this.td = (vmax - vf)/amax;
				tv = 0;
			}
		}
		/* debug
		System.out.println("Max velocity: " + vmax);
		System.out.println("Max acceleration: " + amax);
		System.out.println("Time accelerating: " + this.ta);
		System.out.println("Time decelerating: " + this.td);
		System.out.println("Time at cruise velocity: " + tv);
		System.out.println("Tstep: " + tStep);
		*/
	}

	public void generateSegments(){
		Segment first = new Segment();
		first.setAcc(amax);
		first.setVel(v0);
		first.setX(0);
		first.setT(0);
		segments.add(first);
		int i = 0;
		double time = 0;
		if(v0 == vf)	
			time = ta + tv + ta;
		else    
			time = ta + tv + td;
		for(double t = tStep; t < time; t+=tStep){
			segments.add(new Segment());
			i++;
			segments.get(i).setT(t);
			if(segments.get(i).getT() <= ta){
				segments.get(i).setAcc(amax);
				double vel = segments.get(i-1).getVel() + (amax*tStep);
				segments.get(i).setVel(vel);
				segments.get(i).setX(segments.get(i-1).getX() + (vel * tStep) + ((amax/2)*(tStep*tStep)));
				//System.out.println("(if 1) Segment #" + i + ": " + segments.get(i).toString());
			}
			else if(segments.get(i).getT() <= ta + tv && segments.get(i).getT() > ta){
				segments.get(i).setAcc(0);
				segments.get(i).setVel(vmax);
				segments.get(i).setX(segments.get(i-1).getX() + (vmax * tStep) + ((amax/2)*(tStep*tStep)));
				//System.out.println("(if 2) Segment #" + i + ": " + segments.get(i).toString());
			}
			else {
				segments.get(i).setAcc(-amax);
				double vel = segments.get(i-1).getVel() + (-amax*tStep);
				segments.get(i).setVel(vel);
				segments.get(i).setX(segments.get(i-1).getX() + (vel * tStep) + ((-amax/2)*(tStep*tStep)));
				//System.out.println("(if 3) Segment #" + i + ": " + segments.get(i).toString());
			}
		}	
	}
	
	public void generate(){
		findTimes();
		generateSegments();
	}
	
	public void calculateTime(){
		double totalTime = 0;
		for(int i = 0; i < segments.size()-1; i++){
			segments.get(i).setT(totalTime);
			double v0 = segments.get(i).getVel();
			double vf = segments.get(i+1).getVel();
			double deltaX = Math.abs(segments.get(i+1).getX() - segments.get(i).getX());
			double a = ((vf*vf) - (v0*v0)) / (4*deltaX);
			double t = 0;
			if(a != 0){
				double b = v0;
				double c = -deltaX;
				t = (-b + Math.sqrt((b * b) - (4 * a * c))) / (2 * a);
			}
			else{
				t = deltaX / v0;
			}
			totalTime += t;
		}
		segments.get(segments.size()-1).setT(totalTime);
	}
}