package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.motionControl.MotionProfileExample;
import org.usfirst.frc.team3130.robot.motionControl.TrapezoidalTrajectory;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;

public class RunMotionProfiles extends Command {

	private WPI_TalonSRX talonL;
	private WPI_TalonSRX talonR;
	private TrapezoidalTrajectory trajL;
	private TrapezoidalTrajectory trajR;
	private MotionProfileExample runMotion;
	
	//a test run; real ones will need parameters
	public RunMotionProfiles(){
		talonL = Chassis.getFrontL();
		talonR = Chassis.getFrontR();
		TrapezoidalTrajectory traj = new TrapezoidalTrajectory(12, 0, 0, 
				Constants.kMaxVelocity, Constants.kMaxAcceleration, 10);
		traj.generate(Constants.kLWheelDiameter);
		trajL = traj;
		trajR = traj;
	}
	
    public RunMotionProfiles(WPI_TalonSRX talonL, TrapezoidalTrajectory trajL, 
    						 WPI_TalonSRX talonR, TrapezoidalTrajectory trajR) {
    	requires(Chassis.GetInstance());
    	this.talonL = talonL;
    	this.talonR = talonR;
    	this.trajL = trajL;
    	this.trajR = trajR;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	talonL.configMotionProfileTrajectoryPeriod(10, 20);
    	talonR.configMotionProfileTrajectoryPeriod(10, 20);
    	runMotion = new MotionProfileExample(talonL, trajL, talonR, trajR);
    	SetValueMotionProfile setOutput = runMotion.getSetValue();
		talonL.set(ControlMode.MotionProfile, setOutput.value);
		talonR.set(ControlMode.MotionProfile, setOutput.value);
    	runMotion.startMotionProfile();
		//System.out.println("About to run motion profiles...");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	runMotion.control();
    	SetValueMotionProfile setOutput = runMotion.getSetValue();
		talonL.set(ControlMode.MotionProfile, setOutput.value);
		talonR.set(ControlMode.MotionProfile, setOutput.value);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	talonL.set(ControlMode.PercentOutput, 0);
    	talonR.set(ControlMode.PercentOutput, 0);
		/* clear our buffer and put everything into a known state */
		runMotion.reset();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
