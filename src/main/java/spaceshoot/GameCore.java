package spaceshoot;

import java.awt.Rectangle;
import java.sql.Time;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import spaceshoot.entity.EntitySystemManager;
import spaceshoot.entity.entities.EnemyShip;
import spaceshoot.entity.systems.MoveSystem;

public class GameCore implements Runnable, AutoCloseable {
	
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	
	protected final EntitySystemManager entitySystemManager = new EntitySystemManager();
	
	protected final MoveSystem moveSystem;
	
	protected Rectangle playerRect = new Rectangle(50,(768/2)-13 ,26,19 );
	
	protected Long timestampPast;
	protected Long timestampCurrent;
	protected Long timestampFuture;
	
	
	private GameCore() {
		moveSystem = new MoveSystem();
		entitySystemManager.register(moveSystem);
		timestampCurrent = System.nanoTime();
		timestampPast = timestampCurrent - 1000000/30; //estimate
		timestampFuture = timestampCurrent + 1000000/30; //estimate
	}
	
	
	public Rectangle getPlayerRect() {
		return playerRect; //TODO return an unmodifiable copy
	}

	public EntitySystemManager getSystemManager() {
		return entitySystemManager;
	}

	public Long getTimestampPast() {
		return timestampPast;
	}


	public Long getTimestampCurrent() {
		return timestampCurrent;
	}


	public Long getTimestampFuture() {
		return timestampFuture;
	}

	public ScheduledExecutorService getExecutor() {
		return executor;
	}


	/**
	 * Implementation of the Runnable interface
	 * that simulates one simulation step
	 */
	public void run() {
		timestampPast = timestampCurrent;
		timestampCurrent = System.nanoTime();
		timestampFuture = timestampCurrent + 1000000/30; //estimate
		
		moveSystem.processAll();

        System.out.println("SIM");
	}

	
	public void start() {
		timestampPast = System.nanoTime();
		

		//create some dummy enemies for testing
		EnemyShip.create(entitySystemManager, "baddie", 500,50, 27,21 , -1, 0);
		
		//hardcode 30 updates per second for now
		//these can't overlap if they overrun
		executor.scheduleAtFixedRate(this, 0, 1000/30, TimeUnit.MILLISECONDS);
	}
	
	public void close() {
		//Force shutdown now of child threads
		executor.shutdownNow();
	}
	
	
	public static GameCore create() {
		return new GameCore();
	}

}
