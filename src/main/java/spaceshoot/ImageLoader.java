package spaceshoot;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ImageLoader {
	
	
	LoadingCache<String, BufferedImage> imagesBufferedCache = CacheBuilder.newBuilder()
		       .maximumSize(100)
		       .build(
		           new CacheLoader<String, BufferedImage>() {
		             public BufferedImage load(String name) {
		         		URL url = this.getClass().getResource("/"+name+".png");
		        		if (url == null) throw new IllegalArgumentException("Invalid name specified: "+name);
		        		BufferedImage img = null;
		        		try {
		        		    img = ImageIO.read(url);
		        		} catch (IOException e) {
		        			//if we can't access it, something major has gone wrong
		        			//upgrade to an unchecked exception
		        			throw new RuntimeException(e);
		        		}
		        		return img;
		             }
		           });
	
	protected Map<String, VolatileImage> imagesVolatileMap = new HashMap<>();

	public BufferedImage getImageBuffered(String name) {
		try {
			return imagesBufferedCache.get(name);
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());			
		}
	}
	
	public VolatileImage getImageVolatile(String name) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		
		if (imagesVolatileMap.containsKey(name)) {
			VolatileImage imageV = imagesVolatileMap.get(name);
			if (imageV.validate(gc) == VolatileImage.IMAGE_OK) {
				return imageV;
			}
			
		}
		//image is not in the map, or needs to be replaced in the map
		BufferedImage imageB = getImageBuffered(name);
		VolatileImage imageV = createVolatileFromBuffered(imageB);
		imagesVolatileMap.put(name, imageV);
		return imageV;
	}
	
	public VolatileImage createVolatileFromBuffered(BufferedImage imageB) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		
		VolatileImage imageV;
		//keep trying to create it until we succeed
		do {
			imageV = gc.createCompatibleVolatileImage(imageB.getWidth(), imageB.getHeight(), imageB.getTransparency());
		} while (imageV.validate(gc)== VolatileImage.IMAGE_INCOMPATIBLE);
		
		//now we have a volatile image assigned, draw the buffered image onto it

		Graphics2D g = null;
		try {
			g = imageV.createGraphics();
			//to handle transparancy, clear to transparent instead of default white
			if (imageB.getTransparency() != Transparency.OPAQUE){
				g.setComposite(AlphaComposite.Src);
				g.setColor(Color.black);
				g.clearRect(0, 0, imageV.getWidth(), imageV.getHeight());
			}
			g.drawImage(imageB,null,0,0);
		} finally {	
			// It's always best to dispose of your Graphics objects.
			if (g != null)
				g.dispose();
		}
		
		return imageV;
	}
}
