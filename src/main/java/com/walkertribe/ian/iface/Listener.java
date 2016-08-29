package com.walkertribe.ian.iface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation that marks methods which are to be invoked when certain events
 * occur. To be eligible to be a Listener, a method must: 1) be public, 2) have
 * a void return type, and 3) have exactly one argument of an accepted type (or
 * any of their subtypes). The accepted types are:
 * </p>
 * <ul>
 * <li>{@link com.walkertribe.ian.protocol.ArtemisPacket ArtemisPacket}</li>
 * <li>{@link com.walkertribe.ian.world.ArtemisObject ArtemisObject}</li>
 * <li>{@link com.walkertribe.ian.iface.ConnectionEvent ConnectionEvent}</li>
 * </ul>
 * <p>
 * Annotating the method alone is not enough to get notifications; you must
 * register the object that has the annotated method with the
 * ArtemisNetworkInterface implementation that will be receiving the packets or
 * events.
 * </p>
 * <p>
 * The Listener annotation is inherited; classes which override a superclass's
 * listener method need not use the Listener annotation themselves, although
 * there is no harm in doing so.
 * </p>
 * @author rjwut
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Listener {
	// no properties
}