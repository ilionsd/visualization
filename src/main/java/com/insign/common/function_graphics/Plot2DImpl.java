package com.insign.common.function_graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by ilion on 13.03.2015.
 */
public class Plot2DImpl extends AbstractPlot2D{
	public static final Color[] COLORS_DEFAULT_ROTATION = new Color[] {Color.BLACK};

	private BufferedImage image;
	private Graphics2D graphics;

	private List<Color> colors;
	private ListIterator<Color> colorIterator;

	private Color backgroundColor = Color.WHITE;

	private void init(){
		image = new BufferedImage(getImageWidth(), getImageHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
		graphics = image.createGraphics();
		setColorsRotation(COLORS_DEFAULT_ROTATION);
		clear();
	}

	public Plot2DImpl(int width, int height, double xMin, double yMin, double xMax, double yMax) {
		super(width, height, xMin, yMin, xMax, yMax);
		init();
	}

	@Override
	protected Graphics2D getGraphics() {
		return graphics;
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public void setColorsRotation(Color[] colors) {
		if (colors == null || colors.length == 0)
			throw new IllegalArgumentException("Colors array is null or empty");
		this.colors = new ArrayList<Color>();
		for (Color color : colors)
			this.colors.add(color);
		colorIterator = this.colors.listIterator();
	}

	@Override
	public Color[] getColorsRotation() {
		Color[] colors = new Color[this.colors.size()];
		return this.colors.toArray(colors);
	}

	@Override
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
	}

	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	protected Color getNextColor() {
		if (colorIterator.hasNext())
			return colorIterator.next();
		else {
			colorIterator = colors.listIterator();
			return getNextColor();
		}
	}
}
