package com.macleod.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.macleod.engine.graphics.gui.GuiSystem;
import com.macleod.engine.io.FocusHandler;
import com.macleod.engine.io.KeyboardHandler;
import com.macleod.engine.io.MouseHandler;

/**
 * This class is meant to represent the front-end of the application that the user will see (e.g. the window). It therefore is the start of all I/O management
 * and such. However, this means that the Application will appear as a blank window until a GuiSystem has been added (so it has something to render), and the
 * update cycle has been started (see {@code start})
 */
//TODO: Make a Screen class to contain the Canvas object - making the Application window truly distinct from the functionality of the Graphics rendering and
// 		input management. This is important for two reasons: one, it's not sightly to have the Canvas methods accessible through the Application instance
//		(since it's not important to the role of the Application window), and two, it should be a target to have the functionality of input management and
//		rendering repeatable for non-application window programs (e.g. an Applet)
public class Application extends Canvas {
	private static final long serialVersionUID = 1L;

	// The decision to make this volatile implies the need for safety between multiple threads. In most situations it's expected that only a single thread
	// would control the state of the application (starting or stopping) so this is purely precautionary
	private volatile boolean running = false;
	
	private final JFrame frame;
	private final Thread applicationThread;

	private GuiSystem guiSystem;
	
	public Application(int initialWidth, int initialHeight) { this("Application", initialWidth, initialHeight, 2); }
	public Application(String title, int initialWidth, int initialHeight) { this(title, initialWidth, initialHeight, 2); }
	
	public Application(String title, int initialWidth, int initialHeight, int numberOfGraphicsBuffers) {
		frame = new JFrame();
		// We need to attach the Canvas object that this class inherits properties from to the JFrame (because we actually use the Canvas component for
		// handling rendering and such - not the JFrame). This gives us options to easily restructure our code for a non-JFrame application like a web 
		// component if we wish
		frame.getContentPane().add(this);
		
		setTitle(title);
		setSize(initialWidth, initialHeight);
		setResizable(false);
		//TODO: Change this close operation to be DO_NOTHING_ON_CLOSE, with a shutdown hook that calls the 'stop' method
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		// We don't start with the window being visible until the user 'starts' the application - mostly this is so that the user is aware that they haven't
		// started the application when they don't see anything being drawn or updated, the milliseconds of delay before anything is rendered wouldn't have a
		// noticable impact
		frame.setVisible(false);
		
		Log.info("Have created Application window with dimensions (" + frame.getWidth() + ", " + frame.getHeight() + ")");
		
		// We want to execute everything the application does within it's own thread space, but we don't want to start it prematurely. We set it up here but
		// expect the user to start the thread via the start method once everything is ready
		applicationThread = new Thread(new Runnable() {
			public void run() {
				applicationLoop();
			}
		});
		// Java's VM will stop once all Threads left running are 'Daemon' - so make sure this one isn't to avoid the virtual machine closing it
		applicationThread.setDaemon(false);
		applicationThread.setPriority(Thread.MAX_PRIORITY);
		
		// To draw our graphics to the Canvas object, we will be using the supplied Java BufferStrategy/Graphics library
		createBufferStrategy(numberOfGraphicsBuffers);
		if(getBufferStrategy() == null) {
			final IllegalStateException exception = new IllegalStateException("Didn't find a BufferStrategy for the Application window after create attempt");
			Log.error("Failed to create a BufferStrategy for the Application window", exception);
			throw exception;
		} else {
			Log.info("Graphics system has been created for the Application window");
		}
		
		// As previously stated, it is this Application that inherits the I/O handlers - not the JFrame, so apply them to the Canvas parent class
		final MouseHandler mouseHandler = new MouseHandler(this);
		addMouseListener(mouseHandler);
		addMouseWheelListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		
		final KeyboardHandler keyboardHandler = new KeyboardHandler(this);
		addKeyListener(keyboardHandler);
		
		final FocusHandler focusHandler = new FocusHandler(this);
		addFocusListener(focusHandler);
		Log.info("Have finished adding input listeners to Application window");
	}
	
	public final void setTitle(String title) { 
		assert (title != null) : "Cannot set an Application window to have a null title";
		frame.setTitle(title); 
	}
	public final void centreWindow() { frame.setLocationRelativeTo(null); }
	public final void setVisible(boolean visible) { frame.setVisible(visible); }

	public final void setResizable(boolean resizable) { 
		frame.setResizable(resizable);
	}
	
	public final void setSize(int width, int height) { 
		assert ((width > 0) && (height > 0)) : 
			"Cannot set an Application window to have non-positive dimensions. Was given dimensions (" + width + "," + height + ")";

		//TODO: In the future, once the Canvas functionality we use for Graphics and input has been seperated from the Application window functionality, we
		//		will want to start having the sizing consider the JFrame border. For now, because we use the Canvas size (not JFrame size), we will not have a
		//		JFrame of the exact dimensions width and height once this method is called. Instead, our Application window will have the additional size of
		//		the JFrame borders and title area
		super.setSize(width, height);
		
		// Since we are setting the dimensions of the content being drawn within the JFrame (so not including the borders or title of the frame), we must pack
		// the frame so that the size of the JFrame is constricted to the dimensions of said content. This is what the 'pack' method accomplishes
		frame.pack();
	}
	
	public final void setGuiSystem(GuiSystem guiSystem) {
		//TODO: May need to handle hooking and unhooking GUI systems
		//TODO: Also need to look at making this Thread safe. Want to make sure that we aren't mid-render of a system when we change our target system
		this.guiSystem = guiSystem;
	}
	
	public synchronized void start() {
		assert (!running) : "Cannot start an application that is already running";

		Log.info("About to start Application thread");
		
		running = true;
		applicationThread.start();
		frame.setVisible(true);
	}
	
	public synchronized void stop() {
		assert (running) : "Cannot stop an application that is not running";
		
		Log.info("About to stop Application thread");
		
		running = false;
		// We give the Application 'forever' to close because we don't want to force any shutdowns until the Thread is comfortable that all resources have been
		// released
		try { applicationThread.join(0);
		} catch(Exception e) { Log.error("Exception thrown while closing Application thread", e); }
	}
	
	private void applicationLoop() {
		Log.info("Application thread has started");
		
		// Currently we are not limiting the number of updates that will happen per second, so we don't wait for a certain amount of time to elapse before 
		// updating
		final double nsTargetTimeBetweenUpdates = 0; //(1000000000.0 / 60.0);
		
		int numberOfUpdatesThisSecond = 0;
		long nsLastUpdate = System.nanoTime();
		long nsLastSecond = System.nanoTime();
		while(running) {
			final long nsCurrentTime = System.nanoTime();
			
			final long nsTimeSinceLastUpdate = nsCurrentTime - nsLastUpdate;
			final long nsTimeSinceLastSecond = nsCurrentTime - nsLastSecond;
			
			if(nsTimeSinceLastUpdate >= nsTargetTimeBetweenUpdates) {
				final float sTimeSinceLastUpdate = (nsTimeSinceLastUpdate / 1000000000.0f);
				
				beforeRenderCall();
				if(guiSystem != null) guiSystem.update(sTimeSinceLastUpdate);
				renderCall();
				
				numberOfUpdatesThisSecond++;
				
				nsLastUpdate = nsCurrentTime;
			}
			
			if(nsTimeSinceLastSecond >= 1000000000) {
				Log.info(numberOfUpdatesThisSecond + "fps!");
				
				nsLastSecond = nsCurrentTime;
				numberOfUpdatesThisSecond = 0;
			}
		}
		
		// As a warning we unhook all GuiLayers currently on the GuiSystem. This gives all the layers an idea that the program is closing so they can release
		// any resources
		if(guiSystem != null) guiSystem.onShutdown();
	}
	
	private final void beforeRenderCall() {
		final BufferStrategy bufferStrategy = getBufferStrategy();
		assert (bufferStrategy != null) : "Cannot prepare to render a frame when the BufferStrategy being drawn to is null";
		
		if(bufferStrategy != null) {
			// We need to get the Graphics instance for the current Buffer frame in the BufferStrategy
			final Graphics graphics = bufferStrategy.getDrawGraphics();

			if(graphics != null) {
				// We draw a black rectangle across the entire Buffer frame to make sure we don't see any remnants from a previous frame that was drawn
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, super.getWidth(), super.getHeight());
			} else {
				Log.error("A graphics instance was not found for the current draw frame");
			}
		}

		guiSystem.prepareForRender();
	}
	
	private final void renderCall() {
		final BufferStrategy bufferStrategy = getBufferStrategy();
		assert (bufferStrategy != null) : "Cannot render a frame when the BufferStrategy being drawn to is null";
		
		if(bufferStrategy != null) {
			// We need to get the Graphics instance for the current Buffer frame in the BufferStrategy
			final Graphics graphics = bufferStrategy.getDrawGraphics();

			if(graphics != null) {
				guiSystem.drawToGraphicsInstance(graphics);
				
				// We need to release the resources being used by this Graphics instance so that it can be 'flushed' to the Buffer frame. This is essentially
				// the draw call to the BufferStrategy
				graphics.dispose();
				
				// This changes (flips) the current Buffer frame to the other Buffer frame/frames in the BufferStrategy. Essentially this changes the current
				// frame to be the frame being drawn to the Canvas. This is what makes the Buffer frame we have drawn to visible on the Canvas object
				bufferStrategy.show();
			}
		}
	}
	
	
	public final GuiSystem getGuiSystem() { return guiSystem; }
	public final boolean hasGuiSystem() { return (guiSystem != null); }
	
}
