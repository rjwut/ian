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

		UNK_3_1,
		UNK_3_2,
		COLOR,
		FORE_SHIELDS,
		AFT_SHIELDS,
		UNK_3_6,
		UNK_3_7,
		UNK_3_8,

		UNK_4_1,
		UNK_4_2
	}
	private static final Bit[] BITS = Bit.values();

	GenericMeshParser() {
		super(ObjectType.GENERIC_MESH);
	}

	@Override
	public Bit[] getBits() {
		return BITS;
	}

	@Override
	protected ArtemisMesh parseImpl(PacketReader reader) {
        float x = reader.readFloat(Bit.X, Float.MIN_VALUE);
        float y = reader.readFloat(Bit.Y, Float.MIN_VALUE);
        float z = reader.readFloat(Bit.Z, Float.MIN_VALUE);

        reader.readObjectUnknown(Bit.UNK_1_4, 4);
        reader.readObjectUnknown(Bit.UNK_1_5, 4);
        reader.readObjectUnknown(Bit.UNK_1_6, 8);
        reader.readObjectUnknown(Bit.UNK_1_7, 4);
        reader.readObjectUnknown(Bit.UNK_1_8, 4);
        reader.readObjectUnknown(Bit.UNK_2_1, 4);
        reader.readObjectUnknown(Bit.UNK_2_2, 8);
        
        final ArtemisMesh mesh = new ArtemisMesh(reader.getObjectId());
        mesh.setName(reader.readString(Bit.NAME));
        mesh.setX(x);
        mesh.setY(y);
        mesh.setZ(z);
        mesh.setMesh(reader.readString(Bit.TEXTURE_PATH)); // wtf?!
        mesh.setTexture(reader.readString(Bit.TEXTURE_PATH));
        
        reader.readObjectUnknown(Bit.UNK_2_6, 4);
        reader.readObjectUnknown(Bit.UNK_2_7, 2);
        reader.readObjectUnknown(Bit.UNK_2_8, 1);
        reader.readObjectUnknown(Bit.UNK_3_1, 1);
        reader.readObjectUnknown(Bit.UNK_3_2, 1);

        boolean hasColor = reader.has(Bit.COLOR);

        // color
        if (hasColor) {
        	mesh.setARGB(
        			1.0f,
        			reader.readFloat(),
        			reader.readFloat(),
        			reader.readFloat()
        	);
        }

        mesh.setFakeShields(
        		reader.readFloat(Bit.FORE_SHIELDS, Float.MIN_VALUE),
        		reader.readFloat(Bit.AFT_SHIELDS, Float.MIN_VALUE)
        );

        reader.readObjectUnknown(Bit.UNK_3_6, 1);
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
				.writeString(Bit.TEXTURE_PATH, mesh.getMesh())
				.writeString(Bit.TEXTURE_PATH, mesh.getTexture())
				.writeUnknown(Bit.UNK_2_6)
				.writeUnknown(Bit.UNK_2_7)
				.writeUnknown(Bit.UNK_2_8)
				.writeUnknown(Bit.UNK_3_1)
				.writeUnknown(Bit.UNK_3_2);

		if (mesh.hasColor()) {
			writer	.writeFloat(((float) mesh.getRed()) / 255)
					.writeFloat(((float) mesh.getGreen()) / 255)
					.writeFloat(((float) mesh.getBlue()) / 255);
		}

		writer	.writeFloat(Bit.FORE_SHIELDS, mesh.getShieldsFront(), Float.MIN_VALUE)
				.writeFloat(Bit.AFT_SHIELDS, mesh.getShieldsRear(), Float.MIN_VALUE)
				.writeUnknown(Bit.UNK_3_6)
				.writeUnknown(Bit.UNK_3_7)
				.writeUnknown(Bit.UNK_3_8)
				.writeUnknown(Bit.UNK_4_1)
				.writeUnknown(Bit.UNK_4_2);
	}
}