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

	VideoCapture capture = new VideoCapture();
//	private UsbCamera camera = new UsbCamera("CamZero", 0);
	CvSink cvSink;
	CvSource outputStream;
	private Mode mode = Mode.kDisabled;
	private boolean modeChanged = true;
	private Point3 location = new Point3();

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
				//open the video stream and make sure it's opened
				//We specify desired frame size and fps in constructor
				//Camera must be able to support specified framesize and frames per second
				//or this will set camera to defaults
				int count=1;
				while (!capture.open(0) /*CAPTURE_PORT, CAPTURE_COLS, CAPTURE_ROWS, CAPTURE_FPS)*/)
				{
					System.err.println("Error connecting to camera stream, retrying "+ count);
					count++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
				}

				//After Opening Camera we need to configure the returned image setting
				//all opencv v4l2 camera controls scale from 0.0 to 1.0
				capture.set(Videoio.CAP_PROP_EXPOSURE, 10);
				capture.set(Videoio.CAP_PROP_BRIGHTNESS, 30);
				
				
				outputStream = CameraServer.getInstance().putVideo(cameraName, 640, 400);
				
				
				
				
				
/*				
				
				camera.setResolution(1280, 720);
				// HD-3000 min exposure is 5, Tested: 5, 10, 20, 39, 78, 156, 312, 625...
				camera.setExposureManual(10);
				camera.setWhiteBalanceManual(4500);
				camera.setBrightness(30);
				camera.setFPS(3);

				// Actually putVideo does startAutomaticCapture() -- TODO: optimize
//				CameraServer.getInstance().startAutomaticCapture(camera);

//				outputStream = new CvSource(cameraName, VideoMode.PixelFormat.kMJPEG, 640, 400, 30);
				cvSink = CameraServer.getInstance().getVideo(camera);
//				CameraServer.getInstance().addServer(cvSink);
				System.out.println("LocationCamera init: "+ outputStream + ", "+ cvSink);
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
				System.err.println("LocationCamera thread got interrupted. Exiting the thread");
			}).start(); // Thread
			
		}
	}

	void doView() {
		if(modeChanged) {
			modeChanged = false;
/*			cvSink.free();
			camera.setResolution(424,240);
			camera.setExposureManual(78);
			camera.setWhiteBalanceManual(4500);
			camera.setBrightness(30);
			camera.setFPS(30);
*/			System.out.println("LocationCamera switched to View mode");
		}
		Mat frame = new Mat();
		if(!capture.read(frame)) {
			DriverStation.reportError("Error reading from capture", false);
			return;
		}
		outputStream.putFrame(frame);
	}

	void doLocation() {
		if(modeChanged) {
			modeChanged = false;
			/*
			camera.setResolution(1280, 720);
			// HD-3000 min exposure is 5, Tested: 5, 10, 20, 39, 78, 156, 312, 625...
			camera.setExposureManual(10);
			camera.setWhiteBalanceManual(4500);
			camera.setBrightness(30);
			camera.setFPS(3);
//			CameraServer.getInstance().getServer().setSource(outputStream);
 */
			System.out.println("LocationCamera switched to Location mode");
		}
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
    	
		Imgproc.resize(frame, frame, new Size(640,400));
//		Mat outFrame = new Mat(frame,new Rect(new Point(640-120, 360-135), new Size(240, 135)));
		outputStream.putFrame(frame);
		
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
