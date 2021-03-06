package org.usfirst.frc.team3130.robot.commands;

import org.usfirst.frc.team3130.robot.subsystems.Chassis;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Shift extends Command {

	private Timer timer;
	private boolean m_bShifted;
	private boolean currentShift;
	
    public Shift() {
    	requires(Chassis.GetInstance());

        timer = new Timer();
        m_bShifted = false;
        currentShift = Chassis.GetShiftedUp(); //true = high gear
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	DriverStation.reportWarning("Shift.java command started", false);
    	timer.reset();
    	timer.start();
    	Chassis.TalonsToCoast(true);
    	Chassis.DriveTank(0, 0); 		//Cut all power to the motors so they aren't running during the shift
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	currentShift = Chassis.GetShiftedUp();
    	//Execute the shift only once, and only at a certain delay after the motors have been stopped
    	if(!m_bShifted && timer.get() > Preferences.getInstance().getDouble("Shift Dead Time Start", 0.125)){
    		Chassis.ShiftDown(currentShift);
    		m_bShifted = true;
    		//Reset the timer so that the ending dead time is from shifting rather than from the start.
    		timer.reset();
    		timer.start();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //End the command a configurable length of time after the robot has shifted gears
        return (m_bShifted && timer.get()> Preferences.getInstance().getDouble("Shift Dead Time End", 0.125));
    }

    // Called once after isFinished returns true
    protected void end() {
    	//Set variables to default states for next execution of command
    	m_bShifted = false;
    	timer.stop();
    	Chassis.TalonsToCoast(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
