package com.walkertribe.ian.protocol.core.world;

import java.util.ArrayList;
import java.util.List;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;
import com.walkertribe.ian.util.TestUtil;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * An AbstractPacketTester subclass adapted for testing objects stored in
 * ObjectUpdatePackets.
 * @author rjwut
 */
public abstract class AbstractObjectUpdatePacketTester<T extends ArtemisObject> extends AbstractPacketTester<ObjectUpdatePacket> {
	/**
	 * Invoked by AbstractObjectUpdatePacketTester when it has successfully
	 * parsed the desired number of packets with no bytes left over. The
	 * objects stored in these packets are extracted and aggregated into a
	 * single list of objects, which is subsequently passed into this method;
	 * subclasses should evaluate them to ensure that they contain the expected
	 * data and throw an assert if not.
	 */
	protected abstract void testObjects(List<T> objects);

	/**
	 * Covers the valueOf() method for any inner enums for the declared Class,
	 * then locates the corresponding test packet file and invokes
	 * {@link #execute(String, Origin, int)}.
	 */
	@SuppressWarnings("unchecked")
	protected void execute(Class<? extends ObjectParser> parserClass, int packetCount) {
		Class<?>[] innerClasses = parserClass.getDeclaredClasses();

		// Cover the inner Bit enum (and any other inner enums we find)
		for (Class<?> innerClass : innerClasses) {
			if (innerClass.isEnum()) {
				TestUtil.coverEnumValueOf((Class<? extends Enum<?>>) innerClass);
			}
		}

		execute(
				"core/world/ObjectUpdatePacket-" + parserClass.getSimpleName() + ".txt",
				Origin.SERVER,
				packetCount
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected final void testPackets(List<ObjectUpdatePacket> packets) {
		List<T> objects = new ArrayList<T>();

		for (ObjectUpdatePacket pkt : packets) {
			objects.addAll((List<T>) pkt.getObjects());
		}

		testObjects(objects);
	}
}
