package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.protocol.AbstractProtocol;
import com.walkertribe.ian.protocol.core.comm.*;
import com.walkertribe.ian.protocol.core.eng.*;
import com.walkertribe.ian.protocol.core.gm.*;
import com.walkertribe.ian.protocol.core.helm.*;
import com.walkertribe.ian.protocol.core.sci.*;
import com.walkertribe.ian.protocol.core.setup.*;
import com.walkertribe.ian.protocol.core.singleseat.*;
import com.walkertribe.ian.protocol.core.weap.*;
import com.walkertribe.ian.protocol.core.world.*;

/**
 * Implements the core Artemis protocol.
 * @author rjwut
 */
public class CoreArtemisProtocol extends AbstractProtocol {
	// The packet classes supported by this Protocol
	private static final Class<?>[] PACKET_CLASSES = {
			// server packets
			AllShipSettingsPacket.class,
			BeamFiredPacket.class,
			EngAutoDamconUpdatePacket.class,
			EngGridUpdatePacket.class,
			CommsIncomingPacket.class,
			ConsoleStatusPacket.class,
			DmxMessagePacket.class,
			DestroyObjectPacket.class,
			DockedPacket.class,
			ExplosionPacket.class,
			BayStatusPacket.class,
			SingleSeatLaunchedPacket.class,
			GameMasterButtonPacket.class,
			GameMasterInstructionsPacket.class,
			GameMessagePacket.class,
			GameOverPacket.class,
			GameOverReasonPacket.class,
			GameOverStatsPacket.class,
			GameStartPacket.class,
			HeartbeatPacket.class,
			IncomingAudioPacket.class,
			IntelPacket.class,
			JumpBeginPacket.class,
			JumpEndPacket.class,
			KeyCaptureTogglePacket.class,
			KlaxonPacket.class,
			ObjectUpdatePacket.class,
			PausePacket.class,
			PerspectivePacket.class,
			PlayerShipDamagePacket.class,
			SkyboxPacket.class,
			SoundEffectPacket.class,
			VersionPacket.class,
			WelcomePacket.class,

			// client packets
			ActivateUpgradePacket.class,
			AudioCommandPacket.class,
			CaptainTargetPacket.class,
			ClimbDivePacket.class,
			CommsOutgoingPacket.class,
			ConvertTorpedoPacket.class,
			EngRequestGridUpdatePacket.class,
			EngSendDamconPacket.class,
			EngSetAutoDamconPacket.class,
			EngSetCoolantPacket.class,
			EngSetEnergyPacket.class,
			SingleSeatLaunchPacket.class,
			FireBeamPacket.class,
			FireTubePacket.class,
			GameMasterButtonClickPacket.class,
			GameMasterMessagePacket.class,
			GameMasterTargetLocationPacket.class,
			GameMasterTargetObjectPacket.class,
			HelmJumpPacket.class,
			HelmRequestDockPacket.class,
			HelmSetClimbDivePacket.class,
			HelmSetImpulsePacket.class,
			HelmSetSteeringPacket.class,
			HelmSetWarpPacket.class,
			HelmToggleReversePacket.class,
			KeystrokePacket.class,
			LoadTubePacket.class,
			ReadyPacket.class,
			SciScanPacket.class,
			SciTargetPacket.class,
			SetBeamFreqPacket.class,
			SetConsolePacket.class,
			SetMainScreenPacket.class,
			SetShieldsPacket.class,
			SetShipPacket.class,
			SetShipSettingsPacket.class,
			ToggleAutoBeamsPacket.class,
			TogglePerspectivePacket.class,
			ToggleRedAlertPacket.class,
			WeaponsTargetPacket.class,
			UnloadTubePacket.class
	};

	@Override
	public void registerPacketFactories(PacketFactoryRegistry registry) {
		registerPacketFactories(registry, PACKET_CLASSES);
	}
}