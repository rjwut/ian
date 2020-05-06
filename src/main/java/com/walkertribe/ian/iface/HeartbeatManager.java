package com.walkertribe.ian.iface;

import com.walkertribe.ian.protocol.core.HeartbeatPacket;

/**
 * Class responsible for tracking and sending HeartbeatPackets.
 * @author rjwut
 */
class HeartbeatManager {
    private static final long HEARTBEAT_SEND_INTERVAL_MS = 3_000;
    private static final long HEARTBEAT_TIMEOUT_MS = 15_000;

    private ThreadedArtemisNetworkInterface iface;
    private long mStartTime = System.currentTimeMillis();
    private long mLastHeartbeatReceivedTime = -1;
    private long mLastHeartbeatSentTime = -1;
    private boolean mLost = false;
    private boolean mAutoSendHeartbeat = true;

    HeartbeatManager(ThreadedArtemisNetworkInterface iface) {
        this.iface = iface;
    }

    /**
     * Sets whether the HeartbeatManager should send HeartbeatPackets or not.
     */
    void setAutoSendHeartbeat(boolean autoSendHeartbeat) {
        mAutoSendHeartbeat = autoSendHeartbeat;
    }

    /**
     * Invoked when a HeartbeatPacket is received from the remote machine.
     */
    void onHeartbeat(HeartbeatPacket pkt) {
        mLastHeartbeatReceivedTime = pkt.getTimestamp();

        if (mLost) {
            mLost = false;
            iface.dispatch(new HeartbeatRegainedEvent());
        }
    }

    /**
     * Checks to see if we need to send a HeartbeatLostEvent, and sends it if needed.
     */
    void checkForHeartbeat() {
        if (mLost) {
            return;
        }

        long fromTime = mLastHeartbeatReceivedTime == -1 ? mStartTime : mLastHeartbeatReceivedTime;
        long elapsed = System.currentTimeMillis() - fromTime;

        if (elapsed >= HEARTBEAT_TIMEOUT_MS) {
            mLost = true;
            iface.dispatch(new HeartbeatLostEvent());
        }
    }

    /**
     * Determines whether enough time has elapsed that we need to send a HeartbeatPacket, and sends
     * it if needed. Does nothing if autoSendHeartbeat is set to false.
     */
    void sendHeartbeatIfNeeded() {
        if (!mAutoSendHeartbeat) {
            return;
        }

        long now = System.currentTimeMillis();

        if (now - mLastHeartbeatSentTime >= HEARTBEAT_SEND_INTERVAL_MS) {
            iface.send(iface.getSendType().createHeartbeat());
            mLastHeartbeatSentTime = now;
        }
    }

    /**
     * Returns the number of milliseconds since the last HeartbeatPacket was received, or null if
     * one has not been received yet.
     */
    Long getLastHeartbeat() {
        if (mLastHeartbeatReceivedTime == -1) {
            return null;
        }

        return System.currentTimeMillis() - mLastHeartbeatReceivedTime;
    }
}
