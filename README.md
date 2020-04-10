Interface for *Artemis* Networking (IAN)
========================================
[![Build Status](https://secure.travis-ci.org/rjwut/ian.svg)](http://travis-ci.org/rjwut/ian)

**IAN** is an unofficial Java library for communicating with [*Artemis Spaceship Bridge Simulator*](https://artemisspaceshipbridge.com/) servers and clients.

IAN is a heavily revamped version of [ArtClientLib](https://github.com/rjwut/ArtClientLib). It currently supports *Artemis* versions 2.7.5. If you need Java library support for previous versions of *Artemis*, please see the version table below:

*Artemis* | IAN/ArtClientLib
---: | ---
2.7.0-4 | [IAN 3.4.0](https://github.com/rjwut/ian/releases/tag/v3.4.0)
2.4.0 | [IAN 3.2.1](https://github.com/rjwut/ian/releases/tag/v3.2.1)
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

**Don't pirate *Artemis*.** IAN is intended to allow you to enhance your *Artemis* experience with custom behavior. However, using custom *Artemis* software does not release you from the obligation to license the original *Artemis* software. If you are playing *Artemis* without a license, either with stock or custom software, you are engaging in software piracy and inhibiting the future development of *Artemis*. If you don't have a license, please support Thom by purchasing one now.

**I'd love to see what you do with it!** If you make something cool from this, I'd love to know. Crediting this library would be appreciated, as would sharing any improvements you make, to potentially include upstream contributions in the form of pull requests.

**You can help make IAN better.** There are several ways that you can assist in IAN's development. See IAN's [contribution guidelines](https://github.com/rjwut/ian/blob/master/CONTRIBUTING.md) for details.

## Using IAN ##

### Examples ###
The `com.walkertribe.ian.example` package contains some simple demonstration classes to help you see how to use IAN:

* `AnnounceServerDemo`: Shows how to respond to server discovery requests from clients so that you can make your server or proxy discoverable on the LAN.
* `ClientDemo`: Sample client that toggles your ship's red alert state when shields are raised or lowered.
* `FindServersDemo`: Shows how to discover servers on the LAN.
* `ProxyDemo`: Simple proxy that suppresses toggling red alert by any client that connects through it.

### Discovering *Artemis* Servers on the LAN ###
See `FindServersDemo` for example code.

1. **Implement the `ServerDiscoveryRequester.Listener` interface.** This object will be notified whenever a server is discovered and when the discovery wait time ends.

2. **Construct a `ServerDiscoveryRequester` and pass it your `Listener`.** You'll also need to specify how long you want to wait for responses to your discovery request.

3. **Run the `ServerDiscoveryRequester` instance.** To do this, construct a new `Thread`, passing in the `ServerDiscoveryRequester` instance, then call `start()` on the `Thread`. While the `Thread` is running, `Listener.onDiscovered()` will be invoked for each server it finds. When the wait time expires, it will invoke `Listener.onStop()`.

4. **Reuse the `ServerDiscoveryRequester` as needed.** The `ServerDiscoveryRequester` will only send a single request, when the `Thread` first starts. If you want to continually poll for servers, wait for the `ServerDiscoveryRequester` to stop, then spin up another `Thread` with the same requester object.

### Connecting to an *Artemis* Server ###
See `ClientDemo` for example code.

1. **Instantiate an object which implements `ArtemisNetworkInterface`.** This object is responsible for managing the connection to the *Artemis* server. If you have a `Server` object from a `ServerDiscoveryRequester` (see the previous section), you can just invoke `connect()` on the `Server`, which will return an `ArtemisNetworkInterface`. Otherwise, you must construct a `ThreadedArtemisNetworkInterface` object (which implements `ArtemisNetworkInterface`).  You must provide the constructor with the host/IP address and port to which it should connect. (By default, *Artemis* servers listen for connections on port 2010, but this can be changed in *Artemis*'s `artemis.ini` file.) On construction, it will attempt to connect, throwing an `IOException` if it fails.

2. **Add event listeners.** Next, you must add one or more event listeners to the `ArtemisNetworkInterface` via the `addListener()` method. Event listeners are objects which contain methods marked with the `@Listener` annotation. IAN will notify these listeners when certain events occur. See the **Event Listeners** section below for more details.

3. **Start the network interface.** Once your listeners are registered, invoke `start()` on the `ArtemisNetworkInterface` object. Once you invoke `start()`, your listeners will start being notified of events.

4. **Send packets in response to events.** As your listeners receive events, you will inevitably want to send packets back to the Artemis server. To do so, pass an instance of the appropriate class that implements `ArtemisPacket` to the `ArtemisNetworkInterface.send()` method. Here's an example:

   ```java
   private ArtemisNetworkInterface iface;

   /**
    * Fire whatever's in the given tube.
    */
   private void fireTorpedo(int tubeIndex) {
     iface.send(new FireTubePacket(tubeIndex));
   }
   ```

5. **Disconnect.** To disconnect from the Artemis server, invoke the `ArtemisNetworkInterface.stop()` method. Make sure you do this so that the send and receive threads will be terminated; otherwise, your application won't stop.

### Accepting an *Artemis* Client Connection ###
1. **Listen for a client connection.** Open a `ServerSocket` on the desired port, then call `accept()` on it to listen for a connecting client. The `accept()` method will block until a client connects or it times out, and return a `Socket` object when the client connects. You can set the timeout by calling `ServerSocket.setSoTimeout()`; passing in `0` will cause it to wait indefinitely for a connection.

2. **Wrap the client `Socket` in a `ThreadedArtemisNetworkInterface` object.** `ThreadedArtemisNetworkInterface` has a constructor that accepts a `Socket` and an `Origin` (`CLIENT` in this case). The resulting object will be responsible for managing the connection to the client.

3. **Use the `ArtemisNetworkInterface` the same way you do with clients.** You can now add listeners, start the interface, send packets to the client, and disconnect just as you do when connected to a server. See "Connecting to an *Artemis* Server" above, starting at step 2.

### Creating an *Artemis* Proxy Server ###
*See `ProxyDemo` for example code.*

1. **Listen for client connections.** See "Accepting an *Artemis* Client Connection" above.

2. **When you receive a connection, connect to the *Artemis* server for which you are proxying.** This is done exactly the same way as you would for creating an *Artemis* client, as documented in "Connecting to an *Artemis* Server". You now have two `ArtemisNetworkInterface`s: one for the client and one for the server.

3. **Pass through all packets.** The `proxyTo()` method on `ArtemisNetworkInterface` creates a "pass through" connection between two connections of opposite origin types. Any packet that is not caught by a listener method will be passed through automatically. Note that `proxyTo()` only creates connection in one direction; to pass through packets both ways, each connection will need to call `proxyTo()` on the other.

4. **Turn off auto-heartbeat.** By default, `ArtemisNetworkInterface` will automatically send periodic heartbeat packets so that the remote machine knows that it's still alive. However, in this scenario, your proxy is going to automatically forward the heartbeat packets it receives, so it doesn't need to send its own. So you will want to turn off automatic heartbeat packet sending on both sides. To do this, call `setAutoSendHeartbeat(false)`. (Note: If for some reason you are actually listening for heartbeat packets, you will need to pass them along manually.)

5. **Add listeners.** Add your listeners to both the client and server interface objects. Once the listener has caught the packet and extracted whatever information it wants from it, you can either send the packet along by passing it to `send()`, suppress it (by doing nothing), or even inject your own packets instead (by constructing them and passing them to `send()`). Remember that `proxyTo()` does **not** pass along packets caught by listeners, so it's up to you to `send()` them if you want them passed along. Also, keep in mind that multiple listeners can catch the same packet; be careful not to send it more than once!

6. **Start both network interfaces.** Invoke `start()` on both of them to start consuming packets from both connections.

7. **When one side disconnects, close the connection to the other side.** Listen for the `DisconnectEvent` from both sides. When you receive it from one side, invoke `ArtemisNetworkInterface.stop()` on the other connection (or both connections, if that's easier; calling `stop()` on an already closed connection has no effect).

### Make Your *Artemis* Server Discoverable on the LAN ###
*See `AnnounceServerDemo` for example code.*

1. **Construct a `ServerDiscoveryResponder`.** You can either determine the IP and host name to announce yourself (in which case, it's up to you to ensure they are correct), or you can use the no-arg constructor and allow IAN to attempt to select the appropriate network interface to use. Only active, non-loopback interfaces where the address corresponds to an [IPv4 private network](https://en.wikipedia.org/wiki/Private_network) will be considered. If no suitable interface is found, an `IOException` will be thrown with the message "No suitable network interface found." Those that meet these criteria will be sorted, first by whether or not it is a wifi interface (with non-wifi preferred), then by class (A, B, then C). The most preferred interface will be selected.

2. **Run the `ServerDiscoveryResponder` instance.** To do this, construct a new `Thread`, passing in the `ServerDiscoveryResponder` instance, then call `start()` on the `Thread`. While the `Thread` is running, the `ServerDiscoveryResponder` will automatically respond to any discovery requests it receives.

3. **Invoke `stop()` when you are no longer accepting client connections.** You don't want to continue to announce your server's presence to the LAN if you're not accepting client connections anymore. Calling `stop()` on the `ServerDiscoveryResponder` will cause it to shut down the `Thread` so that it will stop responding to requests.

### Event Listeners ###
To make your application react to events, you must write one or more event listeners and register them with your `ThreadedArtemisNetworkInterface` object via the `addListener()` method. An event listener can be any `Object` which has one or more methods marked with the `@Listener` annotation. A listener method must be `public`, return `void`, and have exactly one argument. The type of that argument indicates what sort of events will cause the method to be invoked:

* `ConnectionEvent`: Events related to IAN's connection with the remote machine or its heartbeat.
* `ArtemisPacket`: IAN receives a packet from the remote machine.
* `ArtemisObject`: IAN receives an `ArtemisObject` inside an `ObjectUpdatePacket`.

You should use the most specific subtype you can for the argument type, as IAN will only invoke your listener method when the type of the argument matches. For example, if you write a listener method with an argument of type `CommsIncomingPacket`, it will be invoked only when that packet type is received (when the COMMs station receives an incoming text message). IAN will only bother to parse a packet if a listener is interested in it, so writing your listener methods to specific subtypes can be significantly more efficient. If you write a listener that has `ArtemisPacket` as its argument, IAN will parse all packets it receives because as far as it knows, you're interested in all of them.

One important event to listen for when creating a client is the `ConnectionSuccessEvent`. You shouldn't attempt to send any packets to the server before you receive this event. This may also be a good time to send a `SetShipPacket` and a `SetConsolePacket`.

It's also likely that you'll want to know when the connection to the remote machine is lost; listening for `DisconnectEvent` will handle that.

Artemis servers and clients send occasional heartbeat packets as a way of letting remote machines know they're still alive. The `ThreadedArtemisNetworkInterface` will raise a `HeartbeatLostEvent` when a heartbeat packet has not been received recently. If afterwards a heartbeat packet is received, a `HeartbeatRegainedEvent` will be raised.

### `ArtemisObject` Instances and Handling Updates ###
The `ArtemisObject` interface is implemented by objects which represent game world objects (ships, bases, creatures, asteroids, torpedoes, black holes, etc.). The server will frequently send `ObjectUpdatePacket`s that contain these objects.

`ArtemisObject`s have properties which are exposed with getters and setters. The properties which are available will vary depending on the object's type. In order to save network bandwidth, the server will usually send only the properties of an object that have changed since the last update rather than the entire state of the object. Therefore, these properties may be unspecified.

Rather than use `null` to mean "unspecified" (and incur the memory overhead of instantiating many primitive wrapper objects), IAN uses a specific value for each property to indicate that it is unspecified. The value that means "unspecified" depends on the data type:

| Data type       | Unspecified value   |
| --------------- | ------------------- |
| `BoolState`     | `BoolState.UNKNOWN` |
| other `Object`s | `null`              |
| `float`         | `Float.NaN`         |
| `byte` or `int` | `-1` or `MIN_VALUE` |

For `byte` or `int`, `-1` is used to mean "unspecified" unless `-1` is a valid value. In that case, `MIN_VALUE` is used. Any property which does not follow the above rules will explain in its documentation.

The `ArtemisObject.updateFrom()` method copies the specified properties from one object to another. This allows you to keep an object current as you recieve updates: simply pass the updated object to the original object's `updateFrom()` method and will have the updates applied to it. If you wish to maintain updated status for the entire game world, the `World` class handles this for you. Simply instantiate one and register it as a listener with your `ArtemisNetworkInterface`. As the simulation runs, you can use the various `get*()` methods to retrieve game state.

### The Ship System Grid ###
Apart from world objects, another common state you may want to track would be a ship's system grid. There are two distinct parts to the grid:

- Infrastructure data: physical locations of nodes and what ship systems are located there
- Current state: damage, status of DAMCON teams

The grid consists of a 5 × 5 × 10 matrix of nodes overlaid on the ship. A node can be a ship system, a hallway, or empty (typically because it falls outside the ship). DAMCON teams can move between any two adjacent non-empty nodes in the grid.

Each node has two sets of coordinates:
- Grid coordinate: Its location in the 5 × 5 × 10 matrix described above. These coordinate values are `int`s.
- Physical coordinate: Its physical location relative to the center of the ship. These coordinate values are `float`s.

The `Grid` class is used to track system grid information, and it can store either or both of the kinds of data mentioned above. When you have a `Vessel` object (described in "Locating *Artemis* Resources" below), you can retrieve the infrastructure grid from it by calling `getGrid()`. The `Grid` class also has `@Listener` methods that will update it with damage and DAMCON information as the simulation runs when you register it with the `ArtemisNetworkInterface`.

Note that `Grid` objects you retrieve from `Vessel`s are "locked", meaning they cannot be modified. (This is because data read from `vesselData.xml` data is cached for reuse.) If you want to use a `Grid` from a `Vessel`to start tracking state, you will need to clone the `Grid` and work from the clone. `Grid` has a constructor that does this for you.

#### Node Accessors ####
There are several ways to retrieve node data from a `Grid`:

- To access an individual node, you will need a `GridCoord`. These are statically cached; you can retrieve one by calling `GridCoord.get(x, y, z)`, passing in its grid coordinates. Once you have the `GridCoord` object, retrieve its corresponding node by passing it to `Grid.get(GridCoord)`.
- `Grid` implements `Iterable<GridNode>`, so you can iterate it to visit all the `GridNodes` in the `Grid`.
- You can also invoke `getAllNodes()` to return a `List` of all the nodes.
- You likely are really only interested in the accessible nodes; `getAccessibleNodes()` will provide those.
- If you would like the nodes grouped by the `ShipSystem` they contain, call `groupNodesBySystem()`, which will return a `Map<ShipSystem, List<GridNode>>`. Note that empty and hallway nodes are omitted from the returned `Map`.

#### Node Mutators ####
Unless you use one of the constructors that prepoplate the `Grid`, a new `Grid` starts out with all 250 nodes being empty. If the `Grid` is not locked, you can replace any node with a new `GridNode` with whatever infrastructure data you specify. You do this by calling `Grid.set(GridNode)`. To copy all the node data from one `Grid` to another, use `copyNodes(Grid, boolean)`.

#### DAMCON Team Accessors ####
To retrieve an individual DAMCON team, call `getDamcon(byte)`, passing in the team's ID. To get a new `List` of all DAMCON teams, call `getAllDamconTeams()`.

#### DAMCON Team Mutators ####
To update a DAMCON team, construct a `DamconTeam` with the new data, then pass it to `setDamcon(DamconTeam)`. To copy all DAMCON team data from one `Grid` into another, call `copyDamcons(Grid)`. Note that these methods will fail if the `Grid` is locked.

#### `Point` Cloud ####
When rendering a 3D image of the `Grid`, you will need to be able to get the locations of all nodes and DAMCON teams so that you can easily transform them prior to rendering. The `toPointCloud()` method does this, returning a `Map<String, Point>` containing the `Point`s for all non-empty nodes and DAMCON teams. Then you can retrieve a `Point` by providing its key:

- `Point`s corresponding to `GridNode`s have keys in `'[x,y,z]'` format, like this: `'[1,5,7]'`.
- `Point`s for DAMCON teams have keys in `'DAMCON n'`, format, like this: `'DAMCON 1'`

It's particularly useful to pass this into `Model.transformPoints()`, allowing you to apply a transformation to them so that you can render them on top of a transformed `Model`. This lets you render a 3D ship with the system grid overlaid on top.

### Locating *Artemis* Resources ###
Some operations require data which is obtained from *Artemis* resource files. There are three major classes of resource data IAN can make use of:

IAN class | *Artemis* files | Contents
--- | --- | ---
`VesselData` | `dat/vesselData.xml` | `Faction`s and `Vessel`s
`Model` | `dat/*.dxs` | 3D mesh
`Grid` | `dat/*.snt` | Vessel system grid

All three of these resource types are optional: as long as you don't need the particular functionality that is dependent on these resource files, IAN can run without them. Note that incompatibilities may result if the remote machine's version of the resource data differs from IAN's.

IAN provides several interfaces and classes that can help you with reading *Artemis* resource data:

* `Context`: An interface for classes which can provide instances of `VesselData`, `Model` and `Grid` to IAN. Any method that requires data from *Artemis* resource files will ask for an object which implements `Context`.

* `DefaultContext`: An implementation of `Context` which can parse *Artemis* resource data from an `InputStream` and cache the results for future use. It delegates the creation of the `InputStream` to a `PathResolver`.

* `PathResolver`: An interface for classes which can provide an `InputStream` that corresponds to an *Artemis* resource path.

* `FilePathResolver`: A `PathResolver` implementation that loads resources from the file system relative to a particular directory. You would typically point this at the *Artemis* install directory, but at most IAN will only look for `dat/vesselData.xml`, `dat/*.dxs`, and `dat/*.snt`; any other files will be ignored.

* `ClasspathResolver`: A `PathResolver` implementation that loads resources from the classpath relative to a specific class. This allows you to bundle the needed resources inside your JAR.

You can provide your own implementations of `Context` or `PathResolver` if the out-of-the-box implementations do not serve your purpose. For example, you could create a custom `PathResolver` implementation that could load *Artemis* resource data from a remote server. Or you can create a custom `Context` implementation that builds the data from scratch instead of parsing it from an `InputStream`. (In fact, that's exactly what the testing code does to exercise code that uses a `Context`.)

### Reading `vesselData.xml` ###
The `vesselData.xml` file contains information about the game's factions and vessels. The `ArtemisShielded` interface includes a `getVessel(Context)` method which will return a `Vessel` object. The `ArtemisBase`, `ArtemisNpc`, and `ArtemisPlayer` classes implement this interface. `Vessel` has a `getFaction()` method which will return the `Faction` corresponding to that `Vessel`.

`Faction` and `Vessel` data can also be retrieved directly from the `VesselData` object, which you can get by calling `Context.getVesselData()`. You can then invoke `getFaction(int)` or `getVessel(int)` to retrieve the `Faction` or `Vessel` with the corresponding ID. You can also iterate all the available `Faction`s or `Vessel`s with the `factionIterator()` and `vesselIterator()` methods.

`DefaultContext` loads resource files on demand and caches them for subsequent requests. If you wish to preload all resources up front, you can do so with the `preloadModels()` and `preloadGrids()` methods.

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
* [@StarryWisdom](https://github.com/StarryWisdom)
* [@theGuyFromHell](https://github.com/theGuyFromHell)
* [@UserMcUser](https://github.com/UserMcUser)
