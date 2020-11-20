package com.macleod.engine.io;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.macleod.engine.Application;
import com.macleod.engine.graphics.gui.GuiEvent;

//TODO: Must look at internal caching of key states for mouse button 'held'/'pressed' event distinction
public class KeyboardHandler extends KeyAdapter {

	private final Application application;
	
	private ArrayList<Integer> keysBeingHeld = new ArrayList<Integer>(10);
	
	public KeyboardHandler(Application application) { 
		assert (application != null) : "KeyboardHandler cannot handle events for a null Application";
		this.application = application;
	}
	
    @Override
    public void keyPressed(KeyEvent e) { 
    	Integer keycode = Integer.valueOf(e.getKeyCode());

    	// We aren't already aware that we are holding the key, we only need to send out an event for the initial press (as well as the continued press)
    	if(!keysBeingHeld.contains(keycode)) {
    		if(application != null) {
    			if(application.hasGuiSystem()) {
    				final GuiEvent keyInitialPressEvent = new GuiEvent(GuiEvent.EventType.KEYBOARD_KEY_PRESSED, keycode, -1, -1, -1);
    				application.getGuiSystem().recieveEvent(keyInitialPressEvent);
    			}
    		}
    		keysBeingHeld.add(keycode);
    	}
    	
    	// Now we send out a notification that the key is still being held
		if(application != null) {
			if(application.hasGuiSystem()) {
				final GuiEvent keyContinuedPressEvent = new GuiEvent(GuiEvent.EventType.KEYBOARD_KEY_HELD, keycode, -1, -1, -1);
				application.getGuiSystem().recieveEvent(keyContinuedPressEvent);
			}
		}
    }

    @Override
    public void keyReleased(KeyEvent e) {
    	Integer keycode = Integer.valueOf(e.getKeyCode());
    	
    	if(application != null) {
			if(application.hasGuiSystem()) {
				final GuiEvent keyReleaseEvent = new GuiEvent(GuiEvent.EventType.KEYBOARD_KEY_RELEASED, keycode, -1, -1, -1);
				application.getGuiSystem().recieveEvent(keyReleaseEvent);
			}
		}
    	
    	keysBeingHeld.remove(keycode);
    }

    @Override
	public void keyTyped(KeyEvent e) { }
    
}
