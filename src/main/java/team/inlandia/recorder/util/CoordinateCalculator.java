package team.inlandia.recorder.util;

public class CoordinateCalculator {
	
	private CoordinateCalculator() {
		
	}
	
	public static int getOrigin(double originWidth, double currenWidth, double current) {
		return (int) (originWidth / currenWidth * current);
	}

}
