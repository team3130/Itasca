package org.usfirst.frc.team3130.robot.autoCommands;

import org.usfirst.frc.team3130.robot.commands.ElevatorToHeight;
import org.usfirst.frc.team3130.robot.commands.IntakeToggle;
import org.usfirst.frc.team3130.robot.commands.RunIntakeIn;
import org.usfirst.frc.team3130.robot.commands.RunIntakeOut;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.CubeIntake;
import org.usfirst.frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * The scale is opposite from start, and the switch is opposite of the scale
 */
public class ScaleSwitchOpp extends CommandGroup {

	private SwitchSide					switcH;
	private AutoDriveStraightToPoint	driveDown;
	private AutoDriveStraightToPoint	driveBehind;
	private AutoDriveStraightToPoint	toCube;
	private AutoDriveStraightToPoint	toCube2;
	private AutoDriveStraightToPoint	back;
	private AutoDriveStraightToPoint	back2;
	private AutoDriveStraightToPoint	back3;
	private AutoDriveStraightToPoint	toScale;
	private AutoDriveStraightToPoint	toScale2;
	private AutoTurn					turnAway;
	private AutoTurn					turnBehind;
	private AutoTurn					turnToCube;
	private AutoTurn					turnToCube2;
	private AutoTurn					turnToScale;
	private AutoTurn					turnToScale2;
	private ElevatorToHeight			eleDown;
	private ElevatorToHeight			eleDown2;
	private ElevatorToHeight			eleUp;
	private ElevatorToHeight			eleUp2;
	private RunIntakeIn					intake;
	private RunIntakeIn					intake2;
	private RunIntakeOut				intakeOut;
	private RunIntakeOut				intakeOut2;
	private IntakeToggle				openIntake;
	private IntakeToggle				openIntake2;
	private IntakeToggle				closeIntake;
	private IntakeToggle				closeIntake2;
	private char						scSide;
	private char						swSide;
	
    public ScaleSwitchOpp(char scale) {
        requires(Chassis.GetInstance());
        requires(Elevator.GetInstance());
        requires(CubeIntake.GetInstance());
        
        this.scSide		= scale;
        if(scSide == 'L'){
        	this.swSide = 'R';
        }
        else{
        	swSide = 'L';
        }
        switcH			= new SwitchSide(swSide);
        eleDown			= new ElevatorToHeight(0);
        turnAway		= new AutoTurn();
        driveDown		= new AutoDriveStraightToPoint();
        turnBehind		= new AutoTurn();
        driveBehind		= new AutoDriveStraightToPoint();
        turnToCube		= new AutoTurn();
        intake			= new RunIntakeIn();
        openIntake		= new IntakeToggle();
        toCube			= new AutoDriveStraightToPoint();
        closeIntake		= new IntakeToggle();
        back			= new AutoDriveStraightToPoint();
        eleUp			= new ElevatorToHeight(0);
        turnToScale		= new AutoTurn();
        toScale			= new AutoDriveStraightToPoint();
        intakeOut		= new RunIntakeOut();
        eleDown2		= new ElevatorToHeight(0);
        back2			= new AutoDriveStraightToPoint();
        turnToCube2		= new AutoTurn();
        intake2			= new RunIntakeIn();
        openIntake2		= new IntakeToggle();
        toCube2			= new AutoDriveStraightToPoint();
        closeIntake2	= new IntakeToggle();
        back3			= new AutoDriveStraightToPoint();
        eleUp2			= new ElevatorToHeight(0);
        turnToScale2	= new AutoTurn();
        toScale2		= new AutoDriveStraightToPoint();
        intakeOut2		= new RunIntakeOut();
        
        addSequential(switcH);
        addParallel(eleDown, 3);
        addSequential(turnAway, 2);
        addSequential(driveDown, 2);
        addSequential(turnBehind, 2);
        addSequential(driveBehind, 4);
        addSequential(turnToCube, 2);
        addParallel(intake, 4);
        addParallel(openIntake, 0.5);
        addSequential(toCube, 1.5);
        addSequential(closeIntake, 0.5);
        addSequential(back, 1.5);
        addParallel(eleUp, 3);
        addSequential(turnToScale, 1.5);
        addSequential(toScale, 2);
        addParallel(intakeOut, 0.5);
        addParallel(eleDown2, 3);
        addSequential(back2, 2);
        addSequential(turnToCube2, 1.5);
        addParallel(openIntake, 0.5);
        addParallel(intake2, 4);
        addSequential(toCube2, 3);
    }
    
    @Override
    protected void initialize(){
    	eleDown.setParam(0);
    	eleDown2.setParam(0);
    	eleUp.setParam(98);
    	intake.SetParam(0.8);
    	intake2.SetParam(0.8);
    	intakeOut.SetParam(-0.6);
    	eleUp.setParam(98);
    	driveDown.SetParam(
				96, 
				6, 
				0.85, 
				false
		);
    	driveBehind.SetParam(
				342, 
				6, 
				.8, 
				false
		);
    	toCube.SetParam(
				126, 
				3,
				0.9, 
				false
		);
    	back.SetParam(
				-72, 
				4, 
				0.7, 
				false
		);
    	toScale.SetParam(
				24, 
				6, 
				0.7, 
				false
		);
    	back2.SetParam(
				-72, 
				4, 
				0.7, 
				false
		);
    	toCube2.SetParam(
				126, 
				3,
				0.7, 
				false
		);
    	if(swSide == 'L'){
    		turnAway.setParam(-90, 1);
    		turnBehind.setParam(90, 1);
    		turnToCube.setParam(140, 3);
    		turnToCube2.setParam(-110, 3);
    		turnToScale.setParam(95, 2);
    	}
    	else{
    		turnAway.setParam(90, 1);
    		turnBehind.setParam(-90, 1);
    		turnToCube.setParam(-140, 3);
    		turnToCube2.setParam(110, 3);
    		turnToScale.setParam(-95, 2);
    	}
    }
}
