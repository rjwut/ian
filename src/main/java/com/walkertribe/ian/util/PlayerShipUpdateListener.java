package com.walkertribe.ian.util;

import com.walkertribe.ian.iface.DisconnectEvent;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.protocol.core.GameOverPacket;
import com.walkertribe.ian.world.Artemis;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * <p>
 * Convenience class for listening for updates to a particular player ship,
 * indicated by ship number. The ship number is usually omitted from updates,
 * but the first update will always specify it. This class automates the
 * tactic of noting the ID of the ship with the given number, and using that to
 * identify the ship from then on. When an update for the specific ship is
 * received, the onShipUpdate() method is invoked.
 * </p>
 * <p>
 * To use, extend the class and implement onShipUpdate(), then pass an instance
 * of your subclass into ArtemisNetworkInterface.addListener().
 * </p>
 * @author rjwut
 */
public abstract class PlayerShipUpdateListener {
	/**
	 * Invoked whenever an update for the desired ship is received.
	 */
	public abstract void onShipUpdate(ArtemisPlayer player);

	private int number;
	private int id = -1;

	/**
	 * Creates a PlayerShipUpdateListener that listens for updates to the ship with the given
	 * number. (For example, by default, Artemis is 1.)
	 */
	public PlayerShipUpdateListener(int number) {
		if (number < 1 || number > Artemis.SHIP_COUNT) {
			throw new IllegalArgumentException("Invalid ship number: " + number);
		}

		this.number = number;
	}

	/**
	 * We've gotten an updated ArtemisPlayer; check to see if it's the one we want. If so, invoke
	 * onShipUpdate().
	 */
	@Listener
	public final void onPlayerObjectUpdated(ArtemisPlayer player) {
		if (id == -1) {
			// We don't know the ship's ID yet
			int curNumber = player.getShipNumber();

			if (curNumber == -1 || curNumber != number) {
				return; // this isn't the one we want
			}

			// We found it; record the ID
			id = player.getId();
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
        id = -1; // ship will probably have a different ID next game
	}

	@Listener
	public void onDisconnect(DisconnectEvent event) {
        id = -1; // ship will probably have a different ID next game
	}

	public int getNumber() {
		return number;
	}
}