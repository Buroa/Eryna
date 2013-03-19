package org.apollo.fs.parser;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.archive.Archive;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.util.ByteBufferUtil;

/**
 * A class which parses object definitions.
 * @author Steve
 */
public final class ObjectDefinitionParser {

	/**
	 * The indexed file system.
	 */
	private final IndexedFileSystem fs;

	/**
	 * Creates the object definition parser.
	 * @param fs The indexed file system.
	 */
	public ObjectDefinitionParser(IndexedFileSystem fs) {
		this.fs = fs;
	}

	/**
	 * Parses the object definitions.
	 * @return The object definitions.
	 * @throws IOException if an I/O error occurs.
	 */
	public ObjectDefinition[] parse() throws IOException {
		Archive config = Archive.decode(fs.getFile(0, 2));
		ByteBuffer dat = config.getEntry("loc.dat").getBuffer();
		ByteBuffer idx = config.getEntry("loc.idx").getBuffer();
		int count = idx.getShort();
		int[] indices = new int[count];
		int index = 2;
		for (int i = 0; i < count; i++) {
			indices[i] = index;
			index += idx.getShort();
		}
		ObjectDefinition[] defs = new ObjectDefinition[count];
		for (int i = 0; i < count; i++) {
			dat.position(indices[i]);
			defs[i] = parseDefinition(i, dat);
		}
		return defs;
	}

	/**
	 * Parses a single definition.
	 * @param id The object's id.
	 * @param buffer The buffer.
	 * @return The definition.
	 */
	private ObjectDefinition parseDefinition(int id, ByteBuffer buffer) {
		ObjectDefinition def = new ObjectDefinition(id);
		int interactableValue = -1;

		while (true) {
			int code = buffer.get() & 0xFF;

			if (code == 0) {
				return def;
			} else if (code == 1) {
				int amount = buffer.get() & 0xFF;
				for (int i = 0; i < amount; i++) {
					buffer.getShort();
					buffer.get();
				}
			} else if (code == 2) {
				def.setName(ByteBufferUtil.readString(buffer));
			} else if (code == 3) {
				def.setDescription(ByteBufferUtil.readString(buffer));
			} else if (code == 5) {
				int amount = buffer.get() & 0xFF;
				for (int i = 0; i < amount; i++) {
					buffer.getShort();
				}
			} else if (code == 14) {
				def.setSizeY(buffer.get() & 0xFF);
			} else if (code == 15) {
				def.setSizeX(buffer.get() & 0xFF);
			} else if (code == 17) {
				def.setWalkable(true);
			} else if (code == 18) {
				// dunno
			} else if (code == 19) {
				interactableValue = buffer.get() & 0xFF;
				def.setInteractable(interactableValue == 1);
			} else if (code == 21) {
				def.setSolid(true);
			} else if (code == 22) {
				@SuppressWarnings("unused")
				boolean flatShading = true;
			} else if (code == 23) {

			} else if (code == 24) {
				@SuppressWarnings("unused")
				int animationId = buffer.getShort();
			} else if (code == 28) {
				buffer.get();
			} else if (code == 29) {
				@SuppressWarnings("unused")
				int brightness = buffer.get();
			} else if (code >= 30 && code < 39) {
				String action = ByteBufferUtil.readString(buffer);
				def.addAction(code - 30, action);
			} else if (code == 39) {
				@SuppressWarnings("unused")
				int contrast = buffer.get();
			} else if (code == 40) {
				int amount = buffer.get() & 0xFF;
				for (int i = 0; i < amount; i++) {
					buffer.getShort();
					buffer.getShort();
				}
			} else if (code == 60) {
				buffer.getShort();
			} else if (code == 65) {
				int scaleX = buffer.getShort() & 0xFFFF;
				def.setScaleX(scaleX);
			} else if (code == 66) {
				int scaleY = buffer.getShort() & 0xFFFF;
				def.setScaleY(scaleY);
			} else if (code == 67) {
				int scaleZ = buffer.getShort() & 0xFFFF;
				def.setScaleZ(scaleZ);
			} else if (code == 68) {
				int mapSceneId = buffer.getShort() & 0xFFFF;
				def.setMapSceneId(mapSceneId);
			} else if (code == 69) {
				@SuppressWarnings("unused")
				int unknown = buffer.get();
			} else if (code == 70) {
				int offsetX = buffer.getShort() & 0xFFFF;
				def.setOffsetX(offsetX);
			} else if (code == 71) {
				int offsetY = buffer.getShort() & 0xFFFF;
				def.setOffsetY(offsetY);
			} else if (code == 72) {
				int offsetZ = buffer.getShort() & 0xFFFF;
				def.setOffsetZ(offsetZ);
			} else if (code == 74) {
				def.setWalkable(false);
			} else if (code == 75) {
				buffer.get();
			} else {
				continue;
			}
		}
	}
}