package org.apollo.util;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;

/**
 * A utility for combat.
 * @author Steve
 */
public final class CombatUtil {

	/**
	 * Gets the items kept on death.
	 * @param keep The items to keep.
	 * @param items The item inventory.
	 * @return The inventory of the kept items.
	 */
	public static Inventory getItemsKeptOnDeath(int keep, Inventory items) {
		final PriorityQueue<Item> allItems = new PriorityQueue<Item>(1, new Comparator<Item>() {
			@Override
			public int compare(Item a, Item b) {
				return a.getDefinition().getValue() * a.getAmount() - b.getDefinition().getValue() * b.getAmount();
			}
		});
		for (final Item item : items)
			allItems.add(item);
		final Inventory keptItems = new Inventory(keep);
		while (keptItems.size() < keep && allItems.size() > 0)
			keptItems.add(allItems.poll());
		return keptItems;
	}

	/**
	 * Gets the items kept on death.
	 * @param keep The items to keep.
	 * @param items The item inventory.
	 * @return The inventory of kept items.
	 */
	public static Inventory getNpcGroundItems(int keep, Inventory items) {
		final PriorityQueue<Item> allItems = new PriorityQueue<Item>(1, new Comparator<Item>() {
			@Override
			public int compare(Item a, Item b) {
				return b.getDefinition().getValue() * b.getAmount() - a.getDefinition().getValue() * a.getAmount();
			}
		});
		for (final Item item : items)
			allItems.add(item);
		final Inventory keptItems = new Inventory(keep);
		while (keptItems.size() < keep && allItems.size() > 0)
			keptItems.add(allItems.poll());
		return keptItems;
	}

}
