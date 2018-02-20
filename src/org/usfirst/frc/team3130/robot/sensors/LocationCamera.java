package org.usfirst.frc.team3130.robot.sensors;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team3130.robot.vision.DetectLED;
import org.usfirst.frc.team3130.robot.vision.Point2;
import org.usfirst.frc.team3130.robot.vision.DetectLED.Chain;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
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

	public class Location {
		public double x;
		public double y;
		public double heading;
	}

	public static String cameraName = "StartCam";
	static final int NTwidth = 320;
	static final int NTheight = 180;

	// Camera intrinsics, obtained by "camera calibration" (see [google] tutorial_interactive_calibration.html)
	// These numbers are for the MS Lifecam HD3000 #1 at 1280x720 mode
	private double[] cameraDoubles = {1.1159304477424785e+03, 0., 6.2199798030884358e+02, 0.,
		    1.1159304477424785e+03, 3.8725727347891808e+02, 0., 0., 1.};
	private double[] distortionDoubles = {1.2109074822929057e-01, -1.0293273066620283e+00, 0., 0.,
		    1.8601846077358326e+00};
	private Mat cameraMatrix = new Mat(3, 3, CvType.CV_32F);
	private MatOfDouble distCoeffs = new MatOfDouble(distortionDoubles);
	private MatOfPoint3f objectPointsL = new MatOfPoint3f();
	private MatOfPoint3f objectPointsR = new MatOfPoint3f();


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
	private double initialPosition = 1.0;
	private Location location = new Location();

	// Image matrixes are big so let's reuse them
	private Mat frame = new Mat();
	private Mat display = new Mat();

	public LocationCamera()
	{
		// Initialize the camera intrinsics
		for(int i=0; i<3; i++)
			for(int j=0; j<3; j++)
				cameraMatrix.put(i, j, cameraDoubles[3*i+j]);

		List<Point3> objectLeft = new ArrayList<Point3>();
		List<Point3> objectRight = new ArrayList<Point3>();
		// Left side
		objectLeft.add(new Point3(-89.44, -57, 300));
		objectLeft.add(new Point3(-55.09, -57, 300));
		objectLeft.add(new Point3(-71.02, -10, 146));
		objectLeft.add(new Point3(-36.91, -8, 146));
		// Right side
		objectRight.add(new Point3(55.09, -57, 300));
		objectRight.add(new Point3(89.44, -57, 300));
		objectRight.add(new Point3(36.91, -8, 146));
		objectRight.add(new Point3(71.02, -10, 146));
		objectPointsL.fromList(objectLeft);
		objectPointsR.fromList(objectRight);

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
			camera.setFPS(15);
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

		processFrame(frame);

		Imgproc.resize(frame, display, new Size(NTwidth,NTheight));
//		Mat outFrame = new Mat(frame,new Rect(new Point(640-120, 360-135), new Size(240, 135)));
		outputStream.putFrame(display);
	}

	private boolean processFrame(Mat image) {
		MatOfPoint2f imagePoints = new MatOfPoint2f();
		MatOfPoint3f objectPoints;
		Rect topROI, bottomROI;
		if(initialPosition < 0) {
			objectPoints = objectPointsL;
			topROI = new Rect(
					new Point(0.2*image.width(), 0.14*image.height()),
					new Size(0.6*image.width(), 0.24*image.height()));
			bottomROI = new Rect(
					new Point(0.0*image.width(), 0.6*image.height()),
					new Size(1.0*image.width(), 0.22*image.height()));
		}
		else {
			objectPoints = objectPointsR;
			topROI = new Rect(
					new Point(0.2*image.width(), 0.14*image.height()),
					new Size(0.6*image.width(), 0.24*image.height()));
			bottomROI = new Rect(
					new Point(0.05*image.width(), 0.6*image.height()),
					new Size(0.9*image.width(), 0.22*image.height()));
		}

		Imgproc.rectangle(image, topROI.tl(),    topROI.br(),    new Scalar(64,64,64), 3);
		Imgproc.rectangle(image, bottomROI.tl(), bottomROI.br(), new Scalar(64,64,64), 3);


		DetectLED detector = new DetectLED()
				.withThresh(60)
				.withMinArea(image.size().area()/10000)
				.withMaxArea(image.size().area()/100)
				.withMaxSegment(image.width()/20);

		detector.findLEDs(image, topROI)
				.findSegments()
				.findChains();

		MatOfPoint2f twoCorners = detector.getCorners();
		if(twoCorners == null) return false;
		imagePoints.push_back(twoCorners);

		for(int i = 0; i < detector.lights.size(); i++) {
			Point center = detector.lights.get(i);
			Imgproc.circle(image, center , (int)(image.width()/80), new Scalar(255,0,255), 3);
		}

		if(detector.chains.size() > 0) {
			Chain sp = detector.chains.get(0);
			if(sp.nodes.size() > 1) {
				int i = 0;
				Point2 pointA = new Point2(0,0);
				for(Point2 pointB: sp.nodes) {
					if(i > 0) {
						Imgproc.line(image, pointA, pointB, new Scalar(0, 255, 255), 3);
					}
					pointA = pointB;
					i++;
				}
			}
		}

		detector.findLEDs(image, bottomROI)
				.findSegments()
				.findChains();

		twoCorners = detector.getCorners();
		if(twoCorners == null) return false;
		imagePoints.push_back(twoCorners);

		for(int i = 0; i < detector.lights.size(); i++) {
			Point center = detector.lights.get(i);
			Imgproc.circle(image, center , (int)(image.width()/80), new Scalar(255,0,255), 3);
		}
		if(detector.chains.size() > 0) {
			Chain sp = detector.chains.get(0);
			if(sp.nodes.size() > 1) {
				int i = 0;
				Point2 pointA = new Point2(0,0);
				for(Point2 pointB: sp.nodes) {
					if(i > 0) {
						Imgproc.line(image, pointA, pointB, new Scalar(0, 255, 255), 3);
					}
					pointA = pointB;
					i++;
				}
			}
		}
		List<Point> corners = imagePoints.toList();
		for(Point corner: corners) {
			int half = image.width()/50;
			Point a = new Point2(-half, -half).plus(corner);
			Point b = new Point2( half,  half).plus(corner);
			Imgproc.rectangle(image, a, b, new Scalar(0,255,0), 3);
		}

		Mat rvec = new Mat();
		Mat tvec = new Mat();
		Calib3d.solvePnP(objectPoints, imagePoints, cameraMatrix, distCoeffs, rvec, tvec);
		String text = String.format("Tvec %6.1f %6.1f %6.1f",
			tvec.get(0,0)[0],
			tvec.get(1,0)[0],
			tvec.get(2,0)[0]);
		Imgproc.putText(image, text, new Point(10,10), 1, 1, new Scalar(0,255,0));
		text = String.format("Rvec %8.3f %8.3f %8.3f",
				rvec.get(0,0)[0],
				rvec.get(1,0)[0],
				rvec.get(2,0)[0]);
		Imgproc.putText(image, text, new Point(10,40), 1, 1, new Scalar(0,255,0));

		// Location is a shared object between the threads so synchronize before accessing
		synchronized(location)
		{
			location.x =  tvec.get(0, 0)[0];
			location.x = -tvec.get(2, 0)[0];
			location.heading = rvec.get(1,0)[0]; 
		}

		return true;
	}

	public void setMode(Mode m) {
		synchronized (mode) {
			mode = m;
			modeChanged = true;
		}
	}
	public static void set(Mode m) { GetInstance().setMode(m); }

	public static Location getLocation() {
		LocationCamera instance = GetInstance();
		Location safeLocation;
		// Location is a shared object between the threads so synchronize before accessing
		synchronized (instance.location) {
			safeLocation = instance.location;
		}
		return safeLocation;
	}

	/**
	 * @return the initialPosition
	 */
	public double getInitialPosition() {
		return initialPosition;
	}

	/**
	 * @param initialPosition the initialPosition of the robot relative to the center line.
	 * Positive direction is to the right. The value can be very approximate.
	 */
	public void setInitialPosition(double initialPosition) {
		this.initialPosition = initialPosition;
	}
}
