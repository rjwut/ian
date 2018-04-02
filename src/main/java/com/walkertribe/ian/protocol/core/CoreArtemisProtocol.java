package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.protocol.AbstractProtocol;
import com.walkertribe.ian.protocol.ArtemisPacket;
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
	private static final Class<?>[] CLASSES = {
			// server packets
			AllShipSettingsPacket.class,
			BayStatusPacket.class,
			BeamFiredPacket.class,
			CloakDecloakPacket.class,
			CommsButtonPacket.class,
			CommsIncomingPacket.class,
			ConsoleStatusPacket.class,
			DmxMessagePacket.class,
			DeleteObjectPacket.class,
			DockedPacket.class,
			EndGamePacket.class,
			EngAutoDamconUpdatePacket.class,
			EngGridUpdatePacket.class,
			ExplosionPacket.class,
			GameMasterButtonPacket.class,
			GameMasterInstructionsPacket.class,
			GameMessagePacket.class,
			GameOverPacket.class,
			GameOverReasonPacket.class,
			GameOverStatsPacket.class,
			GameStartPacket.class,
			HeartbeatPacket.class,
			IdleTextPacket.class,
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
			SingleSeatLaunchedPacket.class,
			SingleSeatTextPacket.class,
			SkyboxPacket.class,
			SmokePacket.class,
			SoundEffectPacket.class,
			TagPacket.class,
			TitlePacket.class,
			VersionPacket.class,
			WelcomePacket.class,

			// client packets
			ActivateUpgradePacket.class,
			AudioCommandPacket.class,
			BeaconConfigPacket.class,
			CaptainTargetPacket.class,
			ClimbDivePacket.class,
			CommsOutgoingPacket.class,
			ConvertTorpedoPacket.class,
			EngRequestGridUpdatePacket.class,
			EngResetCoolantPacket.class,
			EngSendDamconPacket.class,
			EngSetAutoDamconPacket.class,
			EngSetCoolantPacket.class,
			EngSetEnergyPacket.class,
			FireBeamPacket.class,
			FireTubePacket.class,
			ButtonClickPacket.class,
			GameMasterMessagePacket.class,
			GameMasterTargetLocationPacket.class,
			GameMasterTargetObjectPacket.class,
			HelmEmergencyJumpPacket.class,
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
			SetSingleSeatSettingsPacket.class,
			SingleSeatLaunchPacket.class,
			SingleSeatPilotPacket.class,
			SingleSeatShootPacket.class,
			ToggleAutoBeamsPacket.class,
			TogglePerspectivePacket.class,
			ToggleRedAlertPacket.class,
			WeaponsTargetPacket.class,
			UnloadTubePacket.class
	};

	@SuppressWarnings("unchecked")
	public CoreArtemisProtocol() {
		for (Class<?> clazz : CLASSES) {
			register((Class<? extends ArtemisPacket>) clazz);
		}
	}
}