Interface for *Artemis* Networking (IAN)
========================================
[![Build Status](https://secure.travis-ci.org/rjwut/ian.svg)](http://travis-ci.org/rjwut/ian)

**IAN** is an unofficial Java library for communicating with [*Artemis Spaceship Bridge Simulator*](http://www.artemis.eochu.com/) servers and clients.

IAN is a heavily revamped version of [ArtClientLib](https://github.com/rjwut/ArtClientLib). It currently supports *Artemis* version 2.4.0. If you need Java library support for previous versions of *Artemis*, please see the version table below:

*Artemis* | IAN/ArtClientLib
---: | ---
2.3.0 | [IAN 3.0.0](https://github.com/rjwut/ian/releases/tag/v3.0.0)
2.1.1 | [ArtClientLib v2.6.0](https://github.com/rjwut/ArtClientLib/releases/tag/v2.6.0)
2.1 | [ArtClientLib v2.4.0](https://github.com/rjwut/ArtClientLib/releases/tag/v2.4.0)
2.0 | [ArtClientLib v2.3.0](https://github.com/rjwut/ArtClientLib/releases/tag/v2.3.0)
1.x | [ArtClientLib v1.x](https://github.com/dhleong/ArtClientLib)

Note, however, that these previous versions will be missing various bug fixes and enhancements found in the latest version.

This library was originally developed by Daniel Leong and released on GitHub with permission of the developer of *Artemis*, Thom Robertson.

## Things to Know Before Using This Library ##

**IAN is an unofficial library.** Thom Robertson and Incandescent Workshop are not involved with its development and do not provide support for it. Please don't bother them with questions about it.

**Stuff might not work.** There is no official public documentation of the *Artemis* protocol. A library such as IAN is made possible through reverse engineering, requiring careful observation of *Artemis* network traffic and experimentation to learn how game data is formatted. IAN is a very good implementation of the *Artemis* protocol, but under these circumstances it is next to impossible to guarantee that it is 100% accurate. Also, new *Artemis* releases are likely to break this library until a contributor can figure out what changes were made and update things accordingly.

**There is no official support.** [Bug reports and feature requests](https://github.com/rjwut/ian/issues) are welcome, and I'm happy to try to answer questions. However, IAN is developed in my spare time, and as such, I can't guarantee support for it. I can't make any promises to address your requests, bug reports or questions in a timely fashion.

**Use at your own risk.** This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; not even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

**Don't pirate *Artemis*.** IAN is intended to allow you to enhance your *Artemis* experience with custom behavior. However, using a custom *Artemis* client or server proxy does not release you from the obligation to license the *Artemis* software. If you are playing *Artemis* without a license, either with stock or custom software, you are engaging in software piracy and inhibiting the future development of *Artemis*. If you don't have a license, please support Thom by purchasing one now.

**I'd love to see what you do with it!** If you make something cool from this, I'd love to know. Crediting this library would be appreciated, as would sharing any improvements you make, to potentially include upstream contributions in the form of pull requests.

**You can help make IAN better.** There are several ways that you can assist in IAN's development:
* Found a bug or missing feature? [Check to see if I already know about it.](https://github.com/rjwut/ian/issues) If not, you can [submit a new issue](https://github.com/rjwut/ian/issues/new).
* Handy with a packet sniffer? Join in the effort to [document the *Artemis* protocol](https://github.com/artemis-nerds/protocol-docs).
* Are you a Java developer? Feel free to implement a feature or bug fix yourself! Fork the repository, apply the changes to your copy, then submit a pull request.

## Using IAN ##

### Examples ###
The `com.walkertribe.ian.example` package contains a couple of simple demonstration classes to help you see how to use IAN:

* `ClientDemo`: Toggles your ship's red alert state when shields are raised or lowered.
* `ProxyDemo`: Stops red alert from being toggled.

### Connecting to an *Artemis* Server ###
1. **Tell IAN where to find *Artemis* resource data.** This involves instantiating an object which implements `Context`. (See the **Locating *Artemis* Resources** section above for details.)

2. **Construct a `ThreadedArtemisNetworkInterface` object.** This object implements `ArtemisNetworkInterface` and is responsible for managing the connection to the *Artemis* server. Along with the `Context` you instantiated earlier, you must provide the constructor with the host/IP address and port to which it should connect. (By default, *Artemis* servers listen for connections on port 2010, but this can be changed in *Artemis*'s `artemis.ini` file.) On construction, it will attempt to connect, throwing an `IOException` if it fails.

3. **Add event listeners.** Next, you must add one or more event listeners to the `ArtemisNetworkInterface` via the `addListener()` method. Event listeners are objects which contain methods marked with the `@Listener` annotation. IAN will notify these listeners when certain events occur. See the **Event Listeners** section below for more details.

4. **Start the network interface.** Once your listeners are registered, invoke `start()` on the `ArtemisNetworkInterface` object. Once you invoke `start()`, your listeners will start being notified of events.

5. **Send packets in response to events.** As your listeners receive events, you will inevitably want to send packets back to the Artemis server. To do so, pass an instance of the appropriate class that implements `ArtemisPacket` to the `ArtemisNetworkInterface.send()` method. Here's an example:

   ```java
   private ArtemisNetworkInterface iface;

   /**
    * Fire whatever's in the given tube.
    */
   private void fireTorpedo(int tubeIndex) {
     iface.send(new FireTubePacket(tubeIndex));
   }
   ```

6. **Disconnect.** To disconnect from the Artemis server, invoke the `ArtemisNetworkInterface.stop()` method. Make sure you do this so that the send and receive threads will be terminated; otherwise, your application won't stop.

### Locating *Artemis* Resources ###
Before you can do anything with IAN, you must instantiate an object which can provide it with *Artemis* resource data. There are three major classes of resource data IAN uses:

IAN class | *Artemis* files | Contents
--- | --- | ---
`VesselData` | `dat/vesselData.xml` | `Faction`s and `Vessel`s
`Model` | `dat/*.dxs` | 3D mesh
`VesselInternals` | `dat/*.snt` | Vessel system nodes and hallways

`VesselData` is required for IAN to function. The other two types of resources are only required if you need a `Model` or `VesselInternals` object. Note that incompatibilities may result if the remote machine's version of the resource data differs from IAN's.

IAN provides several interfaces and classes that can help you with reading *Artemis* resource data:

* `Context`: An interface for classes which can provide instances of `VesselData`, `Model` and `VesselInternals` to IAN. You must provide IAN with an instance of a class which implements `Context` before you can connect.

* `DefaultContext`: An implementation of `Context` which can parse *Artemis* resource data from an `InputStream` and cache the results for future use. It delegates the creation of the `InputStream` to a `PathResolver`.

* `PathResolver`: An interface for classes which can provide an `InputStream` that corresponds to an *Artemis* resource path.

* `FilePathResolver`: A `PathResolver` implementation that loads resources from the file system relative to a particular directory. You would typically point this at the *Artemis* install directory, but IAN will only look for `dat/vesselData.xml` (and optionally `dat/*.dxs` and `dat/*.snt`); any other files will be ignored.

* `ClasspathResolver`: A `PathResolver` implementation that loads resources from the classpath relative to a specific class. This allows you to bundle the needed resources inside your JAR.

You can provide your own implementations of `Context` or `PathResolver` if the out-of-the-box implementations do not serve your purpose. For example, you could create a custom `PathResolver` implementation that could load *Artemis* resource data from a remote server. Or you can create a custom `Context` implementation that builds the data from scratch instead of parsing it from an `InputStream`. (In fact, that's exactly what the testing code does to exercise classes that use a `Context`.)

### Event Listeners ###
To make your application react to events, you must write one or more event listeners and register them with your `ThreadedArtemisNetworkInterface` object via the `addListener()` method. An event listener can be any `Object` which has one or more methods marked with the `@Listener` annotation. A listener method must be `public`, return `void`, and have exactly one argument. The type of that argument indicates what sort of events will cause the method to be invoked:

* `ConnectionEvent`: IAN connects to or disconnects from a remote machine.
* `ArtemisPacket`: IAN receives a packet from the remote machine.
* `ArtemisObject`: IAN receives an `ArtemisObject` inside an `ObjectUpdatePacket`.

You should use the most specific subtype you can for the argument type, as IAN will only invoke your listener method when the type of the argument matches. For example, if you write a listener method with an argument of type `CommsIncomingPacket`, it will be invoked only when that packet type is received (when the COMMs station receives an incoming text message). IAN will only bother to parse a packet if a listener is interested in it, so writing your listener methods to specific subtypes can be significantly more efficient. If you write a listener that has `ArtemisPacket` as its argument, IAN will parse all packets it receives because as far as it knows, you're interested in all of them.

One important event to listen for is the `ConnectionSuccessEvent`. You shouldn't attempt to send any packets to the server before you receive this event. This may also a good time to send a `SetShipPacket` and a `SetConsolePacket`.

It's also likely that you'll want to know when the connection to the server is lost; listening for `DisconnectEvent` will handle that.

### Creating an *Artemis* Proxy Server ###
1. **Tell IAN where to find *Artemis* resource files.** This is done in exactly the same way as when you create a client.

2. **Listen for a client connection.** Open a `ServerSocket` on the desired port, then call `accept()` on it to listen for a connecting client. The `accept()` method will block until a client connects or it times out, and return a `Socket` object when the client connects. You can set the timeout by calling `ServerSocket.setSoTimeout()`; passing in `0` will cause it to wait indefinitely for a connection.

3. **Wrap the client `Socket` in a `ThreadedArtemisNetworkInterface` object.** `ThreadedArtemisNetworkInterface` has a constructor that accepts a `Socket` and a `ConnectionType` (`CLIENT` in this case), along with the `Context`. The resulting object will be responsible for managing the connection to the client.

4. **Connect to the *Artemis* server.** This is done exactly the same way as you would for creating an *Artemis* client, as documented above. You now have two `ArtemisNetworkInterface`s: one for the client and one for the server.

5. **Pass through all packets.** The `proxyTo()` method on `ArtemisNetworkInterface` creates a "pass through" connection between two connections of opposite types. Any packet that is not caught by a listener method will be passed through automatically. Note that `proxyTo()` only creates connection in one direction; to pass through packets both ways, each connection will need to call `proxyTo()` on the other.

5. **Add listeners.** Add your listeners to both the client and server objects. Once the listener has caught the packet and extracted whatever information it wants from it, you can either pass the packet along by passing it to `send()`, suppress it (by doing nothing), or even inject your own packets instead (by constructing them and passing them to `send()`). Remember that `proxyTo()` does **not** pass along packets caught by listeners, so it's up to you to `send()` them if you want them passed along. Also, keep in mind that multiple listeners can catch the same packet; be careful not to send it more than once!

6. **When one side disconnects, close the connection to the other side.** Listen for the `DisconnectEvent` from both sides. When you receive it from one side, invoke `ArtemisNetworkInterface.stop()` on the other connection (or both connections, if that's easier; calling `stop()` on an already closed connection has no effect).

### Tracking World State ###
The most frequently received packets from the server are ones which provide updates on the status of objects in the game world. These updates typically contain only that information which has changed, not the complete state of the object. The `SystemManager` class aggregates the received updates in order to provide an up-to-the-moment view of the game world. To use it, simply construct a new `SystemManager` and add it as a listener to your `ThreadedArtemisNetworkInterface`; it has several listener methods which collect the relevant packets to build the game world. You can then use the various `get*()` methods to retrieve game state from the `SystemManager`.

### Reading `vesselData.xml` ###
The `vesselData.xml` file contains information about the game's factions and vessels. The `ArtemisShielded` interface includes a `getVessel(Context)` method which will return a `Vessel` object. The `ArtemisBase`, `ArtemisNpc`, and `ArtemisPlayer` classes implement this interface. `Vessel` has a `getFaction()` method which will return the `Faction` corresponding to that `Vessel`.

`Faction` and `Vessel` data can also be retrieved directly from the `VesselData` object, which you can get by calling `Context.getVesselData()`. You can then invoke `getFaction(int)` or `getVessel(int)` to retrieve the `Faction` or `Vessel` with the corresponding ID. You can also iterate all the available `Faction`s or `Vessel`s with the `factionIterator()` and `vesselIterator()` methods.

`DefaultContext` loads resource files on demand and caches them for subsequent requests. If you wish to preload all resources up front, you can do so with the `preloadModels()` and `preloadInternals()` methods.

Note that whenever you attempt to get a reference to a single `Faction` or `Vessel`, IAN may return `null`. This occurs when the `VesselData` contains no `Faction` or `Vessel` that corresponds to the given ID. This may occur when the remote machine's `vesselData.xml` file doesn't match yours. You should make sure that your code handles this scenario gracefully.

## Acknowledgements ##
Thanks to Thom Robertson and Incandescent Workshop for creating *Artemis*, and for so graciously tolerating the community's desire to tinker with their baby.

Daniel Leong ([@dhleong](https://github.com/dhleong)) created the original version of ArtClientLib and did a lot of the difficult, thankless gruntwork of reverse engineering the *Artemis* protocol.

Various GitHub users have contributed to IAN by helping to discover and document the protocol, creating pull requests to implement features or bug fixes, or issuing bug reports. Their help is very much appreciated. They are, in alphabetical order:
* [@abrindam](https://github.com/abrindam)
* [@briandurney](https://github.com/briandurney)
* [@chrivers](https://github.com/chrivers)
* [@dthaler](https://github.com/dthaler)
* [@Hackmancoltaire](https://github.com/Hackmancoltaire)
* [@huin](https://github.com/huin)
* [@IvanSanchez](https://github.com/IvanSanchez)
* [@JordanLongstaff](https://github.com/JordanLongstaff)
* [@karafelix](https://github.com/karafelix)
* [@kiwi13cubed](https://github.com/kiwi13cubed)
* [@mrfishie](https://github.com/mrfishie)
* [@NoseyNick](https://github.com/NoseyNick)
* [@prophile](https://github.com/prophile)
* [@russjudge](https://github.com/russjudge)
* [@theGuyFromHell](https://github.com/theGuyFromHell)
* [@UserMcUser](https://github.com/UserMcUser)
