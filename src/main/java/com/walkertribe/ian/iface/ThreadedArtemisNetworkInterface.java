package com.walkertribe.ian.iface;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.CompositeProtocol;
import com.walkertribe.ian.protocol.Protocol;
import com.walkertribe.ian.protocol.core.CoreArtemisProtocol;
import com.walkertribe.ian.protocol.core.HeartbeatPacket;
import com.walkertribe.ian.protocol.core.setup.VersionPacket;
import com.walkertribe.ian.protocol.core.setup.WelcomePacket;
import com.walkertribe.ian.util.Version;

/**
 * <p>
 * Default implementation of ArtemisNetworkInterface. Kicks off three threads:
 * </p>
 * <ul>
 * <li>
 *   The <b>receiver thread</b>, which reads and parses packets from the input
 *   stream.
 * </li>
 * <li>
 *   The <b>event dispatch thread</b>, which fires listeners to respond to
 *   incoming packets, object updates, or events.
 * </li>
 * <li>
 *   The <b>sender thread</b>, which writes outgoing packets to the output
 *   stream.
 * </li>
 * </ul>
 */
public class ThreadedArtemisNetworkInterface implements ArtemisNetworkInterface {
	private Origin mRecvType;
    private Origin mSendType;
    private Protocol mProtocol = new CoreArtemisProtocol();
    private ListenerRegistry mListeners = new ListenerRegistry();
    private ReceiverThread mReceiveThread;
    private SenderThread mSendThread;
    private EventDispatchThread mDispatchThread;
    private boolean mStarted;
    private DisconnectEvent.Cause mDisconnectCause = DisconnectEvent.Cause.LOCAL_DISCONNECT;
    private Exception mException;
    private Debugger mDebugger = new BaseDebugger();
    private List<ArtemisNetworkInterface> mProxyTargets = new LinkedList<ArtemisNetworkInterface>();
    private Long mLastHeartbeatTimestamp = null;

    /**
     * Prepares an outgoing client connection to an Artemis server. The send and
     * receive streams won't actually be opened until start() is called. This
     * constructor causes IAN to wait forever for a connection; a separate
     * constructor is provided for specifying a timeout.
     */
    public ThreadedArtemisNetworkInterface(String host, int port)
    		throws IOException {
    	this(host, port, 0);
    }

    /**
     * Prepares an outgoing client connection to an Artemis server. The send and
     * receive streams won't actually be opened until start() is called. The
     * timeoutMs value indicates how long (in milliseconds) IAN will wait for
     * the connection to be established before throwing an exception; 0 means
     * "wait forever."
     */
    public ThreadedArtemisNetworkInterface(String host, int port, int timeoutMs) 
            throws IOException {
    	Socket skt = new Socket();
    	skt.connect(new InetSocketAddress(host, port), timeoutMs);
    	init(skt, Origin.SERVER);
    }

    /**
     * Creates a ThreadedArtemisNetworkInterface instance which will communicate
     * over the given Socket.
     * 
     * The Origin indicates the expected type of the remote machine; in other
     * words, it corresponds to the type of packets that are expected to be
     * received. If IAN is connecting as a client to a remote server, origin
     * should be Origin.SERVER. (The constructor which accepts a host and port
     * number simplifies this setup.) If IAN is acting as a proxy server and
     * has accepted a socket connection from a remote client, origin should be
     * Origin.CLIENT.
     * 
     * The send/receive streams won't actually be opened until start() is
     * called.
     */
    public ThreadedArtemisNetworkInterface(Socket skt, Origin origin)
    		throws IOException {
    	init(skt, origin);
    }

    /**
     * Invoked by constructors to perform common initialization.
     */
    private void init(Socket skt, Origin origin) throws IOException {
    	mRecvType = origin;
    	mSendType = origin.opposite();
    	skt.setKeepAlive(true);
        mSendThread = new SenderThread(skt);
        mReceiveThread = new ReceiverThread(skt);
        mDispatchThread = new EventDispatchThread();
    }

    @Override
    public Origin getRecvType() {
    	return mRecvType;
    }

    @Override
    public Origin getSendType() {
    	return mSendType;
    }

    @Override
	public void registerProtocol(Protocol protocol) {
    	if (mProtocol instanceof CompositeProtocol) {
    		((CompositeProtocol) mProtocol).add(protocol);
    	} else {
    		CompositeProtocol composite = new CompositeProtocol();
    		composite.add(mProtocol);
    		composite.add(protocol);
    		mProtocol = composite;
    	}
	}

    @Override
    public void addListener(final Object listener) {
    	mListeners.register(listener);
    }

    @Override
    public void start() {
    	if (!mStarted) {
            mReceiveThread.start();
        	mDispatchThread.start();
            mSendThread.start();
    	}
    }

    @Override
    public boolean isConnected() {
        return mSendThread.mConnected;
    }

    @Override
    public void send(final ArtemisPacket pkt) {
    	if (pkt.getOrigin() != mSendType) {
    		throw new IllegalArgumentException(
    				"Can only send " + mSendType + " packets"
    		);
    	}

    	mSendThread.offer(pkt);
    }

    @Override
    public void stop() {
        mReceiveThread.end();
        mSendThread.end();
    }

    /**
     * Returns the number of milliseconds since the last heartbeat packet was
     * received (or since the connection was established if no heartbeat packet
     * has yet been received). If the interface is not connected,
     * getLastHearbeat() returns null.
     */
    public Long getLastHeartbeat() {
    	return mLastHeartbeatTimestamp == null ? null : System.currentTimeMillis() - mLastHeartbeatTimestamp.longValue();
    }

	@Override
	public void attachDebugger(Debugger debugger) {
		if (debugger == null) {
			debugger = new BaseDebugger();
		}

		mDebugger = debugger;
	}

	@Override
	public void proxyTo(ArtemisNetworkInterface iface) {
		if (!iface.getRecvType().equals(getSendType())) {
			throw new IllegalArgumentException(
					"Interfaces must be of opposite Origins"
			);
		}

		mProxyTargets.add(iface);
	}


    /**
	 * Manages sending packets to the OutputStream.
	 */
	private class SenderThread extends Thread {
        private final Socket mSkt;
        private final Queue<ArtemisPacket> mQueue = new ConcurrentLinkedQueue<ArtemisPacket>();
        private boolean mRunning = true;
        private boolean mDone = false;
        
        private final PacketWriter mWriter;
        
        private boolean mConnected;

        private SenderThread(final Socket skt) throws IOException {
            mSkt = skt;
            OutputStream output = new BufferedOutputStream(mSkt.getOutputStream());
            mWriter = new PacketWriter(output);
        }

        /**
         * Enqueues a packet to be sent.
         */
        private boolean offer(final ArtemisPacket pkt) {
        	return mQueue.offer(pkt);
        }

        @Override
        public void run() {
            while (mRunning) {
                try {
                    Thread.sleep(5);
                } catch (final InterruptedException ex) {
                	// TODO Supposed to bail if an InterruptedException is received
                }

                ArtemisPacket pkt = mQueue.poll();

            	if (pkt == null) {
                    // empty queue; loop back to wait
                    continue;
                }

            	mDebugger.onSendPacket(pkt);

            	try {
                    pkt.writeTo(mWriter, mDebugger);
                } catch (final IOException ex) {
                    if (mRunning) {
                    	mDisconnectCause = DisconnectEvent.Cause.IO_EXCEPTION;
                    	mException = ex;
                    }

                    ThreadedArtemisNetworkInterface.this.stop();
                } catch (final Exception ex) {
                	mDebugger.onPacketWriteException(pkt, ex);
                    ThreadedArtemisNetworkInterface.this.stop();
                }
            }

            mConnected = false;
            mLastHeartbeatTimestamp = null;
            ThreadedArtemisNetworkInterface.this.stop();

            // Close the socket here; this will allow us to send any closing
            // packets needed before shutting down the pipes.
            try {
                mSkt.close();
            } catch (final IOException ex) {
            	// DON'T CARE
            }

            mDispatchThread.offer(new DisconnectEvent(mDisconnectCause, mException));
            mDone = true;
        }

        /**
         * Stop sending packets after the current one.
         */
        private void end() {
            mRunning = false;
        }

        /**
         * Receiving a WelcomePacket is how we know we're connected to the
         * server. Send a ConnectionSuccessEvent.
         */
        private void onPacket(final WelcomePacket pkt) {
            final boolean wasConnected = mConnected;
            mConnected = true;
        	mLastHeartbeatTimestamp = System.currentTimeMillis();

            if (!wasConnected) {
            	mListeners.fire(new ConnectionSuccessEvent());
            }
        }

        /**
         * Check the Version against our minimum required version and disconnect
         * if we don't support it.
         */
        private void onPacket(final VersionPacket pkt) {
            final Version version = pkt.getVersion();

            if (version.lt(ArtemisNetworkInterface.MIN_VERSION) || version.ge(ArtemisNetworkInterface.MAX_VERSION_EXCLUSIVE)) {
            	mListeners.fire(new DisconnectEvent(
            			DisconnectEvent.Cause.UNSUPPORTED_SERVER_VERSION,
            			null
            	));

            	ThreadedArtemisNetworkInterface.this.stop();
            }
        }
    }

	/**
	 * Manages receiving packets from the InputStream.
	 */
    private class ReceiverThread extends Thread {
        private boolean mRunning = false;
        private boolean mDone = false;
        private InputStream input;
        private PacketReader mReader;
        
        private ReceiverThread(final Socket skt) throws IOException {
            input = new BufferedInputStream(skt.getInputStream());
        }

        @Override
        public void run() {
            mRunning = true;
            mReader = new PacketReader(getRecvType(), input, mProtocol, mListeners);
            SenderThread sender = ThreadedArtemisNetworkInterface.this.mSendThread;
            
            while (mRunning) {
                try {
                    // read packet
                	final ParseResult result = mReader.readPacket(mDebugger);

                	if (result.getException() != null) {
                		handlePacketException(result.getException());
                	}

                    if (mRunning) {
                    	final ArtemisPacket pkt = result.getPacket();

                    	// Handle certain packets specially
                    	if (pkt instanceof WelcomePacket) {
                    		sender.onPacket((WelcomePacket) pkt);
                    	} else if (pkt instanceof VersionPacket) {
                    		sender.onPacket((VersionPacket) pkt);
                    	} else if (pkt instanceof HeartbeatPacket) {
                    		mLastHeartbeatTimestamp = Long.valueOf(System.currentTimeMillis());
                    	}

                    	// Enqueue to the event dispatch thread
                    	mDispatchThread.offer(result);
                    }
                } catch (final ArtemisPacketException ex) {
                	handlePacketException(ex);
                }
            }
            
            ThreadedArtemisNetworkInterface.this.stop();
            mDone = true;
        }

        /**
         * An exception occurred while parsing; inform the debugger, then
         * determine whether it was fatal. If it was, shut down the connection.
         * If it wasn't, pass the packet along to any proxy targets.
         */
        private void handlePacketException(ArtemisPacketException ex) {
        	mDebugger.onPacketParseException(ex);

        	if (mRunning && ex.getPayload() == null) {
        		// Exception is fatal; shut down connection
            	Throwable cause = ex.getCause();

            	if (cause instanceof EOFException || cause instanceof SocketException) {
            		mDisconnectCause = DisconnectEvent.Cause.REMOTE_DISCONNECT;
            	} else {
            		mDisconnectCause = DisconnectEvent.Cause.PACKET_PARSE_EXCEPTION;
            	}

            	mException = (Exception) cause;
            	ThreadedArtemisNetworkInterface.this.stop();
        	}
        }

        /**
         * Requests that the receiver thread be shut down.
         */
        private void end() {
            mRunning = false;
        }
    }

    /**
     * Invokes listeners. This prevents the receiver thread from blocking on listeners.
     */
    private class EventDispatchThread extends Thread {
        private final Queue<Object> mQueue = new ConcurrentLinkedQueue<Object>();

        /**
         * Enqueues a packet or event to be dispatched.
         */
        private boolean offer(final Object obj) {
            if (!mReceiveThread.mDone || !mSendThread.mDone) {
                return mQueue.offer(obj);
            }

            return false;
        }

        @Override
        public void run() {
            while (!mQueue.isEmpty() || !mReceiveThread.mDone || !mSendThread.mDone) {
                try {
                    Thread.sleep(5);
                } catch (final InterruptedException ex) {
                	// TODO Supposed to bail if an InterruptedException is received
                }

                Object obj = mQueue.poll();

            	if (obj == null) {
            		// The queue is empty; go to next loop to check if the threads are stopped
        			continue;
                }

                if (obj instanceof ParseResult) {
                	ParseResult result = (ParseResult) obj;
                	result.fireListeners();

                	if (!result.isInterestingPacket()) {
                		// No listeners were interested in the packet
                		// itself, so pass it to any proxy targets.
                		ArtemisPacket pkt = result.getPacket();

                		for (ArtemisNetworkInterface target : mProxyTargets) {
                			target.send(pkt);
                		}
                	}
                } else {
                    mListeners.fire((ConnectionEvent) obj);
                }
            }
        }
    }
}