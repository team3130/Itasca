package org.usfirst.frc.team3130.robot.sensors;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 */
public class LocationCamera {

	public static String cameraName = "StartCam";
	private static LocationCamera m_pInstance;
	public static LocationCamera GetInstance()
	{
		synchronized (m_pInstance) {
			if(m_pInstance == null) m_pInstance = new LocationCamera();
		}
		return m_pInstance;
	}

	public UsbCamera camera;
	public boolean isEnabled = false;
	public Point3 location = new Point3();

	public LocationCamera()
	{
		new Thread(() -> 
		{
			camera = CameraServer.getInstance().startAutomaticCapture(cameraName, 0);
			camera.setResolution(1280, 720);
			camera.setBrightness(0);
			camera.setWhiteBalanceManual(0);
            
			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource outputStream = CameraServer.getInstance().putVideo(cameraName, 1280, 720);

			Mat frame = new Mat();
                        
			while(!Thread.interrupted()) 
			{
				if(!isEnabled) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					continue;
				}
				if(cvSink.grabFrame(frame) == 0) {
					DriverStation.reportError(cvSink.getError(), false);
					continue;
				}
				Point3 safeLocation = findPosition(frame);
				synchronized(location)
				{
					location = safeLocation;
				}
            	
				outputStream.putFrame(frame);
				
			}
		}).start();
	}

	void enable() { isEnabled = true; }
	void disable() { isEnabled = false; }
	public static Point3 getPosition() {
		LocationCamera instance = GetInstance();
		Point3 safeLocation;
		synchronized (instance.location) {
			safeLocation = instance.location;
		}
		return safeLocation;
	}

	public Point3 findPosition(Mat frame) {
		// TODO Implement finding robot's position
		Imgproc.circle(frame, new Point(150,150), 100, new Scalar(0,255,0));
		return new Point3(0,0,0);
	}

}
	
	



