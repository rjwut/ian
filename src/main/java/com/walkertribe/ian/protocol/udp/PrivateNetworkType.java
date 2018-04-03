package com.walkertribe.ian.protocol.udp;

/**
 * IPv4 private network types. Used to determine whether an address belongs
 * to a private network.
 * @author rjwut
 */
public enum PrivateNetworkType {
	CLASS_A {
		@Override
		protected boolean match(byte[] addr) {
			return addr.length == 4 && addr[0] == 10;
		}
	},
	CLASS_B {
		@Override
		protected boolean match(byte[] addr) {
			return addr.length == 4 && addr[0] == 172 && addr[1] > 15 && addr[1] < 32;
		}
	},
	CLASS_C {
		@Override
		protected boolean match(byte[] addr) {
			return addr.length == 4 && addr[0] == 192 && addr[1] == 168;
		}
	};

	/**
	 * Returns the private network address type that matches the given
	 * address, or null if it's not a private network address.
	 */
	public static PrivateNetworkType from(byte[] addr) {
		for (PrivateNetworkType type : values()) {
			if (type.match(addr)) {
				return type;
			}
		}

		return null;
	}

	/**
	 * Returns true if the given address matches this private network type.
	 */
	protected abstract boolean match(byte[] addr);
}
