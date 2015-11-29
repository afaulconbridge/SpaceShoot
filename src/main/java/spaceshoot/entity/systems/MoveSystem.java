package spaceshoot.entity.systems;

import spaceshoot.entity.AbstractEntitySystem;
import spaceshoot.entity.facets.Move2DInt;

public class MoveSystem extends AbstractEntitySystem<Move2DInt> {

	@Override
	public void process(Move2DInt t) {
		t.setPastX(t.getX());
		t.setPastY(t.getY());
		t.setX(t.getFutureX());
		t.setY(t.getFutureY());
		t.setFutureX(t.getFutureX()+t.getSpeedX());
		t.setFutureY(t.getFutureY()+t.getSpeedY());
	}

	@Override
	public Class<Move2DInt> getFacetClass() {
		return Move2DInt.class;
	}

}
