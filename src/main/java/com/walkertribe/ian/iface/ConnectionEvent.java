package com.walkertribe.ian.iface;

import java.util.Date;

/**
 * An event regarding the connection to a remote machine. Typically, you would
 * work with a subclass, not this class.
 * @author rjwut
 */
public abstract class ConnectionEvent {
	private long timestamp = System.currentTimeMillis();

	/**
	 * The time this event occurred.
	 */
	public Date getTime() {
		return new Date(timestamp);
	}
}