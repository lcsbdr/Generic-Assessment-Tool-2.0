package de.assessmenttool.prisma;

import java.awt.Polygon;

public class Catalog3Angle {

	private String catalogName = "";
	private boolean isSelected = false;
	private double color1 = -1;
	private double color2 = -1;
	private double color3 = -1;
	private float percentage = 0;

	// these values are set by the prisma which contains the 3angle
	// when printing the prisma to determine coordinates for mouse-events
	private Polygon angleSide = null;
	private Polygon angleFront = null;

	public Catalog3Angle(String catalogName) {
		this.catalogName = catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setAngleSide(Polygon angleSide) {
		this.angleSide = angleSide;
	}
	public Polygon getAngleSide() {
		return angleSide;
	}
	public void setAngleFront(Polygon angleFront) {
		this.angleFront = angleFront;
	}
	public Polygon getAngleFront() {
		return angleFront;
	}
	public double getColor1() {
		return color1;
	}
	public void setColor1(double color1) {
		this.color1 = color1;
	}
	public double getColor2() {
		return color2;
	}
	public void setColor2(double color2) {
		this.color2 = color2;
	}
	public double getColor3() {
		return color3;
	}
	public void setColor3(double color3) {
		this.color3 = color3;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	public float getPercentage() {
		return percentage;
	}
}
