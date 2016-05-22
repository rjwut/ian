package com.walkertribe.ian.iface;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Contains ListenerMethods to be invoked when a corresponding event occurs.
 * @author rjwut
 */
public class ListenerRegistry {
	private List<ListenerMethod> listeners = new CopyOnWriteArrayList<ListenerMethod>();

    /**
     * Registers all methods on the given Object which have the @Listener
     * annotation with the registry. A listener method must be public, return
     * void, and have exactly one argument which is assignable to ArtemisPacket,
     * ArtemisObject or ConnectionEvent.
     */
    public void register(Object object) {
    	synchronized (listeners) {
			Method[] methods = object.getClass().getMethods();
	
			for (Method method : methods) {
				if (method.getAnnotation(Listener.class) != null) {
					listeners.add(new ListenerMethod(object, method));
				}
			}
    	}
    }

    /**
     * Returns true if any listeners are interested in objects of the given
     * class; false otherwise.
     */
    public boolean listeningFor(Class<?> clazz) {
    	synchronized (listeners) {
			for (ListenerMethod listener : listeners) {
				if (listener.accepts(clazz)) {
					return true;
				}
			}
	
			return false;
    	}
    }

    /**
     * Fires all listeners which are interested in the given object.
     */
    void fire(Object obj) {
    	synchronized (listeners) {
			for (ListenerMethod listener : listeners) {
				listener.offer(obj);
			}
    	}
    }
}