package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.Constants;
import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.HeightSetter;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.continuousDrive.ContDrive;
import org.usfirst.frc.team3130.robot.commands.HeightSetter.Direction;
import org.usfirst.frc.team3130.robot.commands.IntakeToggle;
import org.usfirst.frc.team3130.robot.commands.RunElevator;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;
import org.usfirst.frc.team3130.robot.subsystems.Chassis.TurnDirection;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Colin Drive system to pass the baseline
 */
public class PassBaseline extends CommandGroup {
	
	private ContDrive				  driveForward;
	private RunIntakeIn               runIntake;
	private IntakeToggle			  closeIntake;
	private ElevatorToHeight          elevatorUp;

    public PassBaseline() {
       requires(Chassis.GetInstance());
       requires(CubeIntake.GetInstance());
       requires(Elevator.GetInstance());
       
       driveForward = new ContDrive();
       runIntake    = new RunIntakeIn();
       closeIntake  = new IntakeToggle();
       elevatorUp   = new ElevatorToHeight(4.0);
       
       addParallel(runIntake, 1);
       addParallel(closeIntake, 0.5);
       addSequential(elevatorUp, 0.5);
       addSequential(driveForward, 3);
    }
    
    @Override
	protected void initialize() {
        System.out.println("Running PB");
        Chassis.setTurnDir(TurnDirection.kStraight);
    	driveForward.SetParam(
    		Preferences.getInstance().getDouble("AUTON Forward Speed", 0.5),
    		Constants.kWallToSwitch
    		);
    	runIntake.SetParam(Preferences.getInstance().getDouble("AUTON Intake Speed", 0.3));
    }
	
}
