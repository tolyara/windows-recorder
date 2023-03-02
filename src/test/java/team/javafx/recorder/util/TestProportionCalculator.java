package team.javafx.recorder.util;

import org.junit.Assert;
import org.junit.Test;

public class TestProportionCalculator {
	
	@Test
	public void testGetOriginX() throws Exception {
		int originWidth = 1920;
		int currenWidth = 480;
		int currentX = 15;
		int expected = 60;
		
		int actual = CoordinateCalculator.getOrigin(originWidth, currenWidth, currentX);
		Assert.assertEquals(expected, actual);
	}

}
