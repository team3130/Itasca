package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.IntakeToggle;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * 2 cube scale for when scale is opposite from start
 */
public class Scale2CubeOpp extends CommandGroup {

	private ScaleOnly					scale;
	private AutoDriveStraightToPoint	back;
	private AutoDriveStraightToPoint	back2;
	private AutoDriveStraightToPoint	back3;
	private AutoDriveStraightToPoint	back4;
	private AutoDriveStraightToPoint	toScale;
	private AutoDriveStraightToPoint	toScale2;
	private AutoDriveStraightToPoint	toCube;
	private AutoDriveStraightToPoint	toCube2;
	private AutoTurn					turnToScale;
	private AutoTurn					turnToScale2;
	private AutoTurn					turnToCube;
	private AutoTurn					turnToCube2;
	private ElevatorToHeight			eleUp;
	private ElevatorToHeight			eleUp2;
	private ElevatorToHeight			eleDown;
	private ElevatorToHeight			eleDown2;
	private RunIntakeIn					intakeCube;
	private RunIntakeIn					intakeCube2;
	private RunIntakeOut				intakeOut;
	private RunIntakeOut				intakeOut2;
	private IntakeToggle				openIntake;
	private IntakeToggle				closeIntake;
	private IntakeToggle				openIntake2;
	private IntakeToggle				closeIntake2;
	private AutoDelay					delay;
	private AutoDelay					delay2;
	private char						side;
	
    public Scale2CubeOpp(char side) {
    	requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.side    = side;
		scale		 = new ScaleOnly(side);
		eleDown 	 = new ElevatorToHeight(3);
		back	     = new AutoDriveStraightToPoint();
		intakeCube   = new RunIntakeIn();
		openIntake	 = new IntakeToggle();
		turnToCube   = new AutoTurn();
		toCube		 = new AutoDriveStraightToPoint();
		delay		 = new AutoDelay();
		closeIntake	 = new IntakeToggle();
		back2	     = new AutoDriveStraightToPoint();
		turnToScale  = new AutoTurn();
		eleUp        = new ElevatorToHeight(0);
		toScale      = new AutoDriveStraightToPoint();
		delay2		 = new AutoDelay();
		intakeOut    = new RunIntakeOut();
		back3		 = new AutoDriveStraightToPoint();
		intakeCube2  = new RunIntakeIn();
		turnToCube2  = new AutoTurn();
		openIntake2	 = new IntakeToggle();
		toCube2		 = new AutoDriveStraightToPoint();
		closeIntake2 = new IntakeToggle();
		back4	     = new AutoDriveStraightToPoint();
		turnToScale2 = new AutoTurn();
		eleUp2       = new ElevatorToHeight(0);
		eleDown2 	 = new ElevatorToHeight(3);
		toScale2      = new AutoDriveStraightToPoint();
		intakeOut2   = new RunIntakeOut();
		
    	addSequential(scale);
		addParallel(eleDown, 3);
		addSequential(back, 1.5);
		addParallel(intakeCube, 6);
		addSequential(turnToCube, 1.5);
		addParallel(openIntake, 0.5);
		addSequential(toCube, 3);
		addParallel(closeIntake, 0.5);
		addSequential(back2, 1.5);
		addParallel(eleUp, 3);
		addSequential(turnToScale, 1);
		addSequential(toScale, 1.5);
		addParallel(intakeOut, 0.5);
		addSequential(delay, 0.5);
		addParallel(eleDown2, 3);
		addSequential(back3,2);
		addParallel(intakeCube2, 4);
		addSequential(turnToCube2, 2.5);
		addParallel(openIntake2, 0.5);
		addSequential(toCube2, 2.0);
		addParallel(closeIntake2, 0.5);
		addSequential(delay2, 0.25);
		addSequential(back4, 2);
		addSequential(turnToScale2, 1);
		addParallel(eleUp2, 3);
		addSequential(toScale2, 1.5);
		addParallel(intakeOut2, 1);
    }
    
    @Override
    protected void initialize(){
    	intakeOut.SetParam(-0.4);
		intakeCube.SetParam(0.8);
		intakeCube2.SetParam(0.8);
		eleDown.setParam(0.0);
		eleUp.setParam(98.0);
		
		if(side == 'L'){ //Left Side Scale
			back.SetParam(
					-24, 
					4, 
					0.7, 
					false
			);
			back2.SetParam(
					-72, 
					4, 
					0.7, 
					false
			);
			toScale.SetParam(
					20, 
					4, 
					0.7, 
					false
			);
			toCube.SetParam(
					117, 
					3,
					0.9, 
					false
			);
			back3.SetParam(
					-24, 
					4, 
					0.55, 
					false
			);
			toCube2.SetParam(
					124, 
					3,
					0.65, 
					false
			);
			turnToScale.setParam(-130, 2);
			turnToCube.setParam(115, 2);
			turnToCube2.setParam(110, 2);
			
			
		}else{	//Right Side Scale
			back.SetParam(
					-24, 
					4, 
					0.7, 
					false
			);
			back2.SetParam(
					-72, 
					4, 
					0.7, 
					false
			);
			toScale.SetParam(
					60, 
					4, 
					0.7, 
					false
			);
			toCube.SetParam(
					117, 
					3,
					0.9, 
					false
			);
			back3.SetParam(
					-24, 
					4, 
					0.55, 
					false
			);
			toCube2.SetParam(
					124, 
					3,
					0.7, 
					false
			);
			turnToScale.setParam(130, 2);
			turnToCube.setParam(-115, 2);
			turnToCube2.setParam(-110, 2);
		}
    }
}
