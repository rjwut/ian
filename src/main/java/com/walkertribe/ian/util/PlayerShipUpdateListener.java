package com.walkertribe.ian.util;

import com.walkertribe.ian.iface.DisconnectEvent;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.protocol.core.GameOverPacket;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * Convenience class for listening for updates to a particular player ship,
 * indicated by ship index. The ship index is usually omitted from updates, but
 * the first update will always specify it. This class automates the tactic of
 * noting the ID of the ship with the given index, and using that to identify
 * the ship from then on. When an update for the specific ship is received, the
 * onShipUpdate() method is invoked.
 * 
 * To use, extend the class and implement onShipUpdate(), then pass an instance
 * of your subclass into ArtemisNetworkInterface.addListener().
 * 
 * @author rjwut
 */
public abstract class PlayerShipUpdateListener {
	public abstract void onShipUpdate(ArtemisPlayer player);

	private int index;
	private int id;
	private boolean found = false;

	public PlayerShipUpdateListener(int index) {
		this.index = index;
	}

	@Listener
	public final void onPlayerObjectUpdated(ArtemisPlayer player) {
		if (!found) {
			synchronized (this) {
				// We don't know the ship's ID yet
				int curIndex = player.getShipNumber();

				if (curIndex == -1 || curIndex != index) {
					return; // this isn't the one we want
				}

				// We found it; record the ID
				id = player.getId();
				found = true;
			}
		} else {
			// We know the ID, so just check for that
			if (player.getId() != id) {
				return; // this isn't the one we want
			}
		}

		// If we got here, this is the ship we want
		onShipUpdate(player);
	}

	@Listener
	public void onGameOver(GameOverPacket pkt) {
        found = false; // ship will probably have a different ID next game
	}

	@Listener
	public void onDisconnect(DisconnectEvent event) {
        found = false; // ship will probably have a different ID next game
	}

	public int getIndex() {
		return index;
	}
}