package mouse;


import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

public class LeapMouseListener extends Listener{

	private Robot robot;
	private Dimension screenDimension;
	private boolean mouseClick = false;
	private Vector indexPointer = null;
	private boolean leftClick = false;
	private float rightDistance = 35f;

	public LeapMouseListener(Robot r) {
		super();
		this.robot = r;
	}

	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}

	public void onConnect(Controller controller) {
		System.out.println("Connected");
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		this.screenDimension = new Dimension(gd.getDisplayMode().getWidth(),gd.getDisplayMode().getHeight());
	}
	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	public void onFrame(Controller controller) {
		
		Frame frame = controller.frame();
	
		if (frame.hands().count() ==0) {
			this.indexPointer = null;
			return;
		}
			Hand rightHand = frame.hands().get(0);
			FingerList list = rightHand.fingers();
	
			Finger indexFinger = list.fingerType(Finger.Type.TYPE_INDEX)
					.get(0);
			
			Finger thumb = list.fingerType(Finger.Type.TYPE_THUMB).get(0);
			Finger middleFinger = list.fingerType(Finger.Type.TYPE_MIDDLE).get(0);
			Vector newPointer = indexFinger.tipPosition();
			if (rightHand.confidence() < 0.5f || !indexFinger.isExtended()) {
				this.indexPointer = null;
				return;
			}
			Bone indexBone = indexFinger.bone(Bone.Type.TYPE_PROXIMAL);
			float pinchDist = thumb.tipPosition().distanceTo(indexBone.center());
			// System.out.println("pinchDist: " + pinchDist);
			if (newPointer.getZ() > 0) {
				this.indexPointer = null;
				return;
			}
			if (pinchDist <= 25f) {
				this.leftClick();
			} else {
				this.leftRelease();
			}
			if(!leftClick){
			Bone middleBone = middleFinger.bone(Bone.Type.TYPE_PROXIMAL);
			float rightPinch = thumb.tipPosition().distanceTo(middleBone.center());
			//System.out.println("right pinch"+rightPinch);
			if (rightPinch <= rightDistance ) {
				this.rightClick();
			
			}
			}
			 if (this.indexPointer != null) {
				Vector difference = newPointer.minus(indexPointer);
				float smoothening = 10f;
				int dx = Math.round(difference.getX() * smoothening);
				int dy = -Math.round(difference.getY() * smoothening);
				Point currentPosition = MouseInfo.getPointerInfo().getLocation();
				this.mouseMove(currentPosition.x+dx, currentPosition.y+dy);
			}
			this.indexPointer = newPointer;
		
	}
	
	private void mouseMove(int x, int y) {
		
		this.robot.mouseMove(fitScreen(x, 0, screenDimension.width - 1),
				fitScreen(y, 0, screenDimension.height - 1));
	}
	
	private void leftClick() {
		if (this.mouseClick )
			return;
		System.out.println("left click");
		this.mouseClick = true;
		this.leftClick = true;
		this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	}

	private void leftRelease() {
		if (!this.mouseClick)
			return;
		System.out.println("left release");
		this.mouseClick = false;
		this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		this.leftClick = false;
	}
	
	private void rightClick() {
		if (this.mouseClick )
			return;
		System.out.println("right click");
		this.mouseClick = true;
		this.robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		this.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		this.mouseClick = false;
	}

	
	
	private int fitScreen(int val, int min, int max) {
		if (val < min)
			return min;
		else if (val > max)
			return max;
		else
			return val;
	}
	private void scrollMouse(int diff, Robot robot)
	{
	if(diff != 0)
	{
	diff = diff / Math.abs(diff);
	robot.mouseWheel(diff);
	}
	}
}
