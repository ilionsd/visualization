package com.insign.common.function_graphics;

import java.awt.*;
import java.awt.geom.Area;
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
		getGraphics().drawRect(0, 0, getImageWidth(), getImageHeight());
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
		double radius = 2;
		getGraphics().drawOval((int)(point.getX() - radius), (int)(point.getY() - radius),(int)(2 * radius), (int)(2 * radius));
	}

	protected int getKnotsCount() {
		double interval = getxMax() - getxMin();
		int knotsCount = Math.max(POINTS_PER_FUNCTION, (int)Math.ceil(interval / DRAW_FUNCTION_STEP));
		return knotsCount;
	}

	protected Point2D scale(Point2D point) {
		return scale(point.getX(), point.getY());
	}

	protected Point2D scale(double x, double y) {
		float displayX = (float)((x - xMin) / xPerPoint);
		float displayY = (float)((yMax - y) / yPerPoint);
		return new Point2D.Double(displayX, displayY);
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
