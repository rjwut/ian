package com.walkertribe.ian.protocol.udp;

/**
 * IPv4 private network types. Used to determine whether an address belongs
 * to a private network.
 * @author rjwut
 */
public enum PrivateNetworkType {
	TWENTY_FOUR_BIT_BLOCK { // 10.x.x.x
		@Override
		protected boolean match(byte[] addr) {
			return addr.length == 4 && addr[0] == 10;
		}
	},
	TWENTY_BIT_BLOCK { // 172.16.x.x - 172.31.x.x
		@Override
		protected boolean match(byte[] addr) {
			return addr.length == 4 && addr[0] == -44 && addr[1] > 15 && addr[1] < 32;
		}
	},
	SIXTEEN_BIT_BLOCK { // 192.168.x.x
		@Override
		protected boolean match(byte[] addr) {
			return addr.length == 4 && addr[0] == -64 && addr[1] == -88;
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
