package spaceshoot.entity;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntitySystem<T> {

	protected List<T> entities = new ArrayList<>();
	
	/**
	 * This will add the provided entity to this systems list of known
	 * entities, ignoring any duplication.
	 * @param t
	 */
	public void register(T t) {
		if (!entities.contains(t)) {
			entities.add(t);
		}
	}
	
	public void deregister(T t) {
		if (entities.contains(t)) {
			entities.remove(t);
		}
	}
	
	/**
	 * This is a type-safe register of generic Object types
	 * It will test if object is an instance of the accepted
	 * facet interface, and if so, will register it via register(T t) 
	 * @param object
	 */
	public void registerObject(Object object) {
		if (getFacetClass().isInstance(object)) {
			register(getFacetClass().cast(object));
		}
	}
	
	/**
	 * This is a type-safe deregister of generic Object types
	 * It will test if object is an instance of the accepted
	 * facet interface, and if so, will deregister it via deregister(T t) 
	 * @param object
	 */
	public void deregisterObject(Object object) {
		if (getFacetClass().isInstance(object)) {
			deregister(getFacetClass().cast(object));
		}
	}
		
	public void processAll() {
		List<T> deleted = new ArrayList<>();
		for (T t : entities) {
			process(t);
		}
		for (T t : deleted) {
			deregister(t);
		}
	}
	
	public abstract void process(T t);
	
	public abstract Class<T> getFacetClass();
	
}
