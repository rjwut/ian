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
	Origin origin();
	String type() default "";
	int hash() default 0;
	byte[] subtype() default {};
}
