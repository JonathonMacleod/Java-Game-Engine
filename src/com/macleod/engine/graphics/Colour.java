package com.macleod.engine.graphics;

public class Colour {

	public static final int WHITE = getARGB(255, 255, 255, 255);
	public static final int BLACK = getARGB(255, 0, 0, 0);
	
	public static final int RED = getARGB(255, 255, 0, 0);
	public static final int BLUE = getARGB(255, 0, 255, 0);
	public static final int GREEN = getARGB(255, 0, 0, 255);
	
	public static final int NO_ALPHA_VALUE = getARGB(0, 0, 0, 0);
	
	private Colour() { }
	
	public final static int getARGB(int a, int r, int g, int b) { return ((a << 24) | (r << 16) | (g << 8) | b); } 

	public final static int getBComponent(int argb) { return (argb & 0x000000ff); }
	public final static int getGComponent(int argb) { return ((argb & 0x0000ff00) >> 8); }
	public final static int getRComponent(int argb) { return ((argb & 0x00ff0000) >> 16); }
	public final static int getAComponent(int argb) { return ((argb & 0xff000000) >> 24); }
	
}
