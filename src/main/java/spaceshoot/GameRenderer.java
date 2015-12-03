package spaceshoot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spaceshoot.entity.systems.RenderSystem;

public class GameRenderer {

	protected final GameCore core; 
	protected final ImageLoader imageLoader = new ImageLoader();
	
	protected final RenderSystem renderSystem;
	
    private Logger log = LoggerFactory.getLogger(getClass());

	
	public GameRenderer(GameCore core) {
		this.core = core;
		this.renderSystem = new RenderSystem(imageLoader);
		core.getEntitySystemManager().register(renderSystem);
	}
	
	public void render(Graphics2D gg, long timestampRenderStart) {
    	long timeStart = System.nanoTime();
		
		//white border outlining everything
		gg.setColor(Color.white);
		gg.drawRect(0,0, 1023, 767);
		
		renderSystem.setGraphics2D(gg);
		renderSystem.setTimestampPast(core.getTimestampPast());
		renderSystem.setTimestampCurrent(core.getTimestampCurrent());
		renderSystem.setTimestampFuture(core.getTimestampFuture());
		renderSystem.setTimestampNow(timestampRenderStart);
		renderSystem.processAll();
		renderSystem.setGraphics2D(null);
		
		//draw the player last so they are on top
		BufferedImage playerImage = imageLoader.getImage("player");
		gg.drawImage(playerImage, core.playerRect.x, core.playerRect.y, null);
    	
		long timeEnd = System.nanoTime();
    	long interval = timeEnd-timeStart;
    	int fps = (int) (1000000000L/interval);
        log.trace("RENDER ("+fps+"fps)");
        log.trace("start time    :"+timestampRenderStart);
	}

}
