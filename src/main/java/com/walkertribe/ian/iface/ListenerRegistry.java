package com.walkertribe.ian.iface;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Contains ListenerMethods to be invoked when a corresponding event occurs.
 * @author rjwut
 */
public class ListenerRegistry {
	private List<ListenerMethod> listeners = new CopyOnWriteArrayList<ListenerMethod>();

    /**
     * Registers all methods on the given Object which have the Listener
     * annotation with the registry.
     */
    public void register(Object object) {
		Method[] methods = object.getClass().getMethods();
		
		for (Method method : methods) {
			if (method.getAnnotation(Listener.class) != null) {
				listeners.add(new ListenerMethod(object, method));
			}
		}
    }

    /**
     * Returns a List containing all the ListenerMethods which are interested in
     * objects of the given Class.
     */
    public List<ListenerMethod> listeningFor(Class<?> clazz) {
		List<ListenerMethod> interested = new LinkedList<ListenerMethod>();

		for (ListenerMethod listener : listeners) {
			if (listener.accepts(clazz)) {
				interested.add(listener);
			}
		}

		return interested;
    }

    /**
     * Notifies interested listeners about this event.
     */
    public void fire(ConnectionEvent event) {
		for (ListenerMethod listener : listeners) {
			listener.offer(event);
		}
    }
}