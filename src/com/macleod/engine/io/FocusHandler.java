package com.macleod.engine.io;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import com.macleod.engine.Application;
import com.macleod.engine.graphics.gui.GuiEvent;

public class FocusHandler extends FocusAdapter {

	private final Application application;
	
	public FocusHandler(Application application) { 
		assert (application != null) : "FocusHandler cannot handle events for a null Application";
		this.application = application;
	}
	
	@Override
    public void focusGained(FocusEvent e) {
		if(application != null) {
			if(application.hasGuiSystem()) {
				final GuiEvent focusGainedEvent = new GuiEvent(GuiEvent.EventType.FOCUS_GAINED);
				application.getGuiSystem().recieveEvent(focusGainedEvent);
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(application != null) {
			if(application.hasGuiSystem()) {
				final GuiEvent focusLostEvent = new GuiEvent(GuiEvent.EventType.FOCUS_LOST);
				application.getGuiSystem().recieveEvent(focusLostEvent);
			}
		}
	}
	
}
