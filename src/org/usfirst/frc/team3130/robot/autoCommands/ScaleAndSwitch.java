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
 * Deposits a cube into the scale then the switch
 */
public class ScaleAndSwitch extends CommandGroup {
	private AutoDriveStraightToPoint	driveForward;
	private AutoDriveStraightToPoint	driveBack;
	private AutoTurn					turnToScale;
	private ElevatorToHeight			elevatorUp;
	private ElevatorToHeight			elevatorUpAgain;
	private ElevatorToHeight			elevatorDown;
	private ElevatorToHeight			eleDownAgain;
	private ElevatorToHeight			eleUpAgain;
	private ElevatorToHeight			eleRaise;
	private RunIntakeIn					intakeIn;
	private RunIntakeOut				intakeOut;
	private RunIntakeOut				cubeSpit;
	private AutoTurn					turnToCube;
	private RunIntakeIn					intakeCube;
	private RunIntakeIn					intakeAgain;
	private AutoDriveStraightToPoint    driveToCube;
	private AutoDriveStraightToPoint	driveABit;
	private RunIntakeOut				depositCube;
	private AutoTurn					turnToSwitch;
	private AutoTurn					turnAway;
	private IntakeToggle				openIntake;
	private IntakeToggle				openIntakeAgain;
	private IntakeToggle				closeIntake;
	private IntakeToggle				closeIntakeAgain;
	private AutoDelay					delay;
	private AutoDelay					delay2;
	private AutoDelay					delay3;
	private AutoDriveStraightToPoint	toSwitch;
	private AutoDriveStraightToPoint	toSwitchLast;
	private AutoDriveStraightToPoint	back;
	private AutoDriveStraightToPoint	backLast;
	private AutoDriveStraightToPoint	straight;
	private AutoDriveStraightToPoint	straightAgain;
	private AutoDriveStraightToPoint	driveBehind;
	private AutoDriveStraightToPoint	driveToScale;
	private AutoTurn					turnSecond;
	private AutoTurn					turnLast;
	private AutoTurn					turnBehind;
	private String						start;
	private char						scale;
	private char						switcH;
	private boolean						sameScale;
	private boolean						sameSwitch;

	public ScaleAndSwitch(String start, String gameData) {
		requires(Chassis.GetInstance());
		requires(Elevator.GetInstance());
		requires(CubeIntake.GetInstance());
		
		this.start    = start;
		this.switcH	  = gameData.charAt(0);
		this.scale    = gameData.charAt(1);
		this.sameScale = (start.equals("Right") && scale == 'R') || (start.equals("Left") && scale == 'L');
		this.sameSwitch = (scale == 'R' && switcH == 'R') || (scale == 'L' && switcH == 'L');
		driveForward = new AutoDriveStraightToPoint();
		new AutoDriveStraightToPoint();
		driveBack    = new AutoDriveStraightToPoint();
		turnToScale  = new AutoTurn();
		elevatorUp   = new ElevatorToHeight(0);
		new ElevatorToHeight(8);
		intakeIn     = new RunIntakeIn();
		intakeAgain     = new RunIntakeIn();
		intakeOut    = new RunIntakeOut();
		cubeSpit     = new RunIntakeOut();
		elevatorDown = new ElevatorToHeight(3);
		eleDownAgain = new ElevatorToHeight(5);
		turnToCube   = new AutoTurn();
		driveABit    = new AutoDriveStraightToPoint();
		driveToCube  = new AutoDriveStraightToPoint();
		intakeCube   = new RunIntakeIn();
		elevatorUpAgain = new ElevatorToHeight(40);
		eleUpAgain = new ElevatorToHeight(40);
		eleRaise	 = new ElevatorToHeight(98);
		depositCube  = new RunIntakeOut();
		turnAway     = new AutoTurn();
		turnToSwitch = new AutoTurn();
		openIntake   = new IntakeToggle();
		closeIntake  = new IntakeToggle();
		openIntakeAgain   = new IntakeToggle();
		closeIntakeAgain  = new IntakeToggle();
		delay        = new AutoDelay();
		delay2       = new AutoDelay();
		delay3       = new AutoDelay();
		toSwitch     = new AutoDriveStraightToPoint();
		back         = new AutoDriveStraightToPoint();
		turnSecond	 = new AutoTurn();
		straight     = new AutoDriveStraightToPoint();
		turnLast	 = new AutoTurn();
		straightAgain= new AutoDriveStraightToPoint();
		backLast	 = new AutoDriveStraightToPoint();
		toSwitchLast	 = new AutoDriveStraightToPoint();
		driveToScale	 = new AutoDriveStraightToPoint();
		turnBehind   = new AutoTurn();
		driveBehind = new AutoDriveStraightToPoint();
		
		//beginning of auton
		addParallel(elevatorUp,3);
		addParallel(intakeIn,1);
		addSequential(driveForward,4.1);
		
		//scale is on same side as start
		if(sameScale){
			addSequential(turnToScale, 1.1);
			addSequential(intakeOut, 1);
			//switch is on the same side as the scale
			if(sameSwitch){
				addSequential(driveBack, 2);
				addParallel(elevatorDown, 3);
				addSequential(turnAway, 1.5);
				addSequential(driveABit, 1.5);
				addSequential(openIntake, 0.5);
				addSequential(turnToCube, 1.5);
				addParallel(intakeCube, 3);
				addSequential(driveToCube, 1.5);
				addSequential(closeIntake, 0.5);
				addParallel(elevatorUpAgain, 1.5);
				addSequential(delay3, 1.0);
				addSequential(toSwitch, 1);
				addSequential(depositCube, 1);
				addSequential(back, 1);
				addParallel(eleDownAgain, 2);
				addSequential(turnSecond, 1);
				addParallel(openIntakeAgain, 1);
				addParallel(intakeAgain, 2.5);
				addSequential(straight, 1.5);
				addSequential(turnLast, 1);
				addSequential(straightAgain, 0.5);
				addSequential(closeIntakeAgain, 0.3);
				addParallel(eleUpAgain, 2);
				addSequential(toSwitchLast, 1);
				addSequential(cubeSpit, 1);
			}
		}
		//scale is on opposite side of start
		else{
			addSequential(turnBehind, 2);
			addSequential(driveBehind,5);
			addParallel(eleRaise,3);
			addSequential(turnToScale,1);
			addSequential(driveToScale,1.5);
			addSequential(intakeOut,1);
			addParallel(elevatorDown, 3);
			addSequential(driveBack,2);
			//switch is on the same side as the scale
			/*
			if(sameSwitch){
				addParallel(intakeCube, 4);
				addParallel(openIntake, 0.5);
				addSequential(turnToCube, 2);
				addSequential()
			}
			//switch is opposite the scale
			else{
				
			}*/
		}
	}
    
	@Override
    protected void initialize(){
    	//System.out.println("INIT SCALE AND SWITCH ________________");
    	//Always same
		intakeIn.SetParam(0.3);
		intakeAgain.SetParam(0.8);
		intakeCube.SetParam(0.8);
		depositCube.SetParam(-0.6);
		cubeSpit.SetParam(-0.6);
		elevatorDown.setParam(3.0);
		eleDownAgain.setParam(5.0);
		elevatorUpAgain.setParam(40.0);
		eleUpAgain.setParam(40.0);
		eleRaise.setParam(98);
		if(sameScale){
			elevatorUp.setParam(98);
			intakeOut.SetParam(-1.0);
			driveForward.SetParam(
					Preferences.getInstance().getDouble("ScaleSwitch Forward Dist", 404), 
					20, 
					0.85, 
					false
			);
			if(scale =='L'){		//Left Side Scale, Switch, and Start
				turnToScale.setParam(45, 5);
				turnAway.setParam(80, 3);
				turnToCube.setParam(Preferences.getInstance().getDouble("ScaleSwitch turnToCube Left Side", 45), 2);
				turnToSwitch.setParam(20, 3);
				turnSecond.setParam(-45, 3);
				turnLast.setParam(55, 3);
			}else{				//Right Side Scale, Switch, and Start
				turnToScale.setParam(-55, 2);
				turnToCube.setParam(Preferences.getInstance().getDouble("ScaleSwitch turnToCube Right Side", -45), 2);
				turnToSwitch.setParam(-20, 3);
			}
		}
		else {
			elevatorUp.setParam(12);
			intakeOut.SetParam(-0.4);
			driveForward.SetParam(
					348, 
					5, 
					0.75, 
					false
			);
			driveBehind.SetParam(
					300, 
					5, 
					.75, 
					false
			);
			driveToScale.SetParam(
					24, 
					10, 
					0.6, 
					false
			);
			if(scale == 'L'){
				turnToScale.setParam(100, 1);
				turnBehind.setParam(-90, 1);
				turnToCube.setParam(-180, 3);
			}
			else{
				turnToScale.setParam(-100, 1);
				turnBehind.setParam(90, 1);
				turnToCube.setParam(180, 3);
			}
		}
		driveBack.SetParam(
				Preferences.getInstance().getDouble("ScaleSwitch Back Dist", -24), 
				5, 
				0.5, 
				false
		);
		driveToCube.SetParam(//106
				Preferences.getInstance().getDouble("ScaleSwitch ToCube Dist", 30),  
				5, 
				.85, 
				false
		);
		driveABit.SetParam(
				76, 
				14, 
				.7, 
				false
		);
		toSwitch.SetParam(
				Preferences.getInstance().getDouble("ScaleSwitch ToSwitch Dist", 16), 
				14, 
				.7, 
				false
		);
		toSwitchLast.SetParam(
				Preferences.getInstance().getDouble("ScaleSwitch ToSwitch Dist", 26), 
				14, 
				.7, 
				false
		);
		back.SetParam(
				-36, 
				8, 
				.7, 
				false
		);
		straight.SetParam(
				44, 
				4, 
				.7, 
				false
		);
		straightAgain.SetParam(
				18, 
				4, 
				.7, 
				false
		);
		backLast.SetParam(
				-48, 
				8, 
				.7, 
				false
		);
		//driveToScale.SetParam(12, 10, 0.4, false);
		
    }
}
