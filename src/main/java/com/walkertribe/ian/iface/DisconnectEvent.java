package com.walkertribe.ian.iface;

/**
 * An event that gets thrown when an existing connection to a remote machine is
 * lost. The {@link #cause} property indicates why the connection was lost. If
 * there is an exception that further explains the event, it is provided in the
 * {@link #exception} property; otherwise, it will be null.
 * @author rjwut
 */
public class DisconnectEvent extends ConnectionEvent {
	/**
	 * Indicates the reason that the connection was terminated.
	 */
	public enum Cause {
		/**
		 * The connection was closed from this side; in other words, the user
		 * terminated the connection intentionally.
		 */
		LOCAL_DISCONNECT(true),

		/**
		 * The connection was closed from the remote side. This could be because
		 * the remote machine terminated the connection intentionally, or a
		 * network problem caused the connection to drop.
		 */
		REMOTE_DISCONNECT(true),

		/**
		 * IAN encountered an error from which it could not recover while
		 * attempting to parse a packet. This would typically be caused by a
		 * bug in IAN or in a protocol extension being used by a custom proxy.
		 */
		PACKET_PARSE_EXCEPTION(false),

		/**
		 * An I/O exception occurred. The {@link #exception} property may have
		 * more information, but this is generally an external problem that
		 * IAN can't do anything about.
		 */
		IO_EXCEPTION(false),

		/**
		 * The server version in use is not supported by IAN.
		 */
		UNSUPPORTED_SERVER_VERSION(false);

		private boolean normal;

		private Cause(boolean normal) {
			this.normal = normal;
		}
	}

	private Cause cause;
	private Exception exception;

	DisconnectEvent(Cause cause, Exception exception) {
		this.cause = cause;
		this.exception = exception;
	}

	/**
	 * Returns a Cause value describing the reason the connection was
	 * terminated.
	 */
	public Cause getCause() {
		return cause;
	}

	/**
	 * Returns true if this DisconnectEvent is one that would be expected to
	 * occur when the connection is intentionally terminated. Note that this
	 * doesn't guarantee that the disconnection was intentional, just that
	 * it could have been. 
	 */
	public boolean isNormal() {
		return cause.normal;
	}

	/**
	 * Returns the Exception that was thrown as a result of the connection
	 * termination, if any.
	 */
	public Exception getException() {
		return exception;
	}

	@Override
	public String toString() {
		return "Disconnected: " + cause;
	}
}