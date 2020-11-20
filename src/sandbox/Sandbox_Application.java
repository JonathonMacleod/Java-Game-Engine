package sandbox;

import com.macleod.engine.Application;
import com.macleod.engine.graphics.gui.GuiSystem;

/*
 * This class purely exists for testing - it doesn't need to be compiled in the main engine.
 */
public class Sandbox_Application {

	public static void main(String[] args) {
		final Application app = new Application("Sandbox", 16 * 100, 9 * 100);
		
		final GuiSystem guiSystem = new GuiSystem();
		guiSystem.addLayer(new Sandbox_ExampleGuiLayer());

		app.setGuiSystem(guiSystem);
		app.start();
	}
	
}
