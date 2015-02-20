package mouse;


import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;

import com.leapmotion.leap.Controller;

public class Main {public static void main(String[] args) {
	Robot robot = null;
	try {
		robot = new Robot();
	} catch (AWTException e) {
		System.err.println("Could not instantiate AWT Robot");
		return;
	}
	// Create a sample listener and controller
	LeapMouseListener listener = new LeapMouseListener(robot);
	Controller controller = new Controller();
	// Have the sample listener receive events from the controller
	controller.addListener(listener);
	// Keep this process running until Enter is pressed
	System.out.println("Press Enter to quit...");
	try {
		System.in.read();
	} catch (IOException e) {
		e.printStackTrace();
	}
	// Remove the sample listener when done
	controller.removeListener(listener);
}
}
