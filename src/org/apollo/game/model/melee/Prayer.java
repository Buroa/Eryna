package org.apollo.game.model.melee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apollo.game.event.impl.ConfigEvent;
import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Character;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;

@SuppressWarnings("javadoc")
public final class Prayer {

	/**
	 * Represents prayers (Curses/Normals)
	 * @author Brandyn / Scu11
	 */
	public static enum Prayers {
		THICK_SKIN(new double[] { 5609, 1, 83, 3 }, DEFENCE_PRAYER, false),
		BURST_OF_STRENGTH(new double[] { 5610, 4, 84, 3 }, STRENGTH_PRAYER, false),
		CLARITY_OF_THOUGHT(new double[] { 5611, 7, 85, 3 }, ATTACK_PRAYER, false),
		ROCK_SKIN(new double[] { 5612, 10, 86, 6 }, DEFENCE_PRAYER, false),
		SUPERHUMAN_STRENGTH(new double[] { 5613, 13, 87, 6 }, STRENGTH_PRAYER, false),
		IMPROVED_REFLEXES(new double[] { 5614, 16, 88, 6 }, ATTACK_PRAYER, false),
		RAPID_RESTORE(new double[] { 5615, 19, 89, 1 }, -1, false),
		RAPID_HEAL(new double[] { 5616, 22, 90, 2 }, -1, false),
		PROTECT_ITEM(new double[] { 5617, 25, 91, 2 }, -1, false),
		STEEL_SKIN(new double[] { 5618, 28, 92, 12 }, DEFENCE_PRAYER, false),
		ULTIMATE_STRENGTH(new double[] { 5619, 31, 93, 12 }, STRENGTH_PRAYER, false),
		INCREDIBLE_REFLEXES(new double[] { 5620, 34, 94, 12 }, ATTACK_PRAYER, false),
		PROTECT_FROM_MAGIC(new double[] { 5621, 37, 95, 12, 2 }, OVERHEAD_PRAYER, false),
		PROTECT_FROM_MISSILES(new double[] { 5622, 40, 96, 12, 1 }, OVERHEAD_PRAYER, false),
		PROTECT_FROM_MELEE(new double[] { 5623, 43, 97, 12, 0 }, OVERHEAD_PRAYER, false),
		RETRIBUTION(new double[] { 683, 46, 98, 3, 3 }, OVERHEAD_PRAYER, false),
		REDEMPTION(new double[] { 684, 49, 99, 6, 5 }, OVERHEAD_PRAYER, false),
		SMITE(new double[] { 685, 52, 100, 18, 4 }, OVERHEAD_PRAYER, false),
		SHARP_EYE(new double[] { 70080, 8, 601, 12 }, RANGE_PRAYER | ATTACK_PRAYER | STRENGTH_PRAYER, false),
		MYSTIC_WILL(new double[] { 70082, 9, 602, 12 }, MAGIC_PRAYER | ATTACK_PRAYER | STRENGTH_PRAYER, false),
		HAWK_EYE(new double[] { 70084, 26, 603, 6 }, RANGE_PRAYER | ATTACK_PRAYER | STRENGTH_PRAYER, false),
		MYSTIC_LORE(new double[] { 70086, 27, 604, 6 }, MAGIC_PRAYER | ATTACK_PRAYER | STRENGTH_PRAYER, false),
		EAGLE_EYE(new double[] { 70088, 44, 605, 3 }, RANGE_PRAYER | ATTACK_PRAYER | STRENGTH_PRAYER, false),
		MYSTIC_MIGHT(new double[] { 70090, 45, 606, 3 }, MAGIC_PRAYER | ATTACK_PRAYER | STRENGTH_PRAYER, false),
		CHIVALRY(new double[] { 70092, 60, 607, 1.5 }, ATTACK_PRAYER | STRENGTH_PRAYER | DEFENCE_PRAYER, false),
		PIETY(new double[] { 70094, 70, 608, 1.5 }, ATTACK_PRAYER | STRENGTH_PRAYER | DEFENCE_PRAYER, false),
		PROTECT_ITEM_(new double[] { 87231, 50, 83, 18, 12567, 2213 }, -1, true),
		SAP_WARRIOR(new double[] { 87233, 50, 84, 2.57 }, SAP_PRAYER, true),
		SAP_RANGER(new double[] { 87235, 52, 85, 2.57 }, SAP_PRAYER, true),
		SAP_MAGE(new double[] { 87237, 54, 101, 2.57 }, SAP_PRAYER, true),
		SAP_SPIRIT(new double[] { 87239, 56, 102, 2.57 }, SAP_PRAYER, true),
		BERSERKER(new double[] { 87241, 59, 86, 18, 12589, 2266 }, -1, true),
		DEFLECT_SUMMON(new double[] { 87243, 62, 87, 3 }, OVERHEAD_PRAYER, true),
		DEFLECT_MAGIC(new double[] { 87245, 65, 88, 3, 10 }, OVERHEAD_PRAYER, true),
		DEFLECT_RANGE(new double[] { 87247, 68, 89, 3, 11 }, OVERHEAD_PRAYER, true),
		DEFLECT_MELEE(new double[] { 87249, 71, 90, 3, 9 }, OVERHEAD_PRAYER, true),
		LEECH_ATTACK(new double[] { 87251, 74, 91, 3 }, LEECH_PRAYER, true),
		LEECH_RANGE(new double[] { 87253, 76, 103, 3 }, LEECH_PRAYER, true),
		LEECH_MAGE(new double[] { 87255, 78, 104, 3 }, LEECH_PRAYER, true),
		LEECH_DEFENCE(new double[] { 88001, 80, 92, 3 }, LEECH_PRAYER, true),
		LEECH_STRENGTH(new double[] { 88003, 82, 93, 3 }, LEECH_PRAYER, true),
		LEECH_ENERGY(new double[] { 88005, 84, 94, 3.6 }, LEECH_PRAYER, true),
		LEECH_SPECIAL(new double[] { 88007, 86, 95, 3 }, LEECH_PRAYER, true),
		WRATH(new double[] { 88009, 89, 96, 12, 16 }, OVERHEAD_PRAYER, true),
		SOUL_SPLIT(new double[] { 88011, 92, 97, 2, 17 }, OVERHEAD_PRAYER, true),
		TURMOIL(new double[] { 88013, 95, 105, 2, 12565, 2226 }, SAP_PRAYER | LEECH_PRAYER, true);

		/**
		 * The prayers.
		 */
		private static Map<Integer, Prayers> prayers = new HashMap<Integer, Prayers>();

		/**
		 * Appends all of the prayer values into a map.
		 */
		static {
			for (final Prayers prayer : Prayers.values())
				prayers.put(prayer.id, prayer);
		}

		/**
		 * Gets the prayer for the given button id.
		 * @param prayer The prayer button id.
		 * @return The prayer containing the given button id.
		 */
		public static Prayers forId(int prayer) {
			return prayers.get(prayer);
		}

		/**
		 * Gets the prayer for the given name.
		 * @param name The prayer name.
		 * @return The prayer resolving to the given name.
		 */
		public static Prayers forName(String name) {
			return Prayers.valueOf(name.replace(" ", "_").toUpperCase());
		}

		private final int id;
		private final String name;
		private final int level;
		private final int config;
		private final int icon;
		private final double drain;
		private final int anim;
		private final int graphic;
		private final int prayMask;
		private final boolean curse;

		/**
		 * Creates a prayer with the given configuration.
		 * @param data The data containing configurations.
		 * @param prayerMask The prayer bit mask.
		 * @param curse True if curses, false if otherwise.
		 */
		private Prayers(double[] data, int prayerMask, boolean curse) {
			this.id = (int) data[0];
			this.level = (int) data[1];
			this.config = (int) data[2];
			this.drain = data[3];
			this.icon = (int) (data.length == 5 ? data[4] : -1);
			this.anim = data.length == 6 ? (int) data[4] : -1;
			this.graphic = data.length == 6 ? (int) data[5] : -1;
			this.name = this.toString();
			this.prayMask = prayerMask;
			this.curse = curse;
		}

		/**
		 * Gets the animation.
		 * @return The prayer animation.
		 */
		public Animation getAnimation() {
			return new Animation(anim);
		}

		public int getClientConfiguration() {
			return config;
		}

		public double getDrain() {
			return drain;
		}

		public Graphic getGraphic() {
			return new Graphic(graphic);
		}

		public int getHeadIcon() {
			return icon;
		}

		public int getLevelRequired() {
			return level;
		}

		public int getMask() {
			return prayMask;
		}

		public String getName() {
			return name;
		}

		public int getPrayerId() {
			return id;
		}

		public boolean isCurse() {
			return curse;
		}
	}

	private static final int OVERHEAD_PRAYER = 1;
	private static final int ATTACK_PRAYER = 2;
	private static final int STRENGTH_PRAYER = 4;
	private static final int RANGE_PRAYER = 8;
	private static final int MAGIC_PRAYER = 16;
	private static final int DEFENCE_PRAYER = 32;
	private static final int SAP_PRAYER = 64;
	private static final int LEECH_PRAYER = 128;

	/**
	 * Check for the extra prayers on, such as turning on Piety turns off all
	 * other strength boosting
	 * @param prayer The prayer toggled
	 */
	public static void checkExtraPrayers(Player player, Prayers prayer) {
		if (prayer.getMask() == -1)
			return;
		final boolean overheadPrayer = (prayer.getMask() & OVERHEAD_PRAYER) != 0;
		final boolean attackPrayer = (prayer.getMask() & ATTACK_PRAYER) != 0;
		final boolean strengthPrayer = (prayer.getMask() & STRENGTH_PRAYER) != 0;
		final boolean defencePrayer = (prayer.getMask() & DEFENCE_PRAYER) != 0;
		final boolean rangePrayer = (prayer.getMask() & RANGE_PRAYER) != 0;
		final boolean magicPrayer = (prayer.getMask() & MAGIC_PRAYER) != 0;
		// boolean sapPrayer = (prayer.getMask() & SAP_PRAYER) != 0;
		// boolean leechPrayer = (prayer.getMask() & LEECH_PRAYER) != 0;
		for (final Prayers p : Prayers.values()) {
			if (!player.getPrayers().contains(p) || p == prayer)
				continue;
			if (p.getMask() == -1)
				continue;
			if ((p.getMask() & OVERHEAD_PRAYER) != 0 && overheadPrayer)
				togglePrayer(player, p);
			if (!p.isCurse())
				if ((p.getMask() & ATTACK_PRAYER) != 0 && attackPrayer
						|| (p.getMask() & STRENGTH_PRAYER) != 0 && strengthPrayer
						|| (p.getMask() & DEFENCE_PRAYER) != 0 && defencePrayer
						|| (p.getMask() & RANGE_PRAYER) != 0 && rangePrayer
						|| (p.getMask() & MAGIC_PRAYER) != 0 && magicPrayer)
					togglePrayer(player, p);
		}
	}

	/**
	 * Clears the prayers.
	 * @param character The player to clear the prayers from.
	 */
	public static void clearPrayers(Character character, boolean off) {
		if (!off)
			for (final Prayers p : Prayers.values()) {
				character.send(new ConfigEvent(p.getClientConfiguration(), 0));
				character.getPrayers().remove(p);
			}
		else {
			final List<Prayers> toRemove = new ArrayList<Prayers>();
			for (final Prayers p : character.getPrayers()) {
				toRemove.add(p);
				character.send(new ConfigEvent(p.getClientConfiguration(), 0));
			}
			character.getPrayers().removeAll(toRemove);
			if (character.isControlling())
				character.sendMessage("You have run out of prayer points!");
		}
	}

	/**
	 * Drains the prayer.
	 * @param source The source.
	 */
	public static void drainPrayer(Character source) {
		double remove = 0;
		for (final Prayers p : source.getPrayers())
			remove += p.getDrain();
		if (remove > 0) {
			source.setPrayerDrain((int) (source.getPrayerDrain() + remove));
			final int bonus = (int) source.getBonuses().getBonuses().getPrayer();
			final int remove_prayer = source.getPrayerDrain() / (60 + bonus * 2);
			source.setPrayerDrain(source.getPrayerDrain() - remove_prayer * (60 + bonus * 2));
			if (remove_prayer < source.getSkillSet().getSkill(Skill.PRAYER).getCurrentLevel())
				source.getSkillSet().increaseSkill(Skill.PRAYER, -remove_prayer);
		}
	}

	/**
	 * Gets the bonuses for melee.
	 * @param source The player.
	 * @return The bonuses for melee.
	 */
	public static int getBonusMelee(Character source) {
		int i = 1;
		final List<Prayers> p = source.getPrayers();
		if (p.contains(Prayers.BURST_OF_STRENGTH))
			i += .05;
		if (p.contains(Prayers.SUPERHUMAN_STRENGTH))
			i += .1;
		if (p.contains(Prayers.ULTIMATE_STRENGTH))
			i += .15;
		if (p.contains(Prayers.CHIVALRY))
			i += .18;
		if (p.contains(Prayers.PIETY))
			i += .23;
		return i;
	}

	/**
	 * Gets the bonuses for range.
	 * @param source The player.
	 * @return The bonuses for range.
	 */
	public static int getBonusRange(Character source) {
		int i = 1;
		final List<Prayers> p = source.getPrayers();
		if (p.contains(Prayers.SHARP_EYE))
			i += .05;
		if (p.contains(Prayers.HAWK_EYE))
			i += .1;
		if (p.contains(Prayers.EAGLE_EYE))
			i += .15;
		return i;
	}

	/**
	 * Toggle a prayer, setting the headicon and checking level if turning on
	 * @param prayer The prayer to toggle
	 */
	public static void togglePrayer(Player player, Prayers prayer) {
		if (player.getPrayers().contains(prayer)) {
			player.getPrayers().remove(prayer);
			player.send(new ConfigEvent(prayer.getClientConfiguration(), 0));
			if (player.getAppearance().getSkull() != -1) {
				// player.getAppearance().setSkullIcon((byte) -1);
				// player.setAppearance(player.getAppearance());
			}
		} else {
			final Skill PRAYER = player.getSkillSet().getSkill(Skill.PRAYER);
			final int CURRENT = PRAYER.getCurrentLevel();
			final int MAXIMUM = PRAYER.getMaximumLevel();
			if (MAXIMUM < prayer.getLevelRequired()) {
				player.send(new ConfigEvent(prayer.getClientConfiguration(), 0));
				player.send(new SetInterfaceTextEvent(357, "You need a @blu@Prayer level of "
						+ prayer.getLevelRequired() + " to use " + prayer.getName() + "."));
				player.getInterfaceSet().openDialogue(356);
				return;
			}
			if (CURRENT <= 1) {
				player.send(new ConfigEvent(prayer.getClientConfiguration(), 0));
				player.sendMessage("You have no prayer points!");
				return;
			}
			/*
			 * if(client.isOnCurses() && !prayer.isCurse()) return;
			 * if(prayer.getName().startsWith("Protect") ||
			 * prayer.getName().startsWith("Deflect")) {
			 * if(System.currentTimeMillis() - client.stopPrayerDelay < 5000)
			 * {
			 * client.sendMessage("You have been injured and can't use this prayer!"
			 * );
			 * client.getPA().sendFrame36(prayer.getClientConfiguration(), 0);
			 * return; } if(prayer ==
			 * Prayers.PROTECT_FROM_MAGIC || prayer == Prayers.DEFLECT_MAGIC) {
			 * client.protMageDelay =
			 * System.currentTimeMillis(); } if(prayer ==
			 * Prayers.PROTECT_FROM_MISSILES || prayer ==
			 * Prayers.DEFLECT_RANGE) { client.protRangeDelay =
			 * System.currentTimeMillis(); } if(prayer ==
			 * Prayers.PROTECT_FROM_MELEE || prayer == Prayers.DEFLECT_MELEE) {
			 * client.protMeleeDelay =
			 * System.currentTimeMillis(); } }
			 */
			if (prayer.getAnimation().getId() != -1) {
				player.playAnimation(prayer.getAnimation());
				player.playGraphic(prayer.getGraphic());
			}
			player.getPrayers().add(prayer);
			checkExtraPrayers(player, prayer);
			if (prayer.getHeadIcon() != -1) {
				// player.getAppearance().setSkullIcon((byte)
				// prayer.getHeadIcon());
				// player.setAppearance(player.getAppearance());
			}
		}
	}

	/**
	 * Turns overhead prayers off.
	 */
	public static void turnOverheadsOff(Player player) {
		for (final Prayers p : Prayers.values())
			if ((p.getMask() & OVERHEAD_PRAYER) != 0 && player.getPrayers().contains(p))
				togglePrayer(player, p);
	}
}