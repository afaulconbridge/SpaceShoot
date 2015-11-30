package spaceshoot;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MainFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = -5924241627796297645L;

	protected final GraphicsDevice device;
	protected final GraphicsConfiguration gc;
	
	protected GameCore core;
	protected GameRenderer renderer;
	
	private boolean setupDone = false;
	
	public MainFrame(GraphicsDevice device, GraphicsConfiguration gc) {
		super(gc);
		
		this.device = device;
		this.gc = gc;
	}

    public static void main(String[] args) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration gc = device.getDefaultConfiguration();
        
        MainFrame frame = new MainFrame(device, gc);
        frame.setTitle("SpaceShoot");
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);//we run at a regular redraw, so ignore repaint requests outside of that
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        device.setFullScreenWindow(frame); //will also make visible
        
        if (frame.getSize().width < 1024 || frame.getSize().height < 768) {
        	throw new RuntimeException("Must be at least 1024x768 resolution");
        }

        frame.createBufferStrategy(2);

        frame.core = GameCore.create();
		frame.renderer = new GameRenderer(frame.core);
		
        frame.core.start();
        
        EventQueue.invokeLater(frame);
    }
    
    public void run() {
    	long timeStart = System.nanoTime();
    	//System.out.println("MainFrame.run()");

        BufferStrategy bufferStrategy = this.getBufferStrategy();
        Graphics g = null;
        try {
        	g = bufferStrategy.getDrawGraphics();
            Graphics2D gg = (Graphics2D) g;
            
            //now do some background drawing
            gg.setColor(Color.black);
            gg.fillRect(0, 0, this.getWidth(), this.getHeight());
            
            //create a new graphics object for the game renderer to handle
            //this is fixed size
            int xOffset = (this.getWidth()-1024)/2;
            int yOffset = (this.getHeight()-768)/2;
            Graphics2D gg2 = (Graphics2D) gg.create(xOffset, yOffset, 1024, 768);
            //TODO can handle larger sizes by scaling?
            //gg2.setTransform(AffineTransform.getScaleInstance(sx, sy));
            
            //call the game renderer to do the heavy drawing
            renderer.render(gg2, core.getTimestampPast(), core.getTimestampCurrent());
            
            //flip to next buffer
            bufferStrategy.show();
        } finally {
	        //ensure cleanup
	        if (g != null) {
	        	g.dispose();
	        	g = null;
	        }
        }

		core.getExecutor().execute(this);

    	long timeEnd = System.nanoTime();
    	long interval = timeEnd-timeStart;
    	int fps = (int) (1000000000/interval);
        System.out.println("FRAME ("+fps+"fps)");
    }
}
