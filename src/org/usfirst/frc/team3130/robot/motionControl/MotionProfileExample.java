package org.usfirst.frc.team3130.robot.motionControl;

/**-=-=-=-=-=-=-=FROM CTRE PHOENIX GITHUB=-=-=-=-=-=-=-=-
 * Example logic for firing and managing motion profiles.
 * This example sends MPs, waits for them to finish
 * Although this code uses a CANTalon, nowhere in this module do we changeMode() or call set() to change the output.
 * This is done in Robot.java to demonstrate how to change control modes on the fly.
 * 
 * The only routines we call on Talon are....
 * 
 * changeMotionControlFramePeriod
 * 
 * getMotionProfileStatus		
 * clearMotionProfileHasUnderrun     to get status and potentially clear the error flag.
 * 
 * pushMotionProfileTrajectory
 * clearMotionProfileTrajectories
 * processMotionProfileBuffer,   to push/clear, and process the trajectory points.
 * 
 * getControlMode, to check if we are in Motion Profile Control mode.
 * 
 * Example of advanced features not demonstrated here...
 * [1] Calling pushMotionProfileTrajectory() continuously while the Talon executes the motion profile, thereby keeping it going indefinitely.
 * [2] Instead of setting the sensor position to zero at the start of each MP, the program could offset the MP's position based on current position. 
 */

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;

import java.util.ArrayList;

import org.usfirst.frc.team3130.robot.Constants;

import com.ctre.phoenix.motion.*;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.*;

public class MotionProfileExample {

	/**
	 * The status of the motion profile executer and buffer inside the Talon.
	 * Instead of creating a new one every time we call getMotionProfileStatus,
	 * keep one copy.
	 */
	private MotionProfileStatus _status = new MotionProfileStatus();

	/** additional cache for holding the active trajectory point */
	double _pos=0,_vel=0;

	/**
	 * reference to the talon we plan on manipulating. We will not changeMode()
	 * or call set(), just get motion profile status and make decisions based on
	 * motion profile.
	 */
	private WPI_TalonSRX talonL;
	
	/**
	 * reference to the talon we plan on manipulating. We will not changeMode()
	 * or call set(), just get motion profile status and make decisions based on
	 * motion profile.
	 */
	private WPI_TalonSRX talonR;
	
	/**
	 * Holds the values for the motion profile for the left side
	 */
	private double[][] profileL;
	
	/**
	 * Holds the values for the motion profile for the right side
	 */
	private double[][] profileR;
	
	/**
	 * State machine to make sure we let enough of the motion profile stream to
	 * talon before we fire it.
	 */
	private int _state = 0;
	/**
	 * Any time you have a state machine that waits for external events, its a
	 * good idea to add a timeout. Set to -1 to disable. Set to nonzero to count
	 * down to '0' which will print an error message. Counting loops is not a
	 * very accurate method of tracking timeout, but this is just conservative
	 * timeout. Getting time-stamps would certainly work too, this is just
	 * simple (no need to worry about timer overflows).
	 */
	private int _loopTimeout = -1;
	/**
	 * If start() gets called, this flag is set and in the control() we will
	 * service it.
	 */
	private boolean _bStart = false;

	/**
	 * Since the CANTalon.set() routine is mode specific, deduce what we want
	 * the set value to be and let the calling module apply it whenever we
	 * decide to switch to MP mode.
	 */
	private SetValueMotionProfile _setValue = SetValueMotionProfile.Disable;
	/**
	 * How many trajectory points do we wait for before firing the motion
	 * profile.
	 */
	private static final int kMinPointsInTalon = 5;
	/**
	 * Just a state timeout to make sure we don't get stuck anywhere. Each loop
	 * is about 20ms.
	 */
	private static final int kNumLoopsTimeout = 10;
	
	/**
	 * Lets create a periodic task to funnel our trajectory points into our talon.
	 * It doesn't need to be very accurate, just needs to keep pace with the motion
	 * profiler executer.  Now if you're trajectory points are slow, there is no need
	 * to do this, just call _talon.processMotionProfileBuffer() in your teleop loop.
	 * Generally speaking you want to call it at least twice as fast as the duration
	 * of your trajectory points.  So if they are firing every 20ms, you should call 
	 * every 10ms.
	 */
	class PeriodicRunnable implements java.lang.Runnable {
	    public void run() {
	    	talonL.processMotionProfileBuffer();  
	    	talonR.processMotionProfileBuffer();  
	    }
	}
	Notifier _notifer = new Notifier(new PeriodicRunnable());
	

	/**
	 * C'tor
	 * 
	 * @param talon
	 *            reference to Talon object to fetch motion profile status from.
	 */
	public MotionProfileExample(WPI_TalonSRX talonL, TrapezoidalTrajectory trajL, WPI_TalonSRX talonR, TrapezoidalTrajectory trajR) {
		this.talonL = talonL;
		this.talonR = talonR;
		profileL = new double[trajL.getSeg().size()][3];
		profileR = new double[trajL.getSeg().size()][3];;
		for(int i = 0; i < profileL.length; i++){
			profileL[i][0] = trajL.getSeg().get(i).getPos();
			profileR[i][0] = trajR.getSeg().get(i).getPos();
			profileL[i][1] = trajL.getSeg().get(i).getrVel();
			profileR[i][1] = trajR.getSeg().get(i).getrVel();
			profileL[i][2] = trajL.getPointDuration();
			profileR[i][2] = trajR.getPointDuration();
		}
		/*
		 * since our MP is 10ms per point, set the control frame rate and the
		 * notifer to half that
		 */
		talonL.changeMotionControlFramePeriod(5);
		talonR.changeMotionControlFramePeriod(5);
		_notifer.startPeriodic(0.005);
	}

	/**
	 * Called to clear Motion profile buffer and reset state info during
	 * disabled and when Talon is not in MP control mode.
	 */
	public void reset() {
		/*
		 * Let's clear the buffer just in case user decided to disable in the
		 * middle of an MP, and now we have the second half of a profile just
		 * sitting in memory.
		 */
		talonL.clearMotionProfileTrajectories();
		talonR.clearMotionProfileTrajectories();
		
		/* When we do re-enter motionProfile control mode, stay disabled. */
		_setValue = SetValueMotionProfile.Disable;
		/* When we do start running our state machine start at the beginning. */
		_state = 0;
		_loopTimeout = -1;
		/*
		 * If application wanted to start an MP before, ignore and wait for next
		 * button press
		 */
		_bStart = false;
	}

	/**
	 * Called every loop.
	 */
	public void control() {
		/* Get the motion profile status every loop */
		talonL.getMotionProfileStatus(_status);
		talonR.getMotionProfileStatus(_status);

		/*
		 * track time, this is rudimentary but that's okay, we just want to make
		 * sure things never get stuck.
		 */
		if (_loopTimeout < 0) {
			/* do nothing, timeout is disabled */
		} else {
			/* our timeout is nonzero */
			if (_loopTimeout == 0) {
				/*
				 * something is wrong. Talon is not present, unplugged, breaker
				 * tripped
				 */
				Instrumentation.OnNoProgress();
			} else {
				--_loopTimeout;
			}
		}

		/* first check if we are in MP mode */
		if (talonL.getControlMode() != ControlMode.MotionProfile || 
			talonR.getControlMode() != ControlMode.MotionProfile) {
			/*
			 * we are not in MP mode. We are probably driving the robot around
			 * using gamepads or some other mode.
			 */
			_state = 0;
			_loopTimeout = -1;
			//System.out.println("Not in MP mode");
		} else {
			/*
			 * we are in MP control mode. That means: starting Mps, checking Mp
			 * progress, and possibly interrupting MPs if thats what you want to
			 * do.
			 */
			//System.out.println("In MP mode");
			switch (_state) {
				case 0: /* wait for application to tell us to start an MP */
					if (_bStart) {
						_bStart = false;
	
						_setValue = SetValueMotionProfile.Disable;
						startFilling();
						//System.out.println("Started filling, disabled");
						/*
						 * MP is being sent to CAN bus, wait a small amount of time
						 */
						_state = 1;
						_loopTimeout = kNumLoopsTimeout;
					}
					break;
				case 1: /*
						 * wait for MP to stream to Talon, really just the first few
						 * points
						 */
					//System.out.println("Ready to start, waiting for enough points...");
					/* do we have a minimum numberof points in Talon */
					if (_status.btmBufferCnt > kMinPointsInTalon) {
						/* start (once) the motion profile */
						_setValue = SetValueMotionProfile.Enable;
						//System.out.println("MP is enabled");
						/* MP will start once the control frame gets scheduled */
						_state = 2;
						_loopTimeout = kNumLoopsTimeout;
					}
					break;
				case 2: /* check the status of the MP */
					/*
					 * if talon is reporting things are good, keep adding to our
					 * timeout. Really this is so that you can unplug your talon in
					 * the middle of an MP and react to it.
					 */
					//System.out.println("MP is enabled, and simply monitoring");
					if (_status.isUnderrun == false) {
						_loopTimeout = kNumLoopsTimeout;
						//System.out.println("Not underrun");
					}
					/*
					 * If we are executing an MP and the MP finished, start loading
					 * another. We will go into hold state so robot servo's
					 * position.
					 */
					if (_status.activePointValid && _status.isLast) {
						/*
						 * because we set the last point's isLast to true, we will
						 * get here when the MP is done
						 */
						_setValue = SetValueMotionProfile.Hold;
						_state = 0;
						_loopTimeout = -1;
						//System.out.println("Last point, holding MP");
					break;
					}
			}

			/* Get the motion profile status every loop */
			//_talon.getMotionProfileStatus(_status);
			//_heading = _talon.getActiveTrajectoryHeading();
			
			talonL.set(ControlMode.MotionProfile, _setValue.value);
			talonR.set(ControlMode.MotionProfile, _setValue.value);
			
			_pos = talonL.getActiveTrajectoryPosition();
			_vel = talonL.getActiveTrajectoryVelocity();

			/* printfs and/or logging */
			Instrumentation.process(_status, _pos, _vel);
		}
	}
	/**
	 * Find enum value if supported.
	 * @param durationMs
	 * @return enum equivalent of durationMs
	 */
	private TrajectoryDuration GetTrajectoryDuration(int durationMs)
	{	 
		// create return value 
		TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;
		//convert duration to supported type
		retval = retval.valueOf(durationMs);
		//check that it is valid
		if (retval.value != durationMs) {
			DriverStation.reportError("Trajectory Duration not supported - use configMotionProfileTrajectoryPeriod instead", false);		
		}
		//pass to caller
		return retval;
	}
	
	/** Start filling the MPs to all of the involved Talons. */
	public void startFilling() {
		/* since this example only has one talon, just update that one */
		startFilling(profileL, profileR);
	}

	private void startFilling(double[][] profileL, double[][] profileR) {

		/* create an empty point */
		TrajectoryPoint pointL = new TrajectoryPoint();
		TrajectoryPoint pointR = new TrajectoryPoint();

		/* did we get an underrun condition since last time we checked ? */
		if (_status.hasUnderrun) {
			/* better log it so we know about it */
			Instrumentation.OnUnderrun();
			/*
			 * clear the error. This flag does not auto clear, this way 
			 * we never miss logging it.
			 */
			talonL.clearMotionProfileHasUnderrun(0);
			talonR.clearMotionProfileHasUnderrun(0);
		}
		/*
		 * just in case we are interrupting another MP and there is still buffer
		 * points in memory, clear it.
		 */
		talonL.clearMotionProfileTrajectories();
		talonR.clearMotionProfileTrajectories();
		/* set the base trajectory period to zero, use the individual trajectory period below */
		talonL.configMotionProfileTrajectoryPeriod(0, 20);
		talonR.configMotionProfileTrajectoryPeriod(0, 20);
		
		/* This is fast since it's just into our TOP buffer */
		for (int i = 0; i < profileL.length; ++i) {
			double LpositionRot = profileL[i][0];
			double RpositionRot = profileR[i][0];
			double LvelocityRPS = profileL[i][1];
			double RvelocityRPS = profileR[i][1];
			/* for each point, fill our structure and pass it to API */
			pointL.position = LpositionRot * Constants.kDriveCodesPerRev; //Convert Revolutions to Units
			pointL.velocity = LvelocityRPS * Constants.kDriveCodesPerRev / 10.0; //Convert RPS to Units/100ms
			pointR.position = RpositionRot * Constants.kDriveCodesPerRev; //Convert Revolutions to Units
			pointR.velocity = RvelocityRPS * Constants.kDriveCodesPerRev / 10.0; //Convert RPS to Units/100ms
			//point.headingDeg = 0; /* future feature - not used in this example*/
			//point.profileSlotSelect0 = 0; /* which set of gains would you like to use [0,3]? */
			//point.profileSlotSelect1 = 0; /* future feature  - not used in this example - cascaded PID [0,1], leave zero */
			pointL.timeDur = GetTrajectoryDuration((int)profileL[i][2]);
			pointR.timeDur = GetTrajectoryDuration((int)profileR[i][2]);
			pointL.zeroPos = false;
			pointR.zeroPos = false;
			if (i == 0){
				//set to true on first point
				pointL.zeroPos = true; 
				pointR.zeroPos = true; 
			}
			
			pointL.isLastPoint = false;
			pointR.isLastPoint = false;
			if ((i + 1) == profileL.length){
				//set to true on first point
				pointL.isLastPoint = true;
				pointR.isLastPoint = true;
			}

			talonL.pushMotionProfileTrajectory(pointL);
			talonR.pushMotionProfileTrajectory(pointR);
			//System.out.println("Pushing points...");
			
			System.out.println("LVel: " + pointL.velocity) ;
			System.out.println("LDur: " + (int)profileL[i][2] + "vs." + pointL.timeDur);
		}
	}
	/**
	 * Called by application to signal Talon to start the buffered MP (when it's
	 * able to).
	 */
	public void startMotionProfile() {
		_bStart = true;
		//System.out.println("Starting MP...");
	}

	/**
	 * 
	 * @return the output value to pass to Talon's set() routine. 0 for disable
	 *         motion-profile output, 1 for enable motion-profile, 2 for hold
	 *         current motion profile trajectory point.
	 */
	public SetValueMotionProfile getSetValue() {
		return _setValue;
	}
}
