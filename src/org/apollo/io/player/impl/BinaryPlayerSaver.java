package org.apollo.io.player.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apollo.game.model.Appearance;
import org.apollo.game.model.Frenemy;
import org.apollo.game.model.FrenemySet;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.PlayerSettings;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;
import org.apollo.io.player.PlayerSaver;
import org.apollo.util.StreamUtil;

/**
 * A {@link PlayerSaver} implementation that saves player data to a binary file.
 * @author Graham
 */
public final class BinaryPlayerSaver implements PlayerSaver {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.apollo.io.player.PlayerSaver#savePlayer(org.apollo.game.model.Player)
	 */
	@Override
	public void savePlayer(Player player) throws Exception {
		final PlayerSettings settings = player.getSettings();
		final File f = BinaryPlayerUtil.getFile(player.getName());
		final DataOutputStream out = new DataOutputStream(new FileOutputStream(f));
		try {
			// write credentials and privileges
			StreamUtil.writeString(out, player.getName());
			StreamUtil.writeString(out, player.getCredentials().getPassword());
			out.writeByte(player.getPrivilegeLevel().toInteger());
			out.writeBoolean(settings.isMembers());
			// write position
			final Position position = player.getPosition();
			out.writeShort(position.getX());
			out.writeShort(position.getY());
			out.writeByte(position.getHeight());
			// write the privacy settings
			out.writeShort(settings.getPublicChat());
			out.writeShort(settings.getPrivateChat());
			out.writeShort(settings.getTrade());
			// get the run energy & misc
			out.writeByte(player.getRunEnergy());
			out.writeShort(settings.getVotingPoints());
			out.writeBoolean(settings.isMuted());
			out.writeBoolean(settings.isBanned());
			// write appearance
			out.writeBoolean(settings.hasDesignedCharacter());
			final Appearance appearance = player.getAppearance();
			out.writeByte(appearance.getGender().toInteger());
			final int[] style = appearance.getStyle();
			for (final int element : style)
				out.writeByte(element);
			final int[] colors = appearance.getColors();
			for (final int color : colors)
				out.writeByte(color);
			out.flush();
			// write inventories
			writeInventory(out, player.getInventory());
			writeInventory(out, player.getEquipment());
			writeInventory(out, player.getBank());
			// write skills
			final SkillSet skills = player.getSkillSet();
			out.writeByte(skills.size());
			for (int i = 0; i < skills.size(); i++) {
				final Skill skill = skills.getSkill(i);
				out.writeByte(skill.getCurrentLevel());
				out.writeDouble(skill.getExperience());
			}
			// write frenemies
			final FrenemySet frenemies = player.getFrenemySet();
			out.writeByte(frenemies.size());
			for (int i = 0; i < frenemies.size(); i++) {
				final Frenemy frenemy = frenemies.getFrenemy(i);
				if (frenemy != null) {
					out.writeLong(frenemy.getEncodedFrenemy());
					out.writeBoolean(frenemy.isFriend());
				} else {
					out.writeLong(0);
					out.writeBoolean(true);
				}
			}
		} finally {
			out.close();
		}
	}

	/**
	 * Writes an inventory to the specified output stream.
	 * @param out The output stream.
	 * @param inventory The inventory.
	 * @throws IOException if an I/O error occurs.
	 */
	private void writeInventory(DataOutputStream out, Inventory inventory) throws IOException {
		final int capacity = inventory.capacity();
		out.writeShort(capacity);
		for (int slot = 0; slot < capacity; slot++) {
			final Item item = inventory.get(slot);
			if (item != null) {
				out.writeShort(item.getId() + 1);
				out.writeInt(item.getAmount());
			}
			else {
				out.writeShort(0);
				out.writeInt(0);
			}
		}
	}
}
