package com.macleod.engine.graphics.gui;

import java.awt.Graphics;

import com.macleod.engine.graphics.Bitmap;
import com.macleod.engine.graphics.Colour;

public abstract class GuiLayer extends Bitmap {

	private int x, y;
	
	public GuiLayer(int width, int height) { this(0, 0, width, height); }
	
	public GuiLayer(int x, int y, int width, int height) {
		super(width, height);
		this.x = x;
		this.y = y;
	}
	
	/** 
	 * This method will be invoked whenever the system that this layer belongs to receives a GuiEvent.
	 * 
	 * @return
	 * 		The boolean returned represents whether this layer has 'absorbed' the event. For example, if someone has clicked on a button then anything beneath
	 * 		that Gui component doesn't need to also receive that event. By returning true the system would understand that the event doesn't need to travel to
	 *		any other layers. Returning false would indicate that the event wasn't absorbed by this layer (and thus should still be passed to other layers in
	 *		the system)
	 */
	public abstract boolean recieveGuiEvent(GuiEvent event);
	
	/**
	 * This method is called each 'frame' for the layer (alongside how long it has been, in seconds, since the last update)
	 */
	public abstract void update(float sTimeDelay);
	
	/**
	 * This method is called whenever the layer is added to a GuiSystem
	 */
	public abstract void onLoad();
	/**
	 * This method is called whenever the layer is removed from a GuiSystem
	 */
	public abstract void onUnload();

	public final void setX(int x) { this.x = x; }
	public final void setY(int y) { this.y = y; }

	public final void drawToGraphicsInstance(Graphics target) { drawToGraphicsInstance(target, x, y); }

	public void prepareForRender() { setPixelRegion(0, 0, width, height, Colour.NO_ALPHA_VALUE); }
	
	public final int getX() { return x; }
	public final int getY() { return y; }
	
}
