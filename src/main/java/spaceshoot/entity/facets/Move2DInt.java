package spaceshoot.entity.facets;

public interface Move2DInt extends Position2DInt {

	public int getPastX();
	public void setPastX(int x);
	public int getPastY();
	public void setPastY(int y);

	public int getFutureX();
	public void setFutureX(int x);
	public int getFutureY();
	public void setFutureY(int y);
	
	public int getSpeedX();
	public void setSpeedX(int x);
	public int getSpeedY();
	public void setSpeedY(int y);
	
}
