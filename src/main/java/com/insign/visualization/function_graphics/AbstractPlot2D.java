package com.insign.visualization.function_graphics;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.function.Function;

/**
 * Created by ilion on 13.03.2015.
 */
public abstract class AbstractPlot2D implements Plot2D {
	protected static final double DRAW_FUNCTION_STEP = 1;
	protected static final int POINTS_PER_FUNCTION = 100;

	private int imageWidth, imageHeight;
	private double xMin, yMin, xMax, yMax;
	private double xPerPoint, yPerPoint;

	private void init() {
		xPerPoint = (xMax - xMin) / imageWidth;
		yPerPoint = (yMax - yMin) / imageHeight;
	}

	public AbstractPlot2D(int imageWidth, int imageHeight, double xMin, double yMin, double xMax, double yMax) {
		super();
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.xMin = Math.min(xMin, xMax);
		this.yMin = Math.min(yMin, yMax);
		this.xMax = Math.max(xMin, xMax);
		this.yMax = Math.max(yMin, yMax);

		init();
	}

	protected abstract Graphics2D getGraphics();

	@Override
	public abstract Image getImage();

	@Override
	public void clear() {
		getGraphics().setColor(getBackgroundColor());
		getGraphics().fillRect(0, 0, getImageWidth(), getImageHeight());
	}

	@Override
	public void drawFunction(Function<Double, Double> function) {
		drawFunction(function, getNextColor());
	}

	@Override
	public void drawFunction(Function<Double, Double> function, Color color) {
		int knotsCount = getKnotsCount();
		double step = (getxMax() - getxMin()) / (knotsCount - 1);
		getGraphics().setColor(color);
		double x = getxMin();
		Point2D prev = scale(x, function.apply(x));
		for (int k = 1; k <= knotsCount; k++) {
			x += step;
			Point2D current = scale(x, function.apply(x));
			getGraphics().drawLine((int)prev.getX(), (int)prev.getY(), (int)current.getX(), (int)current.getY());
			prev = current;
		}
	}

	@Override
	public void drawPoint(double x, double y) {
		drawPoint(x, y, getNextColor());
	}

	@Override
	public void drawPoint(double x, double y, Color color) {
		getGraphics().setColor(color);
		Point2D point = scale(x, y);
		double radius = 3.5;
		getGraphics().fillOval((int)(point.getX() - radius), (int)(point.getY() - radius),(int)(2 * radius), (int)(2 * radius));
	}

	@Override
	public void drawPoints(Point2D[] points) {
		drawPoints(points, getNextColor());
	}

	@Override
	public void drawPoints(Point2D[] points, Color color) {
		for (Point2D point : points)
			drawPoint(point.getX(), point.getY(), color);
	}

	@Override
	public void drawGrid(double dx, double dy) {
		drawGrid(dx, dy, getNextColor());
	}

	@Override
	public void drawGrid(double dx, double dy, Color color) {
		double centerX, centerY;
		if (getxMin() < 0 && getxMax() > 0)
			centerX = 0;
		else centerX = getxMin();
		if (getyMin() < 0 && getyMax() > 0)
			centerY = 0;
		else centerY = getyMin();
		getGraphics().setColor(color);
		drawVerticalGridTo(centerX, dx);
		drawVerticalGridTo(centerX, -dx);
		drawHorizontalGridTo(centerY, dy);
		drawHorizontalGridTo(centerY, -dy);
		drawOrigin(centerX, centerY);
	}

	private void drawVerticalGridTo(double center, double direction) {
		double nextLine = center + direction;
		while (Double.compare(getxMin(), nextLine) <= 0 && Double.compare(nextLine, getxMax()) <= 0) {
			int nextLineScaled = (int)scaleX(nextLine),
					yMinScaled = (int)scaleY(getyMin()),
					yMaxScaled = (int)scaleY(getyMax());
			getGraphics().drawLine(nextLineScaled, yMinScaled, nextLineScaled, yMaxScaled);
			String str = Double.toString(nextLine);
			getGraphics().drawString(str, nextLineScaled, yMinScaled);
			getGraphics().drawString(str, nextLineScaled, yMaxScaled);
			nextLine+=direction;
		}
	}

	private void drawHorizontalGridTo(double center, double direction) {
		double nextLine = center + direction;
		while (Double.compare(getyMin(), nextLine) <= 0 && Double.compare(nextLine, getyMax()) <= 0) {
			int nextLineScaled = (int)scaleY(nextLine),
					xMinScaled = (int)scaleX(getxMin()),
					xMaxScaled = (int)scaleX(getxMax());
			getGraphics().drawLine(xMinScaled, nextLineScaled, xMaxScaled, nextLineScaled);
			String str = Double.toString(nextLine);
			getGraphics().drawString(str, xMinScaled, nextLineScaled);
			getGraphics().drawString(str, xMaxScaled, nextLineScaled);
			nextLine+=direction;
		}
	}

	private void drawOrigin(double centerX, double centerY) {
		getGraphics().drawLine((int)scaleX(centerX), (int)scaleY(getyMin()), (int)scaleX(centerX), (int)scaleY(getyMax()));
		getGraphics().drawLine((int)scaleX(getxMin()), (int)scaleY(centerY), (int)scaleX(getxMax()), (int)scaleY(centerY));
	}

	private int getKnotsCount() {
		double interval = getxMax() - getxMin();
		int knotsCount = Math.max(POINTS_PER_FUNCTION, (int)Math.ceil(interval / DRAW_FUNCTION_STEP));
		return knotsCount;
	}

	private double scaleX(double x) {
		double displayX = (x - xMin) / xPerPoint;
		return displayX;
	}

	private double scaleY(double y) {
		double displayY = (yMax - y) / yPerPoint;
		return displayY;
	}

	private Point2D scale(Point2D point) {
		return scale(point.getX(), point.getY());
	}

	private Point2D scale(double x, double y) {
		return new Point2D.Double(scaleX(x), scaleY(y));
	}

	protected abstract Color getNextColor();

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	@Override
	public double getxMin() {
		return xMin;
	}

	@Override
	public double getyMin() {
		return yMin;
	}

	@Override
	public double getxMax() {
		return xMax;
	}

	@Override
	public double getyMax() {
		return yMax;
	}

}
