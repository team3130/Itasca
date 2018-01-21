package org.usfirst.frc.team3130.robot;

import org.usfirst.frc.team3130.robot.util.ConstantsBase;

import edu.wpi.first.wpilibj.Preferences;

public class Constants extends ConstantsBase {
	//Vision tracking centerpoint height on high efficiency boiler
    public static double kCenterOfTargetHeight = 86.0; //Correct 9/11/17, in inches
    
    //Chassis constants
    public static double kChassisWidth = 26.0; //Distance between the left and right middle wheels
    public static double kLWheelDiameter = 6.0; //Center wheel
    public static double kRWheelDiameter = 6.0;	//Center wheel
    
    // Turret mechanical constants
    public static double kHardMaxTurretAngle = 97.0;
    public static double kHardMinTurretAngle = -98.0;
    public static double kSoftMaxTurretAngle = 95.0;
    public static double kSoftMinTurretAngle = -95.0;
    public static double kTurretOnTargetTolerance = 0.5;
    public static double kTurretRotationsPerTick =  1.0 / 4096.0 * 24.0 / 164.0; //Correct 9/29/17, CTRE Mag encoder connected to gearbox output shaft

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