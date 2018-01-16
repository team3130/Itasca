package org.usfirst.frc.team3130.robot.motionControl;

import java.util.ArrayList;

/**
 * 
 * @author Ashley
 */
public class Trajectory {
	
	private Path path;
	private double maxVel;
	private double maxAcc;
	private double maxJerk;
	private double startVel;
	private double endVel;
	
	public Trajectory(Path p) {
		path = p;
		startVel = 0.0;
		endVel = 0.0;
	}
	
	public Trajectory(Path p, double startV, double endV) {
		path = p;
		startVel = startV;
		endVel = endV;
	}
	
	public Trajectory getTrajectory() {
		return this;
	}
	
	public double getHConstant(double changeInH){
		return 1.0 - (0.5 * (changeInH / (Math.PI / 4)));
	}
	
	public void generateTrajectory() {
		ArrayList<Segment> segments = path.getPath();
		segments.get(0).setVel(startVel);
		segments.get(0).setDt(0);
		segments.get(0).setAcc(maxAcc * getHConstant(segments.get(0).getH()));
		for(int i = 1; i < segments.size(); i++){
			double changeInHeading = segments.get(i).getH() - segments.get(i - 1).getH();
			double x = segments.get(i).getX() - segments.get(i - 1).getX();
			double y = segments.get(i).getY() - segments.get(i - 1).getY();
			double dist = Math.hypot(x, y);
			double maxA = (segments.get(i - 1).getAcc() + maxJerk) * getHConstant(changeInHeading);
			maxA = Math.min(maxA, maxAcc);
			double a = segments.get(i - 1).getVel();
			double b = 0.5 * maxA;
			double c = - dist;
			double dt = (-b + Math.sqrt((b * b) - (4 * a * c))) / (2 * a);
			double maxV1 = segments.get(i - 1).getVel() + (maxA * dt);
			double maxV2 = maxVel * getHConstant(changeInHeading);
			double maxV = Math.min(maxV1, maxV2);
			segments.get(i).setDt(dt);
			segments.get(i).setVel(maxV);
			segments.get(i).setAcc(maxA);
		}
	}
}
