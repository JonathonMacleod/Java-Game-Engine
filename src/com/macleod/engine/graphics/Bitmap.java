package com.macleod.engine.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.security.InvalidParameterException;

import com.macleod.engine.Log;

public class Bitmap {
	
	//TODO: Investigate the use of VolatileImages or MemoryImageSource and an accelerated Image instead of a BufferedImage for performance
	private final BufferedImage bufferedImage;
	
	protected final int[] pixels;
	public final int width, height;
	
	public Bitmap(int width, int height) {
		if((width < 0) || (height < 0)) {
			final InvalidParameterException exception = 
					new InvalidParameterException("The dimensions of a Bitmap must be positive. Was given dimensions (" + width + "," + height + ")");
			Log.error("An attempt was made to create a Bitmap with invalid dimensions", exception);
			throw exception;
		}
		
		this.width = width;
		this.height = height;

		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//		// We can exploit the internal pixel array of the BufferedImage to retrieve an integer array of ARGB colours, which, when manipulated, will alter the
//		// BufferedImage - still allowing the BufferedImage to be drawn using the included Java graphics library
		pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
	}
	
	public final int getWidth() { return width; }
	public final int getHeight() { return height; }
	public final boolean isPointInDimensions(int x, int y) { return ((x >= 0) && (y >= 0) && (x < width) && (y < height)); }	
	
	public void setPixel(int x, int y, int colourARGB) {
		assert (isPointInDimensions(x, y)) : 
			"Cannot set a pixel at point (" + x + "," + y + ") outside of the Bitmap dimensions (" + width + "," + height + ").";
		
		if(isPointInDimensions(x, y)) pixels[x + y * width] = colourARGB;
	}

	public void setPixelRegion(int x, int y, int width, int height, int colourARGB) {
		assert ((width > 0) && (height > 0)) : 
			"The dimensions of a pixel region being drawn cannot be non-positive. Was given dimensions (" + width + "," + height + ")";
		
		final int endingX = x + width - 1;
		final int endingY = y + height - 1;
		
		assert (isPointInDimensions(x, y)) :
			"Cannot set a pixel region starting at the point (" + x + "," + y + ") - outside of the Bitmap dimensions (" + width + "," + height + ").";
		assert (isPointInDimensions(endingX, endingY)) :
			"Cannot set a pixel region ending at the point (" + endingX + "," + endingY + ") outside of the Bitmap dimensions (" + width + "," + height + ").";
		
		yLoop : for(int currentY = y; currentY <= endingY; currentY++) {
			// If we have extended beyond the height of the bitmap, we have no further pixels to draw (since we draw from the top down)
			if(currentY >= this.height) break yLoop;
			if(currentY < 0) continue yLoop;

			xLoop : for(int currentX = x; currentX <= endingX; currentX++) {
				// If we have extended beyond the width of the bitmap, we don't need to consider the pixels still further beyond the end of the width
				if(currentX >= this.width) break xLoop;
				if(currentX < 0) continue xLoop;
				
				pixels[currentX + currentY * this.width] = colourARGB;
			}
		}
	}
	
	public final void drawToGraphicsInstance(Graphics target, int x, int y) {
		drawToGraphicsInstance(target, x, y, width, height);
	}
	
	public final void drawToGraphicsInstance(Graphics target, int x, int y, int scaledWidth, int scaledHeight) {
		assert (target != null) : "Cannot draw a Bitmap to a null Graphics instance";
		assert ((scaledWidth > 0) && (scaledHeight > 0)) : 
			"Cannot draw a Bitmap with a non-positive dimensions. Was given dimensions: (" + scaledWidth + "," + scaledHeight + ")";
		
		if(target != null) target.drawImage(bufferedImage, x, y, scaledWidth, scaledHeight, null);		
	}
	
	public int getPixelARGB(int x, int y) { 
		assert (isPointInDimensions(x, y)) : 
			"Cannot get the pixel colour at point (" + x + "," + y + ") outside of the Bitmap dimensions (" + width + "," + height + ").";
		
		return (isPointInDimensions(x, y) ? pixels[x + y * width] : Colour.NO_ALPHA_VALUE);
	}
	
}
