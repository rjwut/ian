package com.walkertribe.ian.enums;

import com.walkertribe.ian.world.ArtemisObject;

/**
 * The type of intel in an IntelPacket.
 * @author rjwut
 */
public enum IntelType {
	RACE {
		@Override
		public CharSequence get(ArtemisObject obj) {
			return obj.getRace();
		}

		@Override
		public void set(ArtemisObject obj, CharSequence str) {
			obj.setRace(str);
		}
	},
	CLASS {
		@Override
		public CharSequence get(ArtemisObject obj) {
			return obj.getArtemisClass();
		}

		@Override
		public void set(ArtemisObject obj, CharSequence str) {
			obj.setArtemisClass(str);
		}
	},
	LEVEL_1_SCAN {
		@Override
		public CharSequence get(ArtemisObject obj) {
			return obj.getIntelLevel1();
		}

		@Override
		public void set(ArtemisObject obj, CharSequence str) {
			obj.setIntelLevel1(str);
		}
	},
	LEVEL_2_SCAN {
		@Override
		public CharSequence get(ArtemisObject obj) {
			return obj.getIntelLevel2();
		}

		@Override
		public void set(ArtemisObject obj, CharSequence str) {
			obj.setIntelLevel2(str);
		}
	};

	public abstract CharSequence get(ArtemisObject obj);
	public abstract void set(ArtemisObject obj, CharSequence str);
}
