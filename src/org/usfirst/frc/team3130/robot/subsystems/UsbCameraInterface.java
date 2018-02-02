package org.usfirst.frc.team3130.robot.subsystems;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

/**
 *
 */
public class UsbCameraInterface {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private static UsbCameraInterface m_pInstance;
	public static UsbCameraInterface GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new UsbCameraInterface();
		return m_pInstance;
	}
	public static double startX = 0.0;
	public static double startY = 0.0;
	
	public UsbCameraInterface(){
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture("StartCam", 0);
		camera.setResolution(1280, 720);
	}
	public static void findPosition() {
		CvSink cvSink = CameraServer.getInstance().getVideo("StartCam");
		Mat source = new Mat();
		cvSink.grabFrame(source);
	}
	public static double returnXPosition(){
		return startX;
	}
}
	
	



