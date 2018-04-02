package com.walkertribe.ian.protocol.udp;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PrivateNetworkAddress implements Comparable<PrivateNetworkAddress> {
	/**
	 * Returns a PrivateNetworkAddress believed to represent the best one to
	 * represent this machine on the LAN, or null if none can be found.
	 */
	public static PrivateNetworkAddress guessBestPrivateNetworkAddress() throws SocketException {
		List<PrivateNetworkAddress> all = findAll();
		return all.isEmpty() ? null : all.get(0);
	}

	/**
	 * Returns a prioritized list of PrivateNetworkAddress objects.
	 */
	public static List<PrivateNetworkAddress> findAll() throws SocketException {
		List<PrivateNetworkAddress> list = new ArrayList<PrivateNetworkAddress>();
		Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();

		while (ifaces.hasMoreElements()) {
			NetworkInterface iface = ifaces.nextElement();

			if (iface.isLoopback() || !iface.isUp()) {
				continue; // we don't want loopback interfaces or interfaces that are down
			}

			List<InterfaceAddress> ifaceAddrs = iface.getInterfaceAddresses(); 
			int len = ifaceAddrs.size();

			for (int i = 0; i < len; i++) {
				InterfaceAddress ifaceAddr = ifaceAddrs.get(i);
				InetAddress addr = ifaceAddr.getAddress();
				PrivateNetworkType type = PrivateNetworkType.from(addr.getAddress());

				if (type != null) {
					list.add(new PrivateNetworkAddress(iface, ifaceAddr, type, i));
				}
			}
		}

		return list;
	}

	private NetworkInterface iface;
	private InterfaceAddress addr;
	private PrivateNetworkType type;
	private int addrIndex;

	private PrivateNetworkAddress(NetworkInterface iface, InterfaceAddress addr, PrivateNetworkType type, int addrIndex) {
		this.iface = iface;
		this.addr = addr;
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
		return addr.getAddress().getHostName();
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
			c = iface.getIndex() - that.iface.getIndex(); // prefer earlier-listed interfaces
		}

		if (c == 0) {
			c = addrIndex - that.addrIndex; // prefer earlier-listed addresses
		}

		return c;
	}
}
