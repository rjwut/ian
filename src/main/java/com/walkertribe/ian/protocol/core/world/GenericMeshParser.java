package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisMesh;
import com.walkertribe.ian.world.ArtemisObject;

public class GenericMeshParser extends AbstractObjectParser {
	private enum Bit {
		X,
		Y,
		Z,
		UNK_1_4,
		UNK_1_5,
		UNK_1_6,
		UNK_1_7,
		UNK_1_8,

		UNK_2_1,
		UNK_2_2,
		NAME,
		MESH_PATH,
		TEXTURE_PATH,
		UNK_2_6,
		UNK_2_7,
		UNK_2_8,

		RED,
		GREEN,
		BLUE,
		FORE_SHIELDS,
		AFT_SHIELDS,
		UNK_3_6,
		UNK_3_7,
		UNK_3_8,

		UNK_4_1,
		UNK_4_2
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
        mesh.setX(reader.readFloat(Bit.X, Float.MIN_VALUE));
        mesh.setY(reader.readFloat(Bit.Y, Float.MIN_VALUE));
        mesh.setZ(reader.readFloat(Bit.Z, Float.MIN_VALUE));

        reader.readObjectUnknown(Bit.UNK_1_4, 4);
        reader.readObjectUnknown(Bit.UNK_1_5, 4);
        reader.readObjectUnknown(Bit.UNK_1_6, 8);
        reader.readObjectUnknown(Bit.UNK_1_7, 4);
        reader.readObjectUnknown(Bit.UNK_1_8, 4);
        reader.readObjectUnknown(Bit.UNK_2_1, 4);
        reader.readObjectUnknown(Bit.UNK_2_2, 8);
        
        mesh.setName(reader.readString(Bit.NAME));
        mesh.setMesh(reader.readString(Bit.MESH_PATH));
        mesh.setTexture(reader.readString(Bit.MESH_PATH)); // wtf?!

        reader.readObjectUnknown(Bit.UNK_2_6, 4);
        reader.readObjectUnknown(Bit.UNK_2_7, 2);
        reader.readObjectUnknown(Bit.UNK_2_8, 1);

        mesh.setRed(reader.readFloat(Bit.RED, Float.MIN_VALUE));
        mesh.setGreen(reader.readFloat(Bit.GREEN, Float.MIN_VALUE));
        mesh.setBlue(reader.readFloat(Bit.BLUE, Float.MIN_VALUE));
        mesh.setFakeShields(
        		reader.readFloat(Bit.FORE_SHIELDS, Float.MIN_VALUE),
        		reader.readFloat(Bit.AFT_SHIELDS, Float.MIN_VALUE)
        );

        reader.readObjectUnknown(Bit.UNK_3_6, 4);
        reader.readObjectUnknown(Bit.UNK_3_7, 4);
        reader.readObjectUnknown(Bit.UNK_3_8, 4);
        reader.readObjectUnknown(Bit.UNK_4_1, 4);
        reader.readObjectUnknown(Bit.UNK_4_2, 4);
        return mesh;
	}

	@Override
	public void write(ArtemisObject obj, PacketWriter writer) {
		ArtemisMesh mesh = (ArtemisMesh) obj;
		writer	.writeFloat(Bit.X, mesh.getX(), Float.MIN_VALUE)
				.writeFloat(Bit.Y, mesh.getY(), Float.MIN_VALUE)
				.writeFloat(Bit.Z, mesh.getZ(), Float.MIN_VALUE)
				.writeUnknown(Bit.UNK_1_4)
				.writeUnknown(Bit.UNK_1_5)
				.writeUnknown(Bit.UNK_1_6)
				.writeUnknown(Bit.UNK_1_7)
				.writeUnknown(Bit.UNK_1_8)
				.writeUnknown(Bit.UNK_2_1)
				.writeUnknown(Bit.UNK_2_2)
				.writeString(Bit.NAME, mesh.getName())
				.writeString(Bit.MESH_PATH, mesh.getMesh())
				.writeString(Bit.MESH_PATH, mesh.getTexture())
				.writeUnknown(Bit.UNK_2_6)
				.writeUnknown(Bit.UNK_2_7)
				.writeUnknown(Bit.UNK_2_8)
				.writeFloat(Bit.RED, mesh.getRed(), Float.MIN_VALUE)
				.writeFloat(Bit.GREEN, mesh.getGreen(), Float.MIN_VALUE)
				.writeFloat(Bit.BLUE, mesh.getBlue(), Float.MIN_VALUE)
				.writeFloat(Bit.FORE_SHIELDS, mesh.getShieldsFront(), Float.MIN_VALUE)
				.writeFloat(Bit.AFT_SHIELDS, mesh.getShieldsRear(), Float.MIN_VALUE)
				.writeUnknown(Bit.UNK_3_6)
				.writeUnknown(Bit.UNK_3_7)
				.writeUnknown(Bit.UNK_3_8)
				.writeUnknown(Bit.UNK_4_1)
				.writeUnknown(Bit.UNK_4_2);
	}
}