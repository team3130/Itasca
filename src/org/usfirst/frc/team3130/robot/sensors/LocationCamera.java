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
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSink;
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
	static final int NTwidth = 384;
	static final int NTheight = 240;

	// Camera intrinsics, obtained by "camera calibration" (see [google] tutorial_interactive_calibration.html)
	// These numbers are for the MS Lifecam HD3000 #1 at 1280x720 mode
	private double[] cameraDoubles = {1.1159304477424785e+03, 0., 6.2199798030884358e+02, 0.,
		    1.1159304477424785e+03, 3.8725727347891808e+02, 0., 0., 1.};
	private double[] distortionDoubles = {1.2109074822929057e-01, -1.0293273066620283e+00, 0., 0.,
		    1.8601846077358326e+00};
	private Mat cameraMatrix = new Mat(3, 3, CvType.CV_32F);
	private MatOfDouble distCoeffs = new MatOfDouble(distortionDoubles);


	private static LocationCamera m_pInstance;
	public static synchronized LocationCamera GetInstance()
	{
		if(m_pInstance == null) m_pInstance = new LocationCamera();
		return m_pInstance;
	}

	private UsbCamera camera = new UsbCamera("CamZero", 0);
	CvSink cvSink = CameraServer.getInstance().getVideo(camera);
	CvSource outputStream = CameraServer.getInstance().putVideo(cameraName, NTwidth, NTheight);
	private Mode mode = Mode.kDisabled;
	private boolean modeChanged = true;
	private Point3 location = new Point3();

	// Image matrixes are big so let's reuse them
	private Mat frame = new Mat();
	private Mat display = new Mat();

	public LocationCamera()
	{
		// Initialize the camera intrinsics
		for(int i=0; i<3; i++)
			for(int j=0; j<3; j++)
				cameraMatrix.put(i, j, cameraDoubles[3*i+j]);


		
		
		
		
		
		if(true /*|| camera.isConnected()*/) {
			// Run this thing as a thread so the heavy vision processing won't block the main (robot's) thread.
			new Thread(() -> 
			{
				
				// Done in implicit constructor
				//outputStream = CameraServer.getInstance().putVideo(cameraName, 640, 400);
				
				/* Done later when mode is selected
				camera.setResolution(1280, 800);
				// HD-3000 min exposure is 5, Tested: 5, 10, 20, 39, 78, 156, 312, 625...
				camera.setExposureManual(10);
				camera.setWhiteBalanceManual(4500);
				camera.setBrightness(30);
				camera.setFPS(3);
				*/

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
//						doView();
//						break;
					default:
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				System.err.println("LocationCamera thread got interrupted. Exiting the thread");
			}).start(); // Thread
			
		}
	}

	void doView() {
		if(modeChanged) {
			modeChanged = false;
//			cvSink.setEnabled(false);
			camera.setResolution(NTwidth, NTheight);
			camera.setExposureManual(78);
			camera.setWhiteBalanceManual(4500);
			camera.setBrightness(30);
			camera.setFPS(10);
//			cvSink.setEnabled(true);
			
//			server.setSource(camera);
			System.out.println("LocationCamera switched to View mode.");
		}
/*		Mat frame = new Mat();
		if(cvSink.grabFrame(frame, 1.0) == 0) {
			DriverStation.reportError("Error reading from capture", false);
			return;
		}
		outputStream.putFrame(frame);*/
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void doLocation() {
		if(modeChanged) {
			modeChanged = false;

			camera.setResolution(1280, 720);
			// HD-3000 min exposure is 5, Tested: 5, 10, 20, 39, 78, 156, 312, 625...
			camera.setExposureManual(10);
			camera.setWhiteBalanceManual(4500);
			camera.setBrightness(30);
			camera.setFPS(2);

			System.out.println("LocationCamera switched to Location mode");
		}
		if(cvSink.grabFrame(frame, 1.0) == 0) {
			DriverStation.reportError(cvSink.getError(), false);
			return;
		}
		double imageHeight = frame.height();
		double imageWidth = frame.width();

		MatOfPoint2f imagePoints = new MatOfPoint2f();

		Rect roiRect = new Rect(new Point(imageWidth/8, imageHeight/6), new Size(6*imageWidth/8, imageHeight/4));
		Imgproc.rectangle(frame, roiRect.tl(), roiRect.br(), new Scalar(0,255,0), 4);
		roiRect = new Rect(new Point(imageWidth/8, 2*imageHeight/3), new Size(6*imageWidth/8, imageHeight/4));
		Imgproc.rectangle(frame, roiRect.tl(), roiRect.br(), new Scalar(0,255,0), 4);

		Imgproc.putText(frame, "Res "+imageWidth+"x"+imageHeight, new Point(imageWidth/2-100,imageHeight/2-20), 1, 1, new Scalar(0,200,200));

		// TODO Implement finding robot's position
		Point3 safeLocation = new Point3(0,0,0);

		// Location is a shared object between the threads so synchronize before accessing
		synchronized(location)
		{
			location = safeLocation;
		}

		Imgproc.resize(frame, display, new Size(NTwidth,NTheight));
//		Mat outFrame = new Mat(frame,new Rect(new Point(640-120, 360-135), new Size(240, 135)));
		outputStream.putFrame(display);
	}

	public void setMode(Mode m) {
		synchronized (mode) {
			mode = m;
			modeChanged = true;
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
