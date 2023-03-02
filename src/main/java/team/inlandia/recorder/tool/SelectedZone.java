package team.inlandia.recorder.tool;

import java.awt.Rectangle;

public class SelectedZone {

	private Rectangle zone;

	public SelectedZone(int startX, int startY, int endX, int endY) {
		zone = new Rectangle(startX, startY, endX - startX, endY - startY);
	}

	public SelectedZone(double startX, double startY, double endX, double endY) {
		zone = new Rectangle((int) startX, (int) startY, (int) (endX - startX), (int) (endY - startY));
	}

	public Rectangle getZone() {
		return zone;
	}

	public void setZone(Rectangle zone) {
		this.zone = zone;
	}

}
