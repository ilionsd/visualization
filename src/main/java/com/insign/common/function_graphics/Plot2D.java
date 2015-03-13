package com.insign.common.function_graphics;

import java.awt.*;
import java.util.function.Function;

/**
 * Created by ilion on 13.03.2015.
 */
public interface Plot2D {
	Image getImage();

	void clear();

	void drawFunction(Function<Double, Double> function);
	void drawFunction(Function<Double, Double> function, Color color);
	void drawPoint(double x, double y);
	void drawPoint(double x, double y, Color color);

	void setColorsRotation(Color[] colors);
	Color[] getColorsRotation();
	void setBackgroundColor(Color color);
	Color getBackgroundColor();

	double getxMin();

	double getyMin();

	double getxMax();

	double getyMax();
}
