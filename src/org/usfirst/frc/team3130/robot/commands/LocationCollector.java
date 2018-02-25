/**
 * 
 */
package org.usfirst.frc.team3130.robot.commands;

import java.util.ArrayList;
import java.util.Comparator;
import org.usfirst.frc.team3130.robot.sensors.LocationCamera;
import org.usfirst.frc.team3130.robot.sensors.LocationCamera.Location;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class LocationCollector extends Command {
	ArrayList<Location> queue = new ArrayList<Location>();
	Location receiver;

	public LocationCollector(Location sink) {
		receiver = sink;
		setRunWhenDisabled(true);
	}

	@Override
	protected void initialize() {
		LocationCamera cam = LocationCamera.GetInstance();

		// First define the left/right side of the field and initialize coordinates accordingly
		cam.setInitialPosition(Preferences.getInstance().getDouble("AutonPosition", 24.0));

		// Start processing video stream and calculate the location
		cam.set(LocationCamera.Mode.kLocation);
	}
	
	@Override
	protected void execute() {
		LocationCamera cam = LocationCamera.GetInstance();
		if (cam.hasNew()) {
			double now = Timer.getFPGATimestamp();
			// Remove all outdated (30sec) locations from the queue
			while (queue.size() > 0 && queue.get(0).timestamp + 30 < now) queue.remove(0);

			// Add a new location from the camera processor
			queue.add(cam.getLocation());

			if ( ! queue.isEmpty() ) {
				// TODO Implement a better criteria to choose the best measurement
				// For now let's just take the location with the median x
				ArrayList<Location> tempQueue = new ArrayList<Location>(queue);
				tempQueue.sort(new Comparator<Location>() {
					public int compare(Location a, Location b) { return Double.compare(a.x, b.x); }
				});
				receiver = tempQueue.get(tempQueue.size()/2);
			}
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		LocationCamera.GetInstance().set(LocationCamera.Mode.kDisabled);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
