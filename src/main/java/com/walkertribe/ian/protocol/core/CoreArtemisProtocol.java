package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.protocol.AbstractProtocol;
import com.walkertribe.ian.protocol.core.comm.*;
import com.walkertribe.ian.protocol.core.eng.*;
import com.walkertribe.ian.protocol.core.fighter.*;
import com.walkertribe.ian.protocol.core.gm.*;
import com.walkertribe.ian.protocol.core.helm.*;
import com.walkertribe.ian.protocol.core.sci.*;
import com.walkertribe.ian.protocol.core.setup.*;
import com.walkertribe.ian.protocol.core.weap.*;
import com.walkertribe.ian.protocol.core.world.*;

/**
 * Implements the core Artemis protocol.
 * @author rjwut
 */
public class CoreArtemisProtocol extends AbstractProtocol {
	// The packet classes supported by this Protocol
	private static final Class<?>[] PACKET_CLASSES = {
			// server classes
			// -- prioritized
			ObjectUpdatePacket.class,
			BeamFiredPacket.class,
			EngGridUpdatePacket.class,
			IntelPacket.class,
			SoundEffectPacket.class,
			// -- rest
			AllShipSettingsPacket.class,
			EngAutoDamconUpdatePacket.class,
			CommsIncomingPacket.class,
			ConsoleStatusPacket.class,
			DmxMessagePacket.class,
			DestroyObjectPacket.class,
			DifficultyPacket.class,
			FighterBayStatusPacket.class,
			FighterLaunchedPacket.class,
			GameMasterButtonPacket.class,
			GameMasterInstructionsPacket.class,
			GameMessagePacket.class,
			GameOverPacket.class,
			GameOverReasonPacket.class,
			GameOverStatsPacket.class,
			IncomingAudioPacket.class,
			JumpBeginPacket.class,
			JumpEndPacket.class,
			KeyCaptureTogglePacket.class,
			PausePacket.class,
			PerspectivePacket.class,
			PlayerShipDamagePacket.class,
			SkyboxPacket.class,
			VersionPacket.class,
			WelcomePacket.class,

			// client classes
			// -- prioritized
			ToggleShieldsPacket.class,
			FireBeamPacket.class,
			FireTubePacket.class,
			ToggleAutoBeamsPacket.class,
			SetWeaponsTargetPacket.class,
			LoadTubePacket.class,
			HelmSetSteeringPacket.class,
			HelmSetWarpPacket.class,
			HelmJumpPacket.class,
			EngSetCoolantPacket.class,
			EngSetEnergyPacket.class,
			HelmSetImpulsePacket.class,
			HelmRequestDockPacket.class,
			FighterLaunchPacket.class,
			// -- rest
			AudioCommandPacket.class,
			CaptainSelectPacket.class,
			ClimbDivePacket.class,
			CommsOutgoingPacket.class,
			ConvertTorpedoPacket.class,
			EngSendDamconPacket.class,
			EngSetAutoDamconPacket.class,
			GameMasterButtonClickPacket.class,
			GameMasterMessagePacket.class,
			GameMasterSelectLocationPacket.class,
			GameMasterSelectObjectPacket.class,
			HelmSetClimbDivePacket.class,
			HelmToggleReversePacket.class,
			KeystrokePacket.class,
			ReadyPacket.class,
			EngRequestGridUpdatePacket.class,
			SciScanPacket.class,
			SciSelectPacket.class,
			SetBeamFreqPacket.class,
			SetMainScreenPacket.class,
			SetShipPacket.class,
			SetShipSettingsPacket.class,
			SetConsolePacket.class,
			TogglePerspectivePacket.class,
			ToggleRedAlertPacket.class,
			UnloadTubePacket.class
	};

	@Override
	public void registerPacketFactories(PacketFactoryRegistry registry) {
		registerPacketFactories(registry, PACKET_CLASSES);
	}
}