package spaceshoot.entity;

import java.util.ArrayList;
import java.util.List;

public class EntitySystemManager {

	protected List<AbstractEntitySystem<?>> systems = new ArrayList<>();
	
	public void register(AbstractEntitySystem<?> system){
		//System.out.println("Registering system "+system);
		if (!systems.contains(system)) {
			//System.out.println("Registering new system "+system);
			systems.add(system);
		}
	}
	
	public void deregister(AbstractEntitySystem<?> system){
		if (systems.contains(system)) {
			systems.remove(system);
		}
	}
	
	public void registerEntitiy(Object object) {
		//System.out.println("Attempting registration of "+object);
		for (AbstractEntitySystem<?> system : systems) {
			//System.out.println("Attempting registration with "+system);
			system.registerObject(object);
		}
	}
	
	public void deregisterEntitiy(Object object) {
		for (AbstractEntitySystem<?> system : systems) {
			system.deregisterObject(object);
		}
	}
}
