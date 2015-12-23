package spaceshoot.entity.systems;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spaceshoot.ImageLoader;
import spaceshoot.entity.AbstractEntitySystem;
import spaceshoot.entity.facets.Move2DInt;
import spaceshoot.entity.facets.Renderable;

public class RenderSystem extends AbstractEntitySystem<Renderable> {

	protected Graphics2D gg;
	protected ImageLoader imageLoader;
	
	protected Long timestampPast;
	protected Long timestampCurrent;
	protected Long timestampFuture;
	protected Long timestampNow;

    private Logger log = LoggerFactory.getLogger(getClass());
	
	public RenderSystem(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}
	
	public void setGraphics2D(Graphics2D gg) {
		this.gg = gg;
	}
	
	public void setTimestampPast(Long timestampPast) {
		if (timestampCurrent != null && timestampPast > timestampCurrent) 
			throw new IllegalArgumentException("timestampPast must be before timestampCurrent ("+timestampPast+" vs"+timestampCurrent+")");
		this.timestampPast = timestampPast;
	}

	public void setTimestampCurrent(Long timestampCurrent) {
		if (timestampPast != null && timestampCurrent < timestampPast) 
			throw new IllegalArgumentException("timestampCurrent must be after timestampPast ("+timestampCurrent+" vs"+timestampPast+")");
		this.timestampCurrent = timestampCurrent;
	}
	
	public void setTimestampFuture(Long timestampFuture) {
		//TODO sanity check
		this.timestampFuture = timestampFuture;
	}

	public void setTimestampNow(Long timestampNow) {
		if (timestampPast != null && timestampNow < timestampPast) 
			throw new IllegalArgumentException("timestampNow must be after timestampPast ("+timestampNow+" vs"+timestampPast+")");
//		if (timestampCurrent != null && timestampNow > timestampCurrent) 
//			throw new IllegalArgumentException("timestampNow must be before timestampCurrent ("+timestampNow+" vs"+timestampCurrent+")");
		this.timestampNow = timestampNow;
	}

	@Override
	public void process(Renderable t) {
		
		//System.out.println("Drawing "+t.getImageName());
		Image image = imageLoader.getImageVolatile(t.getImageName());
		//If its a moving thing, interpolate based on position
		if (t instanceof Move2DInt) {
			Move2DInt m = (Move2DInt) t;
			
			double p = 0.0;
			//if (timestampCurrent-timestampPast > 0) {
			//	p = (timestampNow-timestampPast)/(timestampCurrent-timestampPast);
			//}
			double i = (double) (timestampNow-timestampCurrent);
			double j = (double) (timestampFuture-timestampCurrent); 
			p = i/j;
			
			if (p < 0.0) throw new RuntimeException("interpolation cannot be < 0.0 ("+p+")");
			if (p > 1.0) throw new RuntimeException("interpolation cannot be > 1.0 ("+p+")");
			log.info("i = "+i);
			log.info("j = "+j);
			log.info("p = "+p);
			
			int xInter = (int)((m.getPastX()*p) + (m.getX()*(1.0-p)));
			int yInter = (int)((m.getPastY()*p) + (m.getY()*(1.0-p)));
			int left = xInter-t.getWidth()/2;
			int top = yInter-t.getHeight()/2;
			gg.drawImage(image, left, top, null);
		} else {
			//its a static thing, just draw it
			gg.drawImage(image, t.getLeft(), t.getTop(), null);
		}
		
	}

	@Override
	public Class<Renderable> getFacetClass() {
		return Renderable.class;
	}

}
