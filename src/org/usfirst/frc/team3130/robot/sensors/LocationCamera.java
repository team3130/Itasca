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
	public enum Mode {
		kDisabled,
		kView,
		kLocation
	}

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
//		synchronized (m_pInstance) {
			if(m_pInstance == null) m_pInstance = new LocationCamera();
//		}
		return m_pInstance;
	}

	private UsbCamera camera;
	CvSink cvSink;
	CvSource outputStream;
	private Mode mode = Mode.kDisabled;
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
			camera = new UsbCamera(cameraName, 0);
			camera.setResolution(1280, 720);
//			camera.setResolution(640, 360);
//			camera.setResolution(960, 540);
			camera.setExposureManual(60);

			// Actually putVideo does startAutomaticCapture()
//			CameraServer.getInstance().startAutomaticCapture(camera);

			outputStream = CameraServer.getInstance().putVideo(cameraName, 240, 135);
			cvSink = CameraServer.getInstance().getVideo(cameraName);

                        
			while(!Thread.interrupted()) 
			{
				Mode safeMode;
				synchronized(mode) {
					safeMode = mode;
				}
				switch(safeMode) {
				case kLocation:
					doLocation();
					break;
				case kView:
					doView();
					break;
				default:
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start(); // Thread
	}

	void doView() {
		
	}

	void doLocation() {
		Mat frame = new Mat();
		if(cvSink.grabFrame(frame) == 0) {
			DriverStation.reportError(cvSink.getError(), false);
			return;
		}
		double imageHeight = frame.height();
		double imageWidth = frame.width();

		MatOfPoint2f imagePoints = new MatOfPoint2f();

		Rect roiRect = new Rect(new Point(0, imageHeight/5), new Size(imageWidth, imageHeight/3));
		Imgproc.rectangle(frame, roiRect.tl(), roiRect.br(), new Scalar(0,255,0));
		roiRect = new Rect(new Point(0, 2*imageHeight/3), new Size(imageWidth, imageHeight/3));
		Imgproc.rectangle(frame, roiRect.tl(), roiRect.br(), new Scalar(0,255,0));

		Imgproc.putText(frame, "Res "+imageWidth+"x"+imageHeight, new Point(imageWidth/2-100,imageHeight/2-20), 1, 1, new Scalar(0,200,200));

		// TODO Implement finding robot's position
		Point3 safeLocation = new Point3(0,0,0);

		// Location is a shared object between the threads so synchronize before accessing
		synchronized(location)
		{
			location = safeLocation;
		}
    	
//		Imgproc.resize(frame, frame, new Size(240,135));
		Mat outFrame = new Mat(frame,new Rect(new Point(640-120, 360-135), new Size(240, 135)));
		outputStream.putFrame(outFrame);
		
	}

	public void setMode(Mode m) {
		synchronized (mode) {
			mode = m;
		}
	}
	public static void set(Mode m) { GetInstance().setMode(m); }

	public static Point3 getPosition() {
		LocationCamera instance = GetInstance();
		Point3 safeLocation;
		// Location is a shared object between the threads so synchronize before accessing
		synchronized (instance.location) {
			safeLocation = instance.location;
		}
		return safeLocation;
	}
}
	
	



