package spaceshoot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ImageLoader {
	
	
	LoadingCache<String, BufferedImage> images = CacheBuilder.newBuilder()
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

	public BufferedImage getImage(String name) {
		try {
			return images.get(name);
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());			
		}
	}
}
