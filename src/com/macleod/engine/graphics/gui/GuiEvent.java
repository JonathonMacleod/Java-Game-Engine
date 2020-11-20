package com.macleod.engine.graphics.gui;

/**
 * This class represents any input event from the user that needs to be relayed to GUI components. It is a generic class that contains information about all
 * possible types of event, regardless of what event type the particular object is representing. As such, it is important to determine the event type before
 * using information stored within the object. This can be done using the {@code isFocusEvent}, {@code isMouseEvent} and {@code isKeyboardEvent} methods.
 * 
 * <br /><br />
 * 
 * If possible, information given will be up to date. For example, with a mouse button being clicked - accompanying information about the mouse position may be
 * given for completeness - however, with a keyboard input (for example), any mouse information is not guaranteed to be correct
 */
//TODO: Polymorphism was considered for the event system, however, it's believed that due to the relatively infrequent creation and almost immediate
// destruction of the data, a larger-than-necessary object being passed is not the worst situation, and it does provide a 'compactness' that is appealing. This
// may be revisited in the future
public class GuiEvent {

	public enum EventType {
		/** A key press is defined as a singular action occuring once when someone presses a key on their keyboard */
		KEYBOARD_KEY_PRESSED(true, false, false), 
		/** A key release is defined as a singular action occuring once when someone releases a key they had previously been pushing on their keyboard */
		KEYBOARD_KEY_RELEASED(true, false, false), 
		/** A key being held is defined as an ongoing action occuring when someone has pressed a key and not yet released it */
		KEYBOARD_KEY_HELD(true, false, false),
		
		/** This event represents a movement from the cursor. There is no distinction between a movement when a button is held or not being held */
		MOUSE_CURSOR_MOVED(false, true, false),	

		/** A button press is defined as a singular action occuring once when someone presses a button on their mouse */
		MOUSE_BUTTON_PRESSED(false, true, false),
		/** A button release is defined as a singular action occuring once when someone releases a button they had previously had pressed on their mouse */
		MOUSE_BUTTON_RELEASED(false, true, false),
		/** A button being held is defined as an ongoing action occuring when someone has pressed a button and not yet released it */
		MOUSE_BUTTON_HELD(false, true, false),
		
		/** A focus gained event occurs whenever the user has started interacting with this application window - after interacting with another program */
		FOCUS_GAINED(false, false, true),
		/** A focus gained event occurs whenever the user has started interacting with another application window - after interacting with this program */
		FOCUS_LOST(false, false, true);
		
		private final boolean isKeyboardInput, isMouseInput, isFocusInput;
		private EventType(boolean isKeyboardInput, boolean isMouseInput, boolean isFocusInput) { 
			this.isKeyboardInput = isKeyboardInput;
			this.isMouseInput = isMouseInput;
			this.isFocusInput = isFocusInput;
		}
	}
	
	private final EventType typeOfEvent;

	private int keyCode;
	private int mouseButtonKey;
	
	private double relativeCursorX, relativeCursorY;
	
	public GuiEvent(EventType typeOfEvent) {
		assert (typeOfEvent != null) : "GuiEvent should have a non-null event type";
		this.typeOfEvent = typeOfEvent;
	}
	
	public GuiEvent(EventType typeOfEvent, int keycode, int mouseButton, double mouseX, double mouseY) {
		this(typeOfEvent);
		this.keyCode = keycode;
		mouseButtonKey = mouseButton;
		relativeCursorX = mouseX;
		relativeCursorY = mouseY;
	}
	
	public final boolean isFocusEvent() { return typeOfEvent.isFocusInput; }
	public final boolean isMouseEvent() { return typeOfEvent.isMouseInput; }
	public final boolean isKeyboardEvent() { return typeOfEvent.isKeyboardInput; }

	public final EventType getEventType() { return typeOfEvent; }
	
	/** The relative cursor position means relative to the top-left of the entire program window (not any one GUI component) */
	public final double getRelativeCursorX() { return relativeCursorX; }
	/** The relative cursor position means relative to the top-left of the entire program window (not any one GUI component) */
	public final double getRelativeCursorY() { return relativeCursorY; }

	/** The keycode value stored in this field corresponds to the constants found in the java.awt.event.KeyEvent class */
	public final int getKeyCode() { return keyCode; }

	/** The value returned represents the mouse button (using the constants that can be found in the java.awt.event.MouseEvent class) */
	public final int getMouseButton() { return mouseButtonKey; }
	
}
