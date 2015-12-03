package spaceshoot;

import java.awt.Rectangle;
import java.sql.Time;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spaceshoot.entity.EntitySystemManager;
import spaceshoot.entity.entities.EnemyShip;
import spaceshoot.entity.systems.MoveSystem;

public class GameCore {
	
	protected final EntitySystemManager entitySystemManager = new EntitySystemManager();
	
	protected final MoveSystem moveSystem;
	
	protected Rectangle playerRect = new Rectangle(50,(768/2)-13 ,26,19 );
	
	protected Long timestampPast;
	protected Long timestampCurrent;
	protected Long timestampFuture;
	public final Long FRAMEINTERVALNANO = 100000000L / 30;

    private Logger log = LoggerFactory.getLogger(getClass());
	
	
	private GameCore() {
		moveSystem = new MoveSystem();
		entitySystemManager.register(moveSystem);
		timestampCurrent = System.nanoTime();
		timestampPast = timestampCurrent - FRAMEINTERVALNANO; //estimate
		timestampFuture = timestampCurrent + FRAMEINTERVALNANO; //estimate
	}
	
	
	public Rectangle getPlayerRect() {
		return playerRect; //TODO return an unmodifiable copy
	}

	public EntitySystemManager getEntitySystemManager() {
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


	/**
	 * Implementation of the Runnable interface
	 * that simulates one simulation step
	 */
	public void run(long timestampSimStart) {
    	long timeStart = System.nanoTime();
    	
		timestampPast = timestampCurrent;
		timestampCurrent = timestampSimStart;
		timestampFuture = timestampSimStart + FRAMEINTERVALNANO; //estimate
		
		moveSystem.processAll();

    	long timeEnd = System.nanoTime();
    	long interval = timeEnd-timeStart;
    	int fps = (int) (1000000000L/interval);
        log.trace("SIM ("+fps+"fps)");
        log.trace("past time    :"+timestampPast);
        log.trace("current time :"+timestampCurrent);
        log.trace("future time  :"+timestampFuture);
	}
	
	public static GameCore create() {
		return new GameCore();
	}

}
