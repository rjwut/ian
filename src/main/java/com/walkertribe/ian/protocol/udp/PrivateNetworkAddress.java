package com.walkertribe.ian.protocol.udp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * A class which contains all the information needed to perform and respond to
 * UDP server discovery broadcasts.
 * @author rjwut
 */
public class PrivateNetworkAddress implements Comparable<PrivateNetworkAddress> {
	/**
	 * Returns a PrivateNetworkAddress believed to represent the best one to
	 * represent this machine on the LAN, or null if none can be found.
	 */
	public static PrivateNetworkAddress guessBest() throws IOException {
		List<PrivateNetworkAddress> all = findAll();
		return all.isEmpty() ? null : all.get(0);
	}

	/**
	 * Returns a prioritized list of PrivateNetworkAddress objects.
	 */
	public static List<PrivateNetworkAddress> findAll() throws IOException {
		List<PrivateNetworkAddress> list = new ArrayList<PrivateNetworkAddress>();
		Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
		int ifaceIndex = -1;

		while (ifaces.hasMoreElements()) {
			NetworkInterface iface = ifaces.nextElement();
			ifaceIndex++;

			if (iface.isLoopback() || !iface.isUp()) {
				continue; // we don't want loopback interfaces or interfaces that are down
			}

			List<InterfaceAddress> ifaceAddrs = iface.getInterfaceAddresses(); 
			int len = ifaceAddrs.size();

			for (int addrIndex = 0; addrIndex < len; addrIndex++) {
				InterfaceAddress ifaceAddr = ifaceAddrs.get(addrIndex);
				InetAddress addr = ifaceAddr.getAddress();
				PrivateNetworkType type = PrivateNetworkType.from(addr.getAddress());

				if (type != null) {
					list.add(new PrivateNetworkAddress(iface, ifaceAddr, type, ifaceIndex, addrIndex));
				}
			}
		}

		return list;
	}

	private NetworkInterface iface;
	private InterfaceAddress addr;
	private String hostName;
	private PrivateNetworkType type;
	private int ifaceIndex;
	private int addrIndex;

	private PrivateNetworkAddress(NetworkInterface iface, InterfaceAddress addr, PrivateNetworkType type,
			int ifaceIndex, int addrIndex) throws IOException {
		this.iface = iface;
		this.addr = addr;
		hostName = InetAddress.getLocalHost().getHostName();
		this.type = type;
		this.ifaceIndex = ifaceIndex;
		this.addrIndex = addrIndex;
	}

	/**
	 * Returns the NetworkInterface to which this address belongs.
	 */
	public NetworkInterface getInterface() {
		return iface;
	}

	/**
	 * Returns the IP address as a String.
	 */
	public String getIp() {
		return addr.getAddress().getHostAddress();
	}

	/**
	 * Returns the InetAddress.
	 */
	public InetAddress getInetAddress() {
		return addr.getAddress();
	}

	/**
	 * Returns the broadcast address.
	 */
	public InetAddress getBroadcastAddress() {
		return addr.getBroadcast();
	}

	/**
	 * Returns the host name.
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * Returns true if this network interface is wifi.
	 */
	public boolean isWifi() {
		return iface.getName().startsWith("wlan");
	}

	@Override
	public String toString() {
		return addr.getAddress().getHostAddress() + " - " + iface.getDisplayName() + " [" + iface.getName() + "]";
	}

	@Override
	public int compareTo(PrivateNetworkAddress that) {
		boolean thisWifi = isWifi();
		boolean thatWifi = that.isWifi();

		if (thisWifi != thatWifi) {
			return thisWifi ? 1 : -1; // prefer non-wifi connections
		}

		int c = type.ordinal() - that.type.ordinal(); // prefer higher-class types

		if (c == 0) {
			c = ifaceIndex - that.ifaceIndex; // prefer earlier-listed interfaces
			// Note: iface.getIndex() would work, but isn't available before Android API level 19
		}

		if (c == 0) {
			c = addrIndex - that.addrIndex; // prefer earlier-listed addresses
		}

		return c;
	}
}
