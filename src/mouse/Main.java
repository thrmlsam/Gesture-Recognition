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
	// Create a listener and controller
	LeapMouseListener listener = new LeapMouseListener(robot);
	Controller controller = new Controller();
	// Have the listener receive events from the controller
	controller.addListener(listener);
	// Keep this process running until Enter is pressed
	System.out.println("Press Enter to quit...");
	try {
		System.in.read();
	} catch (IOException e) {
		e.printStackTrace();
	}
	// Remove the listener when done
	controller.removeListener(listener);
}
}
