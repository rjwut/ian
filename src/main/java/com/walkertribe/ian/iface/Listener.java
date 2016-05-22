package com.walkertribe.ian.iface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks methods which are to be invoked when a packet is
 * received or an event fires. To be eligible to be a Listener, a method must:
 * 1) be public, 2) have a void return type, and 3) have exactly one argument of
 * an accepted type ({@link com.walkertribe.ian.protocol.ArtemisPacket},
 * {@link com.walkertribe.ian.iface.ConnectionEvent}, or any of their subtypes). The
 * method will only be notified of packets or events that are assignable to the
 * argument's type.
 * 
 * Annotating the method alone is not enough to get notifications; you must
 * register the object that has the annotated method with the
 * ArtemisNetworkInterface implementation that will be receiving the packets or
 * events.
 * 
 * @author rjwut
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Listener {
	// no properties
}