package org.apollo.game.model.skill.farming;

import org.apollo.game.model.Animation;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 25/02/12 Time: 16:39 To change
 * this template use File | Settings | File
 * Templates.
 */
@SuppressWarnings("javadoc")
public final class FarmingConstants {// todo farmers watching crops
	// payment to farmer
	// todo sack making with jute, sack filling with
	// items

	public enum Tools {

		BRONZE(1351, 1, 879, 8), IRON(1349, 1, 877, 7), STEEL(1353, 1, 875, 6), BLACK(1361, 1, 873, 5), MITH(1355, 1,
				871, 4), ADDY(1357, 1, 869, 3), RUNE(1360, 1, 867, 2);

		public static Tools forId(int tool) {
			for (final Tools tools : Tools.values())
				if (tools.getId() == tool)
					return tools;
			return null;
		}

		/**
		 * Gets the tool.
		 * @param player The player.
		 * @return The tool.
		 */
		public static Tools getTool(Player player) {
			Item tool = null;
			final Inventory equipment = player.getEquipment();
			final Inventory pac = player.getInventory();
			tool = equipment.get(EquipmentConstants.WEAPON);
			if (tool != null && forId(tool.getId()) != null)
				return forId(tool.getId());
			else
				for (final Item item : pac) {
					final Tools gt = forId(item.getId());
					if (gt != null)
						return gt;
				}
			return null;
		}

		private final int id;

		private final int level;

		private final int animation;

		private final int pulses;

		private Tools(int id, int level, int animation, int pulses) {
			this.id = id;
			this.level = level;
			this.animation = animation;
			this.pulses = pulses;
		}

		public Animation getAnimation() {
			return new Animation(animation);
		}

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public int getPulses() {
			return pulses;
		}

	}

	public static final int WATERING_CAN_ANIM = 2293;

	public static final int RAKING_ANIM = 2273;

	public static final int SPADE_ANIM = 830;

	public static final int SEED_DIBBING = 2291;

	public static final int PICKING_VEGETABLE_ANIM = 2282;

	public static final int PICKING_HERB_ANIM = 2279;

	public static final int PUTTING_COMPOST = 2283;

	public static final int CURING_ANIM = 2288;

	public static final int FILLING_POT_ANIM = 2287;

	public static final int PLANTING_POT_ANIM = 2272;

	public static final int PRUNING_ANIM = 2275;

	public static final int RAKE = 5341;

	public static final int SEED_DIBBER = 5343;

	public static final int SPADE = 952;

	public static final int TROWEL = 5325;

	public static final int SECATEURS = 5329;

	public static final int MAGIC_SECATEURS = 7409;

	public static final int[] WATERED_SAPPLING = { 5364, 5365, 5366, 5367, 5368, 5369, 5488, 5489, 5490, 5491, 5492,
			5493, 5494, 5495 };

	public static boolean inRangeArea(Position position1, Position position2, Position positionChecked) {
		final int x = position1.getX();
		final int y = position1.getY();
		final int x1 = position2.getX();
		final int y1 = position2.getY();
		return positionChecked.getX() >= x && positionChecked.getY() >= y && positionChecked.getX() <= x1
				&& positionChecked.getY() <= y1;
	}
}
