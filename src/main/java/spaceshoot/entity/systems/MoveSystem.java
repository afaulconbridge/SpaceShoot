package spaceshoot.entity.systems;

import spaceshoot.entity.AbstractEntitySystem;
import spaceshoot.entity.facets.Move2DInt;

public class MoveSystem extends AbstractEntitySystem<Move2DInt> {

	protected int limitLeft = 0;
	protected int limitRight = 1024;
	protected int limitTop = 0;
	protected int limitBottom = 768;
	
	
	@Override
	public void process(Move2DInt t) {
		t.setPastX(t.getX());
		t.setPastY(t.getY());
		t.setX(t.getFutureX());
		t.setY(t.getFutureY());
		t.setFutureX(t.getFutureX()+t.getSpeedX());
		t.setFutureY(t.getFutureY()+t.getSpeedY());
		
		//TODO check if moved off-screen, and delete accordingly
	}

	@Override
	public Class<Move2DInt> getFacetClass() {
		return Move2DInt.class;
	}

}
