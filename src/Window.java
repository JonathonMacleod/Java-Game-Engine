import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Window extends Canvas {
	private static final long serialVersionUID = 1L;

	private final JFrame frame;
	private final Screen screen;
	private final BufferedImage displayedImage;
	
	public Window(String title, int width, int height) {
		// Canvas properties
		final Dimension canvasDimension = new Dimension(width, height);
		setSize(canvasDimension);
		setPreferredSize(canvasDimension);
		setMinimumSize(canvasDimension);
		setMaximumSize(canvasDimension);
		
		// Actually create the JFrame holding the canvas
		frame = new JFrame();
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(title);
		frame.setVisible(true);
		
		// Now we need to get an image that we can draw to the screen. This is how we render - by manipulating the pixels of this image that we keep pushing to
		// the screen
		displayedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		// This returns the reference to the actual pixels of the image - we pass this to screen for our processing
		final int[] pixelArray = ((DataBufferInt) displayedImage.getRaster().getDataBuffer()).getData();
		screen = new Screen(width, height, pixelArray);
	}
	
}
