package team.javafx.recorder.old;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class OldDemo {

	public static void main(String[] args) {

	}

	private void doBeep() throws InterruptedException {
		Toolkit.getDefaultToolkit().beep();
		Thread.sleep(1500);
		Toolkit.getDefaultToolkit().beep();
	}

	private static void oldWay() {
		Frame frame = new Frame("Example Frame");
		Component textArea = new TextArea("Click here... "); // Create a component to add to the
																// frame; in this case a text area
																// with sample text
		textArea.addMouseListener(new MouseAdapter() { // Add a mouse listener to capture click events

			public void mouseClicked(MouseEvent evt) {
//			logOnScreen(evt);
//			saveScreenShot();
			}

//		private void saveScreenShot() {
//			try {
//				ScreenshotSaver.saveScreenShot();
//			} catch (HeadlessException | IOException | AWTException e) {
//				e.printStackTrace();
//			}
//		}

			private void logOnScreen(MouseEvent evt) {
				TextArea source = (TextArea) evt.getSource();

				if ((evt.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
					source.setText(source.getText() + "nLeft mouse button clicked on point [" + evt.getPoint().x + ","
							+ evt.getPoint().y + "]" + "\n");
				}

				if ((evt.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
					source.setText(source.getText() + "nCenter mouse button clicked on point [" + evt.getPoint().x + ","
							+ evt.getPoint().y + "]" + "\n");
				}

				if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
					source.setText(source.getText() + "nRight mouse button clicked on point [" + evt.getPoint().x + ","
							+ evt.getPoint().y + "]" + "\n");
				}
			}

		});

//Add the components to the frame; by default, the frame has a border layout

		frame.add(textArea, BorderLayout.NORTH);

//Show the frame

		int width = 700;

		int height = 500;

		frame.setSize(width, height);

		frame.setVisible(true);

//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

class MyMouseMotionListener implements MouseListener, AWTEventListener {

	@Override
	public void eventDispatched(AWTEvent event) {
		System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
		System.out.println(event);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouseClicked...");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}

class MousePositionTracker implements Runnable {

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100); // this will slow the capture rate to 0.1 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
			Point point = new Point(0, 0);
			pointerInfo = MouseInfo.getPointerInfo();
			point = pointerInfo.getLocation();
			int x = (int) point.getX(); // getX and getY return doubles so typecast
			int y = (int) point.getY();
			System.out.println("" + x + "   " + y); // to see it grabing locations not needed
		}
	}
}

////get global mouse position 
//Point p = MouseInfo.getPointerInfo().getLocation();
//System.out.println(p.x);//

//Thread thread = new Thread(new MouseMotion());
//thread.start();

//Toolkit toolkit = Toolkit.getDefaultToolkit();
//toolkit.addAWTEventListener(new MyMouseMotionListener(), AWTEvent.MOUSE_EVENT_MASK);		
//Frame frame = new Frame();
//frame.setVisible(true);

//oldWay();
//System.out.println(compareScreenshots());
