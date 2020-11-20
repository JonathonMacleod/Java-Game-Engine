package sandbox;

import java.awt.event.MouseEvent;
import java.util.Random;

import com.macleod.engine.graphics.Colour;
import com.macleod.engine.graphics.gui.GuiEvent;
import com.macleod.engine.graphics.gui.GuiLayer;

/**
 * This class demonstrates a simple GuiLayer, which has only a single square (of size 50 x 50 at the position 50 x 50 in its GuiSystem).
 * <br />
 * Currently the example GuiLayer has the simple functionality of changing the colour of the layer whenever the user presses the left-mouse button while their
 * cursor is within the layer's bounds
 */
public class Sandbox_ExampleGuiLayer extends GuiLayer {

	private int currentColour = Colour.RED;
	
	public Sandbox_ExampleGuiLayer() { super(50, 50, 50, 50); }

	public boolean recieveGuiEvent(GuiEvent event) { 
		if(event != null) {
			if(event.isMouseEvent()) {
				if(event.getEventType() == GuiEvent.EventType.MOUSE_BUTTON_PRESSED) {
					final int cursorX = (int) event.getRelativeCursorX();
					final int cursorY = (int) event.getRelativeCursorY();
					
					// Make sure we only consider the event if the click occurred while the cursor is hovering over the layer's bounds
					if((cursorX >= getX()) && (cursorY >= getY()) && (cursorX < (getX() + getWidth())) && (cursorY < (getY() + getHeight()))) {
						final int mouseButton = event.getMouseButton();
						
						if(mouseButton == MouseEvent.BUTTON1) {
							randomizeBoxColour();
						}
						
						// We want to absorb the event since it occurred inside the layer's bounds
						return true;
					}
				}
			}
		}

		return false; 
	}

	private final void randomizeBoxColour() {
		final Random randomInstance = new Random();

		// We want to demonstrate transparency - but it's pointless if the whole box becomes invisible, so we have an alpha channel with a value in the range
		// 100 <= n < 256
		byte alphaComponent = (byte) (randomInstance.nextInt(255 - 100 + 1) + 100);
		
		byte redComponent = (byte) randomInstance.nextInt(256);
		byte greenComponent = (byte) randomInstance.nextInt(256);
		byte blueComponent = (byte) randomInstance.nextInt(256);
		
		currentColour = Colour.getARGB(alphaComponent, redComponent, greenComponent, blueComponent);
	}
	
	public void update(float sTimeDelay) {
		// We are demonstrating transparency, so only a border is drawn currently so that the center of the layer remains transparent
		final int borderThickness = 10;
		
		// Set the left and right borders
		setPixelRegion(0, 0, borderThickness, getHeight(), currentColour);
		setPixelRegion(getWidth() - borderThickness, 0, borderThickness, getHeight(), currentColour);

		// Set the top and bottom borders (don't bother drawing over the already drawn top and bottom to save time)
		setPixelRegion(borderThickness, 0, getWidth() - (2 * borderThickness), borderThickness, currentColour);
		setPixelRegion(borderThickness, getHeight() - borderThickness, getWidth() - (2 * borderThickness), borderThickness, currentColour);
		
//		setPixelRegion(0, 0, 15, 15, currentColour);
	}

	public void onLoad() { }
	public void onUnload() { }

}
