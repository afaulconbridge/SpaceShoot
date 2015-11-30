package spaceshoot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import spaceshoot.entity.systems.RenderSystem;

public class GameRenderer implements Runnable{

	protected final GameCore core; 
	protected final ImageLoader imageLoader = new ImageLoader();
	
	protected final RenderSystem renderSystem;
	
	public GameRenderer(GameCore core) {
		this.core = core;
		this.renderSystem = new RenderSystem(imageLoader);
		core.getSystemManager().register(renderSystem);
	}
	
	public void render(Graphics2D gg, long timestampPast, long timestampCurrent) {
		//white border outlining everything
		gg.setColor(Color.white);
		gg.drawRect(0,0, 1023, 767);
		
		renderSystem.setGraphics2D(gg);
		renderSystem.setTimestampPast(timestampPast);
		renderSystem.setTimestampCurrent(timestampCurrent);
		renderSystem.setTimestampNow(System.nanoTime());
		renderSystem.processAll();
		renderSystem.setGraphics2D(null);
		
		//draw the player last so they are on top
		BufferedImage playerImage = imageLoader.getImage("player");
		gg.drawImage(playerImage, core.playerRect.x, core.playerRect.y, null);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
