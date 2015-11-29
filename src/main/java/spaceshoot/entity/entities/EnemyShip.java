package spaceshoot.entity.entities;

import spaceshoot.entity.EntitySystemManager;
import spaceshoot.entity.facets.Move2DInt;
import spaceshoot.entity.facets.Renderable;

public class EnemyShip implements Renderable, Move2DInt {

	
	protected String imageName;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected int pastX;
	protected int pastY;
	protected int futureX;
	protected int futureY;
	protected int speedX;
	protected int speedY;
	
	private EnemyShip() {
		
	}
	
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getLeft() {
		return x-getWidth()/2;
	}

	@Override
	public int getRight() {
		return x+getWidth()/2;
	}

	@Override
	public int getTop() {
		return y-getHeight()/2;
	}

	@Override
	public int getBottom() {
		return y+getHeight()/2;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public void setX(int x) {
		this.x = x;		
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	public int getPastX() {
		return pastX;
	}

	public void setPastX(int pastX) {
		this.pastX = pastX;
	}

	public int getPastY() {
		return pastY;
	}

	public void setPastY(int pastY) {
		this.pastY = pastY;
	}

	public int getFutureX() {
		return futureX;
	}

	public void setFutureX(int futureX) {
		this.futureX = futureX;
	}

	public int getFutureY() {
		return futureY;
	}

	public void setFutureY(int futureY) {
		this.futureY = futureY;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	@Override
	public String getImageName() {
		return imageName;
	}

	public static EnemyShip create(EntitySystemManager entitySystemManager, String imageName, int x, int y, int width, int height, int speedX, int speedY) {
		EnemyShip ship = new EnemyShip();
		ship.imageName = imageName;
		ship.x = x;
		ship.y = y;
		ship.pastX = x-speedX;
		ship.pastY = y-speedY;
		ship.futureX = x+speedX;
		ship.futureY = x+speedY;
		ship.width = width;
		ship.height = height;
		ship.speedX = speedX;
		ship.speedY = speedY;
		entitySystemManager.registerEntitiy(ship);
		return ship;
	}
	
}
