package org.usfirst.frc.team3130.robot.continuousDrive;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.RobotMap;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;

/**
 *
 */
public class ContTurnDist extends ContTurn{
	
	private double endAngle;
	
    public ContTurnDist() {
        super();
    }

    public ContTurnDist(ContinuousDrive previousCommand){
    	super(previousCommand);
	}
    
    protected double getPos()
    {
    	if(leftOutside) return Chassis.GetDistanceL();
    	return Chassis.GetDistanceR();
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	super.initialize();

    	//System.out.println("ContTurnDist");
    	endAngle += Chassis.GetAngle() * (Math.PI/180f);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	super.execute();
    }

    // Called once after isFinished returns true
    protected void end() {
    	super.end();
    }
    
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

	@Override
	public void SetParam(double percentVBus, double angle, boolean shiftDown) {
		super.SetParam(percentVBus, Constants.kChassisWidth * angle, shiftDown);
		valEnd = Math.abs(valEnd);
		endAngle = angle;
	}

	@Override
	public double getEndAngle() {
		return endAngle;
	}
}
