package com.walkertribe.ian.enums;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.vesseldata.Faction;
import com.walkertribe.ian.vesseldata.Vessel;
import com.walkertribe.ian.world.ArtemisNpc;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * The types of ArtemisObjects to which players can send COMMs messages.
 * @author rjwut
 */
public enum CommsRecipientType {
	/**
	 * Other player ships
	 */
	PLAYER {
		@Override
		public CommsMessage messageFromId(int id) {
			return PlayerMessage.values()[id];
		}
	},
	/**
	 * NCP enemy ships
	 */
	ENEMY {
		@Override
		public CommsMessage messageFromId(int id) {
			return EnemyMessage.values()[id];
		}
	},
	/**
	 * Bases
	 */
	BASE {
		@Override
		public CommsMessage messageFromId(int id) {
			return BaseMessage.values()[id];
		}
	},
	/**
	 * Other (civilian NPCs)
	 */
	OTHER {
		@Override
		public CommsMessage messageFromId(int id) {
			return OtherMessage.fromId(id);
		}
	};

	/**
	 * Returns the CommsRecipientType that corresponds to the given ArtemisObject;
	 * or null if the object in question cannot receive COMMs messages. In the
	 * case of an NPC, the CommsRecipient type might be ENEMY or OTHER; which one
	 * is determined by checking to see if they are on the same side as the
	 * player. In the rare case that one or both of the sides are unknown, it will
	 * fall back on using the Context to retrieve the NPC's faction and checking
	 * see if it has the ENEMY attribute. If neither method works, fromObject()
	 * throw an IllegalStateException.
	 */
	public static CommsRecipientType fromObject(ArtemisPlayer sender, ArtemisObject recipient, Context ctx) {
		ObjectType type = recipient.getType();

		switch (type) {
		case PLAYER_SHIP:
			return PLAYER;
		case BASE:
			return BASE;
		case NPC_SHIP:
			ArtemisNpc npc = (ArtemisNpc) recipient;
			Boolean enemy = null;
			byte senderSide = sender.getSide();
			byte recipientSide = npc.getSide();

			if (senderSide != -1 && recipientSide != -1) {
			    enemy = senderSide != recipientSide;
			} else if (ctx != null) {
	            Vessel vessel = npc.getVessel(ctx);
	            Faction faction = vessel.getFaction();

	            if (faction != null) {
	                enemy = faction.is(FactionAttribute.ENEMY);
	            }
			}

			if (enemy == null) {
			    throw new IllegalStateException("Cannot determine if recipient is enemy");
			}

			return enemy ? ENEMY : OTHER;
		default:
			return null;
		}
	}

	/**
	 * Returns the CommsMessage value that corresponds to the given message ID.
	 */
	public abstract CommsMessage messageFromId(int id);
}
