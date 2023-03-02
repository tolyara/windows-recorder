package team.javafx.recorder;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class RobotDemo {

	public static void main(String[] args) throws AWTException, InterruptedException {
//		click(100, 100);
//		trackMouse();
		
		doKeyInput();
	}

	public static void doClick(int button, int x, int y) throws AWTException {
		Robot bot = new Robot();
		bot.mouseMove(x, y);
		bot.mousePress(button);
		bot.mouseRelease(button);
	}
	
	/*
	 * For this demo you need to drag your input source to right upper side of screen
	 */
	public static void doKeyInput() throws AWTException, InterruptedException {
		Thread.sleep(2000);
		
		doClick(InputEvent.BUTTON1_MASK, 1900, 200);		
		Robot bot = new Robot();
		
		bot.keyPress(KeyEvent.VK_I);
		bot.keyRelease(KeyEvent.VK_I);
		bot.keyPress(KeyEvent.VK_SPACE);
		bot.keyRelease(KeyEvent.VK_SPACE);
		
		bot.keyPress(KeyEvent.VK_B);
		bot.keyRelease(KeyEvent.VK_B);
		bot.keyPress(KeyEvent.VK_O);
		bot.keyRelease(KeyEvent.VK_O);
		bot.keyPress(KeyEvent.VK_T);
		bot.keyRelease(KeyEvent.VK_T);
	}
	
	public static void trackMouse() throws AWTException, InterruptedException
	{
		while(true)
		{
			PointerInfo a = MouseInfo.getPointerInfo();
			Point b = a.getLocation();
			int x = (int) b.getX();
			int y = (int) b.getY();
			System.out.println(y + " "+ x+"");
		
			Robot r = new Robot();
			r.mouseMove(x, y - 50);
			Thread.sleep(500);
		}
	}

}
