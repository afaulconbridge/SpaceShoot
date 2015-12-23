package spaceshoot;

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

public class SpaceShootMain {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) {
		new SpaceShootMain().run();
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
						

		Thread threadSim = new Thread(new SimRunnable(core));
		threadSim.start();
		
		Thread threadRenderer = new Thread(new RendererRunnable(frame, renderer));
		threadRenderer.start();
	}
	
	protected class SimRunnable implements Runnable {

		private final GameCore core;
		
		public SimRunnable(GameCore core) {
			this.core = core;
		}
		
		@Override
		public void run() {
			while (true) {
				long timeStepStart = System.nanoTime();
				core.run(timeStepStart);
				long timeStepEnd = System.nanoTime();
				int intervalMs = (int)((timeStepEnd-timeStepStart) / 1000);
				log.info("Sim interval "+intervalMs+"ms");
				int sleep = 100-intervalMs;
				if (sleep > 0) {
					try {
						Thread.sleep(100-intervalMs);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (RuntimeException e) {
						throw e;
					}
				}
			}
		}
	}
	
	protected class RendererRunnable implements Runnable {
		
		private final JFrame frame;
		private final GameRenderer renderer;
		
		public RendererRunnable(JFrame frame, GameRenderer renderer) {
			this.frame = frame;
			this.renderer = renderer;
		}

		private void render(long timeStepStart) {
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
	            
	            //make sure we dispose of the nested graphics every frame
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
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				long timeStepStart = System.nanoTime();
				render(timeStepStart);
				long timeStepEnd = System.nanoTime();
				long intervalMs = (timeStepEnd-timeStepStart) / 1000;
				log.info("Render interval "+intervalMs+"ms");
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RuntimeException e) {
					throw e;
				}
			}			
		}
		
	}
}
