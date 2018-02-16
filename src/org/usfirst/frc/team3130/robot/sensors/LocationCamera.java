package org.usfirst.frc.team3130.robot.sensors;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
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

	// Camera intrinsics, obtained by "camera calibration" (see [google] tutorial_interactive_calibration.html)
	// These numbers are for the MS Lifecam HD3000 #1 at 1280x720 mode
	private double[] cameraDoubles = {1.1159304477424785e+03, 0., 6.2199798030884358e+02, 0.,
		    1.1159304477424785e+03, 3.8725727347891808e+02, 0., 0., 1.};
	private double[] distortionDoubles = {1.2109074822929057e-01, -1.0293273066620283e+00, 0., 0.,
		    1.8601846077358326e+00};
	private Mat cameraMatrix = new Mat(3, 3, CvType.CV_32F);
	private MatOfDouble distCoeffs = new MatOfDouble(distortionDoubles);


	private static LocationCamera m_pInstance;
	public static LocationCamera GetInstance()
	{
		synchronized (m_pInstance) {
			if(m_pInstance == null) m_pInstance = new LocationCamera();
		}
		return m_pInstance;
	}

	private UsbCamera camera;
	private boolean isEnabled = false;
	private Point3 location = new Point3();

	public LocationCamera()
	{
		// Initialize the camera intrinsics
		for(int i=0; i<3; i++)
			for(int j=0; j<3; j++)
				cameraMatrix.put(i, j, cameraDoubles[3*i+j]);

		// Run this thing as a thread so the heavy vision processing won't block the main (robot's) thread.
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

				// Location is a shared object between the threads so synchronize before accessing
				synchronized(location)
				{
					location = safeLocation;
				}
            	
				outputStream.putFrame(frame);
				
			}
		}).start();
	}

	public static void enable() { GetInstance().isEnabled = true; }
	public static void disable() { GetInstance().isEnabled = false; }

	public static Point3 getPosition() {
		LocationCamera instance = GetInstance();
		Point3 safeLocation;
		// Location is a shared object between the threads so synchronize before accessing
		synchronized (instance.location) {
			safeLocation = instance.location;
		}
		return safeLocation;
	}

	public Point3 findPosition(Mat frame) {
		double imageHeight = frame.height();
		double imageWidth = frame.width();

		MatOfPoint2f imagePoints = new MatOfPoint2f();

		Rect roiRect = new Rect(new Point(0, imageHeight/5), new Size(imageWidth, imageHeight/3));
		Imgproc.rectangle(frame, roiRect.tl(), roiRect.br(), new Scalar(0,255,0));
		roiRect = new Rect(new Point(0, 2*imageHeight/3), new Size(imageWidth, 4*imageHeight/5));
		Imgproc.rectangle(frame, roiRect.tl(), roiRect.br(), new Scalar(0,255,0));

		// TODO Implement finding robot's position
		return new Point3(0,0,0);
	}

}
	
	



