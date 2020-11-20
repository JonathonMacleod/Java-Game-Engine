package com.macleod.engine.graphics.gui;

import java.awt.Graphics;
import java.util.ArrayList;

public class GuiSystem {

	// It's important that we treat the layers like a stack structure because we need to allow some layers to 'absorb' events that we intended only for them
	// (stopping a button being clicked from also being registered on the layers beneath it). As such, the higher the index of a layer in the array, the
	// higher in the stack it is
	private ArrayList<GuiLayer> guiStack = new ArrayList<GuiLayer>(5);
	
	public void addLayer(GuiLayer layer) {
		assert (layer != null) : "Cannot add a null GuiLayer to a GuiSystem"; 
		if(guiStack != null) {
			guiStack.add(layer);
			layer.onLoad();
		}
	}
	
	public void removeLayer(GuiLayer layer) { 
		assert (layer != null) : "Cannot remove a null GuiLayer from a GuiSystem"; 
		if(layer != null) {
			guiStack.remove(layer);
			layer.onUnload();
		}
	}
	
	public void recieveEvent(GuiEvent event) {
		assert (event != null) : "GuiSystem cannot recieve a null event";
		
		// We iterate backwards because it is a stack
		for(int i = (guiStack.size() - 1); i >= 0; i--) {
			final GuiLayer currentLayer = guiStack.get(i);
			
			if(currentLayer != null) {
				final boolean wasAbsorbed = currentLayer.recieveGuiEvent(event);
				
				if(wasAbsorbed) break;
			}
		}
	}
	
	public void update(float sTimeDelay) {
		for(GuiLayer currentLayer : guiStack) {
			if(currentLayer != null) {
				currentLayer.update(sTimeDelay);
			}
		}
	}
	
	public void onShutdown() {
		for(GuiLayer currentLayer : guiStack) {
			if(currentLayer != null) {
				currentLayer.onUnload();
			}
		}
		
		// Just incase this method was called out of turn, we clear the stack because the layers have now 'unloaded' - meaning issues could arise if we tried 
		// to interact with them again
		guiStack.clear();
	}

	public void prepareForRender() {
		for(GuiLayer currentLayer : guiStack) {
			if(currentLayer != null) {
				currentLayer.prepareForRender();
			}
		}
	}
	
	public void drawToGraphicsInstance(Graphics graphics) {
		assert (graphics != null) : "Cannot draw a GuiSystem to a null Graphics instance";
		
		if(graphics != null) {
			// We iterate backwards because it is a stack
			for(int i = (guiStack.size() - 1); i >= 0; i--) {
				final GuiLayer currentLayer = guiStack.get(i);
				
				if(currentLayer != null) currentLayer.drawToGraphicsInstance(graphics);
			}
		}
	}
	
}
