package team.inlandia.recorder.event;

import java.awt.Point;

import team.inlandia.recorder.tool.SelectedZone;

public class MouseClick extends UserEvent {

	private Point point;
	
	private String screenshotPath;
	
	private SelectedZone selectedZone;
	
	public MouseClick(String name) {
		super(name);
	}

	public MouseClick(String name, Point point) {
		super(name);
		this.point = point;
	}

	public MouseClick(String name, Point point, String screenshotPath) {
		super(name);
		this.point = point;
		this.screenshotPath = screenshotPath;
	}

	public Point getPoint() {
		return point;
	}

	public String getScreenshotPath() {
		return screenshotPath;
	}

	public SelectedZone getSelectedZone() {
		return selectedZone;
	}

	public void setSelectedZone(SelectedZone selectedZone) {
		this.selectedZone = selectedZone;
	}

	@Override
	public String toString() {
		return "MouseClick [x = " + point.getX() + ", y = " + point.getY() + "]";
	}

	public boolean hasSelectedZone() {
		return selectedZone != null;
	}

}
