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
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;

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
		public double timestamp;
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
	private boolean locationNew = false;

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

		// Run this thing as a thread so the heavy vision processing won't block the main (robot's) thread.
		new Thread(() -> 
		{
			while(!Thread.interrupted()) 
			{
				Mode safeMode;
				synchronized(mode) {
					safeMode = mode;
				}
				try {
					switch(safeMode) {
					case kLocation:
						doLocation();
						break;
					case kView:
					default:
						doNothing();
					}
				}
				catch (InterruptedException e) {
					// This exception shouldn't really happen but we must handle it anyway
					System.err.println("Location Camera thread caught an interrupted exception");
					Thread.currentThread().interrupt();  // set interrupt flag
				}
			}
			System.err.println("LocationCamera thread got interrupted. Exiting the thread");
		}).start(); // Thread

	}

	void doNothing() throws InterruptedException {
		Thread.sleep(20);
	}

	void doView() throws InterruptedException {
		// TODO Reimplement the view camera. Copying frames from USB to Mat is the worst way to feed
		// the camera stream to the driver station. This method is not used in the robot yet.
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

		// Unfinished thing. Shouldn't be used yet. But sleep if it is.
		Thread.sleep(20);
	}

	void doLocation() throws InterruptedException {
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
			Thread.sleep(5000); // Probably the camera is unplugged, so sleep this many milliseconds
			return;
		}

		processFrame(frame);

		Imgproc.resize(frame, display, new Size(NTwidth,NTheight));
		outputStream.putFrame(display);
	}

	private boolean processFrame(Mat image) {
		boolean notFound = false;

		// First define the left/right side of the field and initialize coordinates accordingly
		initialPosition = Preferences.getInstance().getDouble("AutonPosition", 24.0);
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

		// Here we're going to store the found corners of the plates
		MatOfPoint2f topCorners, bottomCorners;

		// Initialize the LED detector
		DetectLED detector = new DetectLED()
				.withThresh(60)
				.withMinArea(image.size().area()/10000)
				.withMaxArea(image.size().area()/100)
				.withMaxSegment(image.width()/20);

		// Then try to find the scale's plate
		detector.findLEDs(image, topROI)
				.findSegments()
				.findChains();

		topCorners = detector.getCorners();
		if(topCorners == null) notFound = true;

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

		bottomCorners = detector.getCorners();
		if(bottomCorners == null) notFound = true;

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

		// Draw the ROIs for visibility
		Imgproc.rectangle(image, topROI.tl(),    topROI.br(),    new Scalar(64,64,64), 3);
		Imgproc.rectangle(image, bottomROI.tl(), bottomROI.br(), new Scalar(64,64,64), 3);

		if (notFound) return false;
		// Bail out if something was not found. Everything below this line makes sense
		// only if we found all 4 corners

		// Collect the corners into an array of 4 two-dimensional points
		MatOfPoint2f imagePoints = new MatOfPoint2f();
		imagePoints.push_back(topCorners);
		imagePoints.push_back(bottomCorners);

		// Find the translation (tvec) and the rotation (rvec) vectors
		// that represent how the origin is translated from the camera
		// and how the coordinate system is rotated relative to the camera
		Mat rvec = new Mat();
		Mat tvec = new Mat();
		Calib3d.solvePnP(objectPoints, imagePoints, cameraMatrix, distCoeffs, rvec, tvec);

		// Location is a shared object between the threads so synchronize before accessing
		synchronized(location)
		{
			location.x =  tvec.get(0, 0)[0];
			location.y = -tvec.get(2, 0)[0];
			location.heading = rvec.get(1,0)[0];
			location.timestamp = Timer.getFPGATimestamp();
			locationNew = false;
		}

		// Draw squares around the found corners
		Point2 tlOffset = new Point2(-image.width()/50, -image.width()/50);
		Point2 brOffset = new Point2( image.width()/50,  image.width()/50);
		for(Point corner: imagePoints.toList()) {
			Point a = tlOffset.plus(corner);
			Point b = brOffset.plus(corner);
			Imgproc.rectangle(image, a, b, new Scalar(0,255,0), 3);
		}

		// Put the results on screen as text
		String tvecText = String.format("Tvec %6.1f %6.1f %6.1f",
				tvec.get(0,0)[0], tvec.get(1,0)[0], tvec.get(2,0)[0]);
		String rvecText = String.format("Rvec %8.3f %8.3f %8.3f",
				rvec.get(0,0)[0], rvec.get(1,0)[0], rvec.get(2,0)[0]);
		Imgproc.putText(image, tvecText, new Point(10,10), 1, 3, new Scalar(0,255,0));
		Imgproc.putText(image, rvecText, new Point(10,40), 1, 3, new Scalar(0,255,0));

		return true;
	}

	public void setMode(Mode m) {
		synchronized (mode) {
			mode = m;
			modeChanged = true;
		}
	}
	public void set(Mode m) { GetInstance().setMode(m); }

	public boolean hasNew() {return locationNew;}

	public Location getLocation() {
		LocationCamera instance = GetInstance();
		Location safeLocation;
		// Location is a shared object between the threads so synchronize before accessing
		synchronized (instance.location) {
			safeLocation = instance.location;
			instance.locationNew  = false;
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
	public void setInitialPosition(double initP) {
		initialPosition = initP;
	}
}
