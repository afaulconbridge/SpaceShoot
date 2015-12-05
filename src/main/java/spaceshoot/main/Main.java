package spaceshoot.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spaceshoot.GameCore;
import spaceshoot.GameRenderer;
import spaceshoot.entity.entities.EnemyShip;

public class Main {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) {
		new Main().run();
	}
	
	public void run() {
		JFrame frame = new JFrame();

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration gc = device.getDefaultConfiguration();

        frame.setTitle("SpaceShoot");
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);//we run at a regular redraw, so ignore repaint requests outside of that
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        device.setFullScreenWindow(frame); //will also make visible
        
        if (frame.getSize().width < 1024 || frame.getSize().height < 768) {
        	throw new RuntimeException("Must be at least 1024x768 resolution");
        }

        frame.createBufferStrategy(2);

        //setup the required sim and renderer
        GameCore core = GameCore.create();
		GameRenderer renderer = new GameRenderer(core);
		

	
		//create some dummy enemies for testing
		EnemyShip.create(core.getEntitySystemManager(), "baddie", 500,50, 27,21 , -1, 0);
						
		long timeStepStartLast = 0;
		while(true) {
			long timeStepStart = System.nanoTime();
			log.info("timeStepStart = "+timeStepStart);
			log.info("timeStepInterval = "+(timeStepStart-timeStepStartLast));
			timeStepStartLast = timeStepStart;
			//limit sim to 30 FPS
			if (timeStepStart > core.getTimestampFuture()) {
				core.run(timeStepStart);
			}
			
			//now render

	        BufferStrategy bufferStrategy = frame.getBufferStrategy();
	        Graphics g = null;
	        try {
	        	g = bufferStrategy.getDrawGraphics();
	            Graphics2D gg = (Graphics2D) g;
	            
	            //now do some background drawing
	            gg.setColor(Color.black);
	            gg.fillRect(0, 0, frame.getWidth(), frame.getHeight());
	            
	            //create a new graphics object for the game renderer to handle
	            //this is fixed size
	            int xOffset = (frame.getWidth()-1024)/2;
	            int yOffset = (frame.getHeight()-768)/2;
	            Graphics2D gg2 = (Graphics2D) gg.create(xOffset, yOffset, 1024, 768);
	            //TODO can handle larger sizes by scaling?
	            //gg2.setTransform(AffineTransform.getScaleInstance(sx, sy));
	            
	            //call the game renderer to do the heavy drawing
	            renderer.render(gg2, timeStepStart);
	            
	            //make sure we dispose of the nested grapics every frame
	            gg2.dispose();
	            
	            //flip to next buffer
	            bufferStrategy.show();
	        } finally {
		        //ensure cleanup
		        if (g != null) {
		        	g.dispose();
		        	g = null;
		        }
	        }
			
	        //TODO maybe sleep?
	        
		}
	}
	
}
