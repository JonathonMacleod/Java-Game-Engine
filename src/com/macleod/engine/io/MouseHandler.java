package com.macleod.engine.io;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import com.macleod.engine.Application;
import com.macleod.engine.graphics.gui.GuiEvent;

//TODO: Must look at internal caching of button states for mouse button 'held'/'pressed' event distinction
public class MouseHandler extends MouseAdapter {

	private final Application application;
	
	public MouseHandler(Application application) { 
		assert (application != null) : "MouseHandler cannot handle events for a null Application";
		this.application = application;
	}
	
	@Override
    public void mousePressed(MouseEvent e) {
		if(application != null) {
			if(application.hasGuiSystem()) {
				final GuiEvent mousePressedEvent = new GuiEvent(GuiEvent.EventType.MOUSE_BUTTON_PRESSED, -1, e.getButton(), e.getX(), e.getY());
				application.getGuiSystem().recieveEvent(mousePressedEvent);
			}
		}
	}

	@Override
    public void mouseReleased(MouseEvent e) {
		if(application != null) {
			if(application.hasGuiSystem()) {
				final GuiEvent mouseReleasedEvent = new GuiEvent(GuiEvent.EventType.MOUSE_BUTTON_RELEASED, -1, e.getButton(), e.getX(), e.getY());
				application.getGuiSystem().recieveEvent(mouseReleasedEvent);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(application != null) {
			if(application.hasGuiSystem()) {
				final GuiEvent mouseMovedEvent = new GuiEvent(GuiEvent.EventType.MOUSE_CURSOR_MOVED, -1, -1, e.getX(), e.getY());
				application.getGuiSystem().recieveEvent(mouseMovedEvent);
			}
		}
	}

	@Override
    public void mouseMoved(MouseEvent e) {
		if(application != null) {
			if(application.hasGuiSystem()) {
				final GuiEvent mouseMovedEvent = new GuiEvent(GuiEvent.EventType.MOUSE_CURSOR_MOVED, -1, -1, e.getX(), e.getY());
				application.getGuiSystem().recieveEvent(mouseMovedEvent);
			}
		}
	}

	@Override
    public void mouseClicked(MouseEvent e) { }
	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) { }
	@Override
    public void mouseWheelMoved(MouseWheelEvent e) { }
	
}
