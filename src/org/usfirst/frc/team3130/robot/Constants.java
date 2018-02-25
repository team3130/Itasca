package org.usfirst.frc.team3130.robot;

import org.usfirst.frc.team3130.robot.util.ConstantsBase;

import edu.wpi.first.wpilibj.Preferences;

public class Constants extends ConstantsBase {
	//Vision tracking centerpoint height on high efficiency boiler
    public static double kCenterOfTargetHeight = 86.0; //Correct 9/11/17, in inches
    
    //Chassis constants
    public static double kDriveCodesPerRev = 2048.0;
    public static double kChassisWidth = 25.125; //Distance between the left and right middle wheels
    public static double kChassisLength = 32.5; //Length of robot frame in inches
    public static double kLWheelDiameter = 6.0; //Center wheel
    public static double kRWheelDiameter = 6.0;	//Center wheel
    public static double kMaxVelocity = 20.0; //in/s  in high gear
    public static double kMaxAcceleration = 20.0; //in/s^2 TODO:find
    public static double kChassisLowGearF = 0.0; //TODO: find
    public static double kChassisHighGearF = 0.152;
    
    //Elevator constants
    public static double kElevatorSpeed = Preferences.getInstance().getDouble("ElevatorSpeed", 0.6);
    public static double kElevatorTicksPerInch = (4.0 * 1024.0) / (4.0 * Math.PI);
    public static double kElevatorBias = Preferences.getInstance().getDouble("ElevatorBias", -0.2);
    public static double kElevatorZone = Preferences.getInstance().getDouble("ElevatorZone", 16);
    //TODO: Get actual values 
    public static int kElevatorSoftMax = 55; //in encoder ticks 
    public static int kElevatorSoftMin = 55; //in encoder ticks
    
    //Elevator PID
    public static double kElevatorP = Preferences.getInstance().getDouble("ElevatorP",0.2);
    public static double kElevatorI = 0.0;
    public static double kElevatorD = 0.0;
    public static double kElevatorF = 0.0;
    
    //Rangefinder constants
    public static double kCubeInRange = 80.0;//TODO: Get actual range
    public static int kBlinkNumber = 3;

    //Field Measurements
    public static double kWallToSwitch = 143.0; //inches forward to switch from alliance wall
    public static double kSwitchWidth  = 154.5; //average of red and blue side switch widths
    public static double kSwitchSlotWidth = 41.0; //inches wide of switch slot
    
    //Turret PID
    // Units: error is 4096 counts/rev. Max output is +/- 1023 units.
    public static double kTurretKp = Preferences.getInstance().getDouble("TurretAdjP",0.9);
    public static double kTurretKi = Preferences.getInstance().getDouble("TurretAdjI",0.0);
    public static double kTurretKd = Preferences.getInstance().getDouble("TurretAdjD",80.0);
    public static double kTurretKf = 0;
    public static int kTurretIZone = (int) (1023.0 / kTurretKp);
    public static double kTurretRampRate = 0;
    public static int kTurretAllowableError = 100;
    
    //USB camera offsets (in relation to robot center)
    public static double kUSBCamXOffset= 1.0;
    public static double kUSBCamYOffset= 1.0;
    public static double kUSBCamZOffset= 1.0;
    public static double kUSBCamYaw = 0.0; //in degrees
    public static double kUSBCamPitch = 0.0; //in degrees
    
	//Camera in relation to turret.
    public static double kCameraXOffset = -6.454;
    public static double kCameraYOffset = 0.0;
    public static double kCameraZOffset = 22.75;
    public static double kCameraPitchAngleDegrees = 15.0; 
    public static double kCameraYawAngleDegrees = 1.3;
    public static double kCameraDeadband = 0.0;
    
    // Goal tracker constants
    public static double kMaxGoalTrackAge = 0.2;
    public static double kMaxTrackerDistance = 18.0;
    public static double kCameraFrameRate = 30.0;
    public static double kTrackReportComparatorStablityWeight = 1.0;
    public static double kTrackReportComparatorAgeWeight = 1.0;
    public static double kTrackReportComparatorSwitchingWeight = 3.0;
    public static double kTrackReportComparatorDistanceWeight = 2.0; // Unused

   
    public static int kAndroidAppTcpPort = 43130;
    
    public static double kLooperDt = 0.01; 

    public String getFileLocation() {
        return "~/constants.txt";
    }
}