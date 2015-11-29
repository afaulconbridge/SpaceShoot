package spaceshoot.entity.systems;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import spaceshoot.ImageLoader;
import spaceshoot.entity.AbstractEntitySystem;
import spaceshoot.entity.facets.Move2DInt;
import spaceshoot.entity.facets.Renderable;

public class RenderSystem extends AbstractEntitySystem<Renderable> {

	protected Graphics2D gg;
	protected ImageLoader imageLoader;
	
	protected Long timestampPast;
	protected Long timestampCurrent;
	protected Long timestampNow;
	
	public RenderSystem(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}
	
	public void setGraphics2D(Graphics2D gg) {
		this.gg = gg;
	}
	
	public void setTimestampPast(Long timestampPast) {
		this.timestampPast = timestampPast;
	}

	public void setTimestampCurrent(Long timestampCurrent) {
		this.timestampCurrent = timestampCurrent;
	}

	public void setTimestampNow(Long timestampNow) {
		this.timestampNow = timestampNow;
	}

	@Override
	public void process(Renderable t) {
		//System.out.println("Drawing "+t.getImageName());
		BufferedImage image = imageLoader.getImage(t.getImageName());
		//If its a moving thing, interpolate based on position
		if (t instanceof Move2DInt) {
			Move2DInt m = (Move2DInt) t;
			double p = 0.0;
			if (timestampCurrent-timestampPast > 0) {
				p = (timestampNow-timestampPast)/(timestampCurrent-timestampPast);
			}
			int xInter = (int)((m.getPastX()*p) + (m.getX()*(1-p)));
			int yInter = (int)((m.getPastY()*p) + (m.getY()*(1-p)));
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
