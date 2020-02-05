package com.walkertribe.ian.protocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.walkertribe.ian.enums.Origin;

/**
 * Annotation for packet classes. This annotation is not inherited.
 * @author rjwut
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Packet {
	/**
	 * Whether the packet originates from the server or the client. This value
	 * is required.
	 */
	Origin origin();

	/**
	 * The packet type, given as a string, which is then JamCRC hashed. You
	 * must specify either this or hash.
	 */
	String type() default "";

	/**
	 * The packet type, given as a JamCRC hash value. You must specify either
	 * this or type.
	 */
	int hash() default 0;

	/**
	 * Optional packet subtype values. If you specify more than one subtype,
	 * this class will parse packets with any of the given subtypes.
	 */
	byte[] subtype() default {};
}
