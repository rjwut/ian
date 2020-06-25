package com.walkertribe.ian.protocol.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.AbstractPacketTester;

public class ClientHeartbeatPacketTest extends AbstractPacketTester<ClientHeartbeatPacket> {
    @Test
    public void test() {
        execute("core/ClientHeartbeatPacket.txt", Origin.CLIENT, 1);
    }

    @Test
    public void testConstruct() {
        new ClientHeartbeatPacket();
    }

    @Override
    protected void testPackets(List<ClientHeartbeatPacket> packets) {
        Assert.assertNotNull(packets.get(0));
    }
}
