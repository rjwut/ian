Interface for Artemis Networking (IAN)
======================================
**IAN** is an unofficial Java library for communicating with [Artemis Spaceship Bridge Simulator](http://www.artemis.eochu.com/) servers and clients.

IAN is a heavily revamped version of [ArtClientLib](https://github.com/rjwut/ArtClientLib). It currently supports Artemis version 2.3.0. If you need Java library support for previous versions of Artemis, please see the list below:

* Artemis v2.1.1: [ArtClientLib v2.6.0](https://github.com/rjwut/ArtClientLib/releases/tag/v2.6.0)
* Artemis v2.1: [ArtClientLib v2.4.0](https://github.com/rjwut/ArtClientLib/releases/tag/v2.4.0)
* Artemis v2.0: [ArtClientLib v2.3.0](https://github.com/rjwut/ArtClientLib/releases/tag/v2.3.0)
* Artemis v1.x: [ArtClientLib v1.x](https://github.com/dhleong/ArtClientLib) (by Daniel Leong)

This library was originally developed by Daniel Leong and released on GitHub with permission of the developer of Artemis, Thom Robertson.

## Things to know before using this library ##

**IAN is an unofficial library.** Thom Robertson and Incandescent Workshop are not involved with its development and do not provide support for it. Please don't bother them with questions about it.

**Stuff might not work.** There is no official public documentation of the Artemis protocol. A library such as IAN is made possible through reverse engineering, requiring careful observation of Artemis network traffic and experimentation to learn how game data is formatted. IAN is a very good implementation of the Artemis protocol, but under these circumstances it is next to impossible to guarantee that it is 100% accurate. Also, new Artemis releases are likely to break this library until a contributor can figure out what changes were made and update things accordingly.

**There is no official support.** [Bug reports and feature requests](https://github.com/rjwut/ian/issues) are welcome, and I'm happy to try to answer questions. However, IAN is developed in my spare time, and as such, I can't guarantee support for it. I can't make any promises to address your requests, bug reports or questions in a timely fashion.

**Use at your own risk.** This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; not even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

**Don't pirate Artemis.** IAN is intended to allow you to enhance your Artemis experience with custom behavior. However, using a custom Artemis client or server proxy does not release you from the obligation to license the Artemis software. If you are playing Artemis without a license, either with stock or custom software, you are engaging in software piracy and inhibiting the future development of Artemis. If you don't have a license, please support Thom by purchasing one now.

**I'd love to see what you do with it!** If you make something cool from this, I'd love to know. Crediting this library would be appreciated, as would sharing any improvements you make, to potentially include upstream contributions in the form of pull requests.

**You can help make IAN better.** There are several ways that you can assist in IAN's development:
* Found a bug or missing feature? [Check to see if I already know about it.](https://github.com/rjwut/ian/issues) If not, you can [submit a new issue](https://github.com/rjwut/ian/issues/new).
* Handy with a packet sniffer? Join in the effort to [document the Artemis protocol](https://github.com/artemis-nerds/protocol-docs).
* Are you a Java developer? Feel free to implement a feature or bug fix yourself! Fork the repository, apply the changes to your copy, then submit a pull request.

## Acknowledgements ##
Thanks to Thom Robertson and Incandescent Workshop for creating Artemis, and for so graciously tolerating the community's desire to tinker with their baby.

Daniel Leong ([@dhleong](https://github.com/dhleong)) created the original version of ArtClientLib and did a lot of the difficult, thankless gruntwork of reverse engineering the Artemis protocol.

Various GitHub users have contributed to ArtClientLib by helping to discover and document the protocol, creating pull requests to implement features or bug fixes, or issuing bug reports. Their help is very much appreciated. They are, in alphabetical order:
* [@briandurney](https://github.com/briandurney)
* [@huin](https://github.com/huin)
* [@IvanSanchez](https://github.com/IvanSanchez)
* [@karafelix](https://github.com/karafelix)
* [@kiwi13cubed](https://github.com/kiwi13cubed)
* [@mrfishie](https://github.com/mrfishie)
* [@prophile](https://github.com/prophile)
* [@russjudge](https://github.com/russjudge)
* [@theGuyFromHell](https://github.com/theGuyFromHell)
