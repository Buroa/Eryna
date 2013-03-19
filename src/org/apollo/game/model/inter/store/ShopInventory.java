package org.apollo.game.model.inter.store;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;

/**
 * An {@link Inventory} which a {@link Shop} can use.
 * @author Steve
 */
public final class ShopInventory extends Inventory {

	/**
	 * The shop inventory id.
	 */
	public static final int SHOP_INVENTORY_ID = 3900;

	/**
	 * The list of bulked items.
	 */
	private final Map<Integer, Integer> bulk_items = new HashMap<Integer, Integer>();

	/**
	 * Initializes a shop inventory.
	 * @param capacity The capacity of this shop.
	 */
	public ShopInventory(int capacity) {
		super(capacity);
	}

	/**
	 * Initializes a shop inventory.
	 * @param capacity The capacity.
	 * @param mode The mode.
	 */
	public ShopInventory(int capacity, StackMode mode) {
		super(capacity, mode);
	}

	/**
	 * Adds a bulk item into the shop.
	 * @param item The item to bulk.
	 */
	public void bulk(Item item) {
		bulk_items.put(item.getId(), item.getAmount());
	}

	@Override
	public boolean contains(int item) {
		return contains(item, 0);
	}

	/**
	 * Returns true if the store contains the bulked item.
	 * @param item The item to get the value for.
	 * @return True if value is contained in bulk, false if otherwise.
	 */
	public boolean containsBulk(int item) {
		return bulk_items.containsKey(item);
	}

	/**
	 * Processes the items in this shop.
	 */
	public void process() {
		for (final Item item : toList()) {
			if (item == null)
				continue;
			if (!bulk_items.containsKey(item.getId())) {
				final int slot = getSlot(item.getId());
				if (item.getAmount() == 0)
					set(slot, null);
				else
					set(slot, new Item(item.getId(), item.getAmount() - 1));
			} else {
				final int value = bulk_items.get(item.getId());
				if (item.getAmount() < value) {
					final int slot = getSlot(item.getId());
					set(slot, new Item(item.getId(), item.getAmount() + 1));
				} else if (item.getAmount() > value) {
					final int slot = getSlot(item.getId());
					set(slot, new Item(item.getId(), item.getAmount() - 1));
				}
			}
		}
	}

}
