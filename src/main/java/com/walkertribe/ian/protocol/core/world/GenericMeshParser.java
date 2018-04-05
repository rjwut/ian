package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisMesh;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * ObjectParser implementation for generic meshes
 * @author rjwut
 */
public class GenericMeshParser extends AbstractObjectParser {
	private enum Bit {
		X,
		Y,
		Z,
		UNK_1_4,
		UNK_1_5,
		UNK_1_6,
		ROLL,
		PITCH,

		HEADING,
		ROLL_DELTA,
		PITCH_DELTA,
		HEADING_DELTA,
		NAME,
		MESH_PATH,
		TEXTURE_PATH,
		PUSH_RADIUS,

		BLOCK_FIRE,
		SCALE,
		RED,
		GREEN,
		BLUE,
		FORE_SHIELDS,
		AFT_SHIELDS,
		UNK_3_8,

		UNK_4_1,
		UNK_4_2,
		UNK_4_3
	}
	private static final int BIT_COUNT = Bit.values().length;

	GenericMeshParser() {
		super(ObjectType.GENERIC_MESH);
	}

	@Override
	public int getBitCount() {
		return BIT_COUNT;
	}

	@Override
	protected ArtemisMesh parseImpl(PacketReader reader) {
        final ArtemisMesh mesh = new ArtemisMesh(reader.getObjectId());
        mesh.setX(reader.readFloat(Bit.X));
        mesh.setY(reader.readFloat(Bit.Y));
        mesh.setZ(reader.readFloat(Bit.Z));

        reader.readObjectUnknown(Bit.UNK_1_4, 4);
        reader.readObjectUnknown(Bit.UNK_1_5, 4);
        reader.readObjectUnknown(Bit.UNK_1_6, 4);

        mesh.setRoll(reader.readFloat(Bit.ROLL));
        mesh.setPitch(reader.readFloat(Bit.PITCH));
        mesh.setHeading(reader.readFloat(Bit.HEADING));
        mesh.setRollDelta(reader.readFloat(Bit.ROLL_DELTA));
        mesh.setPitchDelta(reader.readFloat(Bit.PITCH_DELTA));
        mesh.setHeadingDelta(reader.readFloat(Bit.HEADING_DELTA));
        mesh.setName(reader.readString(Bit.NAME));
        mesh.setMesh(reader.readString(Bit.MESH_PATH));
        mesh.setTexture(reader.readString(Bit.TEXTURE_PATH));
        mesh.setPushRadius(reader.readFloat(Bit.PUSH_RADIUS));
        mesh.setBlockFire(reader.readBool(Bit.BLOCK_FIRE, 1));
        mesh.setScale(reader.readFloat(Bit.SCALE));
        mesh.setRed(reader.readFloat(Bit.RED));
        mesh.setGreen(reader.readFloat(Bit.GREEN));
        mesh.setBlue(reader.readFloat(Bit.BLUE));
        mesh.setFakeShields(
        		reader.readFloat(Bit.FORE_SHIELDS),
        		reader.readFloat(Bit.AFT_SHIELDS)
        );

        reader.readObjectUnknown(Bit.UNK_3_8, 1);
        reader.readObjectUnknownString(Bit.UNK_4_1);
        reader.readObjectUnknownString(Bit.UNK_4_2);
        reader.readObjectUnknown(Bit.UNK_4_3, 4);
        return mesh;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisMesh mesh = (ArtemisMesh) obj;
		writer	.writeFloat(Bit.X, mesh.getX())
				.writeFloat(Bit.Y, mesh.getY())
				.writeFloat(Bit.Z, mesh.getZ())
				.writeUnknown(Bit.UNK_1_4)
				.writeUnknown(Bit.UNK_1_5)
				.writeUnknown(Bit.UNK_1_6)
				.writeFloat(Bit.ROLL, mesh.getRoll())
				.writeFloat(Bit.PITCH, mesh.getPitch())
				.writeFloat(Bit.HEADING, mesh.getHeading())
				.writeFloat(Bit.ROLL_DELTA, mesh.getRollDelta())
				.writeFloat(Bit.PITCH_DELTA, mesh.getPitchDelta())
				.writeFloat(Bit.HEADING_DELTA, mesh.getHeadingDelta())
				.writeString(Bit.NAME, mesh.getName())
				.writeString(Bit.MESH_PATH, mesh.getMesh())
				.writeString(Bit.TEXTURE_PATH, mesh.getTexture())
				.writeFloat(Bit.PUSH_RADIUS, mesh.getPushRadius())
				.writeBool(Bit.BLOCK_FIRE, mesh.getBlockFire(), 1)
				.writeFloat(Bit.SCALE, mesh.getScale())
				.writeFloat(Bit.RED, mesh.getRed())
				.writeFloat(Bit.GREEN, mesh.getGreen())
				.writeFloat(Bit.BLUE, mesh.getBlue())
				.writeFloat(Bit.FORE_SHIELDS, mesh.getShieldsFront())
				.writeFloat(Bit.AFT_SHIELDS, mesh.getShieldsRear())
				.writeUnknown(Bit.UNK_3_8)
				.writeUnknown(Bit.UNK_4_1)
				.writeUnknown(Bit.UNK_4_2)
				.writeUnknown(Bit.UNK_4_3);
	}
}