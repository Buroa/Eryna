package org.apollo.game.model;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.inv.InventoryListener;

/**
 * Represents an inventory - a collection of {@link Item}s.
 * @author Graham
 */
public class Inventory implements Cloneable, Iterable<Item> {

	/**
	 * An {@link Iterator} implementation for the {@link Inventory} class.
	 * @author Chris Fletcher
	 */
	private final class InventoryIterator implements Iterator<Item> {

		/**
		 * Index of element to be returned by subsequent call to next.
		 */
		private int cursor = 0;

		/**
		 * Index of element returned by most recent call to next or previous.
		 * Reset to -1 if this element is deleted by
		 * a call to remove.
		 */
		private int lastRet = -1;

		@Override
		public boolean hasNext() {
			return cursor != size();
		}

		@Override
		public Item next() {
			try {
				final int i = cursor;
				final Item next = Inventory.this.get(i);
				lastRet = i;
				cursor = i + 1;
				return next;
			} catch (final IndexOutOfBoundsException e) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			if (lastRet < 0)
				throw new IllegalStateException();
			try {
				Inventory.this.remove(lastRet);
				if (lastRet < cursor)
					cursor--;
				lastRet = -1;
			} catch (final IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}
	}

	/**
	 * An enumeration containing the different 'stacking modes' of an
	 * {@link Inventory}.
	 * @author Graham
	 */
	public enum StackMode {
		/**
		 * When in {@link #STACK_ALWAYS} mode, an {@link Inventory} will stack
		 * every single item, regardless of the
		 * settings of individual items.
		 */
		STACK_ALWAYS,
		/**
		 * When in {@link #STACK_STACKABLE_ITEMS} mode, an {@link Inventory}
		 * will stack items depending on their
		 * settings.
		 */
		STACK_STACKABLE_ITEMS,
		/**
		 * When in {@link #STACK_NEVER} mode, an {@link Inventory} will never
		 * stack items.
		 */
		STACK_NEVER;
	}

	/**
	 * A list of inventory listeners.
	 */
	private final List<InventoryListener> listeners = new ArrayList<InventoryListener>(7);

	/**
	 * The capacity of this inventory.
	 */
	private final int capacity;

	/**
	 * The items in this inventory.
	 */
	private Item[] items;

	/**
	 * The stacking mode.
	 */
	private final StackMode mode;

	/**
	 * The size of this inventory - the number of 'used slots'.
	 */
	private int size = 0;

	/**
	 * A flag indicating if events are being fired.
	 */
	private boolean firingEvents = true; // TODO: make this reentrant

	/**
	 * Creates an inventory.
	 * @param capacity The capacity.
	 * @throws IllegalArgumentException if the capacity is negative.
	 */
	public Inventory(int capacity) {
		this(capacity, StackMode.STACK_STACKABLE_ITEMS);
	}

	/**
	 * Creates an inventory.
	 * @param capacity The capacity.
	 * @param mode The stacking mode.
	 * @throws IllegalArgumentException if the capacity is negative.
	 * @throws NullPointerException if the mode is {@code null}.
	 */
	public Inventory(int capacity, StackMode mode) {
		if (capacity < 0)
			throw new IllegalArgumentException("capacity cannot be negative");
		if (mode == null)
			throw new NullPointerException("mode");
		this.capacity = capacity;
		this.items = new Item[capacity];
		this.mode = mode;
	}

	/**
	 * An alias for {@code add(id, 1)}.
	 * @param id The id.
	 * @return {@code true} if the item was added, {@code false} if there was
	 * not enough room.
	 */
	public boolean add(int id) {
		return add(id, 1) == 0;
	}

	/**
	 * An alias for {@link #add(Item)}.
	 * @param id The id.
	 * @param amount The amount.
	 * @return The amount that remains.
	 */
	public int add(int id, int amount) {
		final Item item = add(new Item(id, amount));
		return item != null ? item.getAmount() : 0;
	}

	/**
	 * Adds an item to this inventory. This will attempt to add as much of the
	 * item that is possible. If the item
	 * remains, it will be returned (in the case of stackable items, any
	 * quantity that remains in the stack is
	 * returned). If nothing remains, the method will return {@code null}. If
	 * something remains, the listener will also
	 * be notified which could be used, for example, to send a message to the
	 * player.
	 * @param item The item to add to this inventory.
	 * @return The item that remains if there is not enough room in the
	 * inventory. If nothing remains, {@code null}.
	 */
	public Item add(Item item) {
		final int id = item.getId();
		if (id > ItemDefinition.count())
			return null;
		final boolean stackable = isStackable(item.getDefinition());
		if (stackable) {
			for (int slot = 0; slot < capacity; slot++) {
				final Item other = items[slot];
				if (other != null && other.getId() == id) {
					final long total = (long) item.getAmount() + (long) other.getAmount();
					int amount;
					int remaining;
					if (total > Integer.MAX_VALUE) {
						// amount = (int) (total - Integer.MAX_VALUE);
						// remaining = (int) (total - amount);
						amount = Integer.MAX_VALUE;
						remaining = (int) (total - amount);
						notifyCapacityExceeded();
					}
					else {
						amount = (int) total;
						remaining = 0;
					}
					set(slot, new Item(id, amount));
					return remaining > 0 ? new Item(id, remaining) : null;
				}
			}
			for (int slot = 0; slot < capacity; slot++) {
				final Item other = items[slot];
				if (other == null) {
					set(slot, item);
					return null;
				}
			}
			notifyCapacityExceeded();
			return item;
		}
		int remaining = item.getAmount();
		stopFiringEvents();
		try {
			final Item single = new Item(item.getId(), 1);
			for (int slot = 0; slot < capacity; slot++)
				if (items[slot] == null) {
					remaining--;
					set(slot, single); // share the instances
					if (remaining <= 0)
						break;
				}
		} finally {
			startFiringEvents();
		}
		if (remaining != item.getAmount())
			notifyItemsUpdated();
		if (remaining > 0) {
			notifyCapacityExceeded();
			return new Item(item.getId(), remaining);
		}
		return null;
	}

	/**
	 * Attempts to add all items in the specified inventory to this one, without
	 * removing any from the specifed. This
	 * method has a slight performance benefit over the
	 * {@link #addInventory(Inventory)} method, at the cost of not
	 * knowing what items weren't added.
	 * @param inventory The inventory to add the items of.
	 * @return {@code true} if succesfully added, {@code false} if in any case
	 * an item cannot be added.
	 */
	public boolean addAll(Inventory inventory) {
		if (inventory.size == 0)
			return true;
		final boolean oldFiringEvents = firingEvents;
		firingEvents = inventory.size == 1;
		try {
			for (final Item item : inventory.items)
				if (item != null && add(item) != null)
					return false;
		} finally {
			firingEvents = oldFiringEvents;
		}
		return true;
	}

	/**
	 * Attempts to add all items in the specified inventory to this one, without
	 * removing any from the specified.
	 * @param inventory The inventory to add the items of.
	 * @return An array of items which have not been added ({@code null} if
	 * everything was added).
	 */
	public Item[] addInventory(Inventory inventory) {
		final List<Item> remainder = new ArrayList<Item>(capacity - size);
		for (final Item item : inventory.items) {
			final Item remaining = add(item);
			if (remaining != null)
				remainder.add(remaining);
		}
		return remainder.isEmpty() ? null : (Item[]) remainder.toArray();
	}

	/**
	 * Adds a listener.
	 * @param listener The listener to add.
	 */
	public void addListener(InventoryListener listener) {
		listeners.add(listener);
	}

	/**
	 * Gets the capacity of this inventory.
	 * @return The capacity.
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * Checks the bounds of the specified slot.
	 * @param slot The slot.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
	 */
	private void checkBounds(int slot) {
		if (slot < 0 || slot >= capacity)
			throw new IndexOutOfBoundsException("slot out of bounds");
	}

	/**
	 * Clears the inventory.
	 */
	public void clear() {
		items = new Item[capacity];
		size = 0;
		notifyItemsUpdated();
	}

	/**
	 * Creates a copy of this inventory. Listeners are not copied, they must be
	 * added again yourself! This is so cloned
	 * copies don't send updates to their counterparts.
	 */
	@Override
	public Inventory clone() {
		final Inventory copy = new Inventory(capacity, mode);
		System.arraycopy(items, 0, copy.items, 0, capacity);
		copy.size = size;
		return copy;
	}

	/**
	 * Checks if this inventory contains an item with the specified id.
	 * @param id The item's id.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(int id) {
		return contains(id, 1);
	}

	/**
	 * Checks if the inventory contains at least the specified amount of the
	 * specified id.
	 * @param id The item's id.
	 * @param amount The amount.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean contains(int id, int amount) {
		int ctr = 0;
		if (id > ItemDefinition.count())
			return false;
		if (isStackable(ItemDefinition.forId(id)))
			for (int i = 0; i < capacity && ctr <= size; i++) {
				final Item item = items[i];
				if (item != null) {
					if (item.getId() == id)
						return item.getAmount() >= amount;
					ctr++;
				}
			}
		else {
			int count = 0;
			for (int i = 0; i < capacity && ctr <= size; i++) {
				final Item item = items[i];
				if (item != null) {
					if (item.getId() == id)
						if (++count >= amount)
							return true;
					ctr++;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if this inventory contains the specified item.
	 * @param item The item to check for.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean contains(Item item) {
		return contains(item.getId(), item.getAmount());
	}

	/**
	 * Checks if this inventory contains all of the items.
	 * @param items The items to check for.
	 * @return True if the inventory contains all, false if otherwise.
	 */
	public boolean contains(List<Item> items) {
		for (int i = 0; i < items.size(); i++) {
			final Item item = items.get(i);
			if (!contains(item))
				return false;
		}
		return true;
	}

	/**
	 * Checks if this inventory contains at least one of the specified ids.
	 * @param ids The ids to check for.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean containsAny(int... ids) {
		int ctr = 0;
		for (int i = 0; i < capacity && ctr <= size; i++) {
			final Item item = items[i];
			if (item != null) {
				final int itemId = item.getId();
				for (final int id : ids)
					if (itemId == id)
						return true;
				ctr++;
			}
		}
		return false;
	}

	/**
	 * Forces the capacity to exceeded event to be fired.
	 */
	public void forceCapacityExceeded() {
		notifyCapacityExceeded();
	}

	/**
	 * Forces the refresh of this inventory.
	 */
	public void forceRefresh() {
		notifyItemsUpdated();
	}

	/**
	 * Forces a refresh of a specific slot.
	 * @param slot The slot.
	 */
	public void forceRefresh(int slot) {
		notifyItemUpdated(slot);
	}

	/**
	 * Gets the number of free slots.
	 * @return The number of free slots.
	 */
	public int freeSlots() {
		return capacity - size;
	}

	/**
	 * Calculates the amount of free spaces this inventory still has left for
	 * the specified id.
	 * @param id The item's id.
	 * @return The amount of items with the specified id that can still be added
	 * to this inventory.
	 */
	public int freeSpace(int id) {
		if (isStackable(ItemDefinition.forId(id))) {
			boolean freeSlot = false;
			for (final Item item : items)
				if (item != null) {
					if (item.getId() == id)
						return Integer.MAX_VALUE - item.getAmount();
				}
				else {
					freeSlot = true;
					break;
				}
			if (freeSlot)
				return Integer.MAX_VALUE;
		}
		else
			return freeSlots();
		return 0;
	}

	/**
	 * Gets the item in the specified slot.
	 * @param slot The slot.
	 * @return The item, or {@code null} if the slot is empty.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
	 */
	public Item get(int slot) {
		checkBounds(slot);
		return items[slot];
	}

	/**
	 * Gets an array of all free slot values in this inventory.
	 * @return An array of free slots, or a {@code 0-length} array if there
	 * aren't any.
	 */
	public int[] getFreeSlots() {
		final int count = freeSlots();
		if (count == 0)
			return new int[0];
		final int[] slots = new int[count];
		int ptr = -1;
		for (int i = 0; i < capacity; i++)
			if (items[i] == null)
				slots[ptr++] = i;
		return slots;
	}

	/**
	 * Gets the amount of items with the specified id in this inventory.
	 * @param id The id.
	 * @return The number of matching items, or {@code 0} if none were found.
	 */
	public int getItemCount(int id) {
		if (isStackable(ItemDefinition.forId(id))) {
			final int slot = getSlot(id);
			if (slot != -1)
				return items[slot].getAmount();
			return 0;
		}
		int amount = 0, ctr = 0;
		for (int i = 0; i < capacity && ctr <= size; i++) {
			final Item item = items[i];
			if (item != null) {
				if (item.getId() == id)
					amount++;
				ctr++;
			}
		}
		return amount;
	}

	/**
	 * Gets the amount of items with the specified id in this inventory,
	 * starting at the suggested slot. This method
	 * will prove to be much more efficient if you already know the slot, and
	 * the item is stackable in this inventory.
	 * @param id The id.
	 * @param suggestedSlot The suggested slot.
	 * @return The number of matching items, or {@code 0} if none were found.
	 */
	public int getItemCount(int id, int suggestedSlot) {
		final Item item = items[suggestedSlot];
		if (item != null && item.getId() == id)
			if (isStackable(item.getDefinition()))
				return item.getAmount();
		return getItemCount(id);
	}

	/**
	 * Gets a clone of the items array.
	 * @return A clone of the items array.
	 */
	public Item[] getItems() {
		return items.clone();
	}

	/**
	 * Gets the inventory slot for the specified id.
	 * @param id The id.
	 * @return The first found slot (starting at 0) containing the specified
	 * item, or {@code -1} if none of the slots
	 * matched the conditions.
	 */
	public int getSlot(int id) {
		int ctr = 0;
		for (int slot = 0; slot < capacity && ctr <= size; slot++) {
			final Item item = items[slot];
			if (item != null) {
				if (item.getId() == id)
					return slot;
				ctr++;
			}
		}
		return -1;
	}

	/**
	 * Checks if the inventory has enough space.
	 * @param item The item.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean hasRoomFor(Item item) {
		return freeSpace(item.getId()) > 0;
	}

	/**
	 * Checks if the inventory has room for the items.
	 * @param items The items.
	 * @return True if has room, false if not.
	 */
	public boolean hasRoomFor(Item[] items) {
		for (final Item item : items)
			if (item != null && !hasRoomFor(item))
				return false;
		return true;
	}

	/**
	 * Checks if the item specified by the definition should be stacked.
	 * @param def The definition.
	 * @return {@code true} if the item should be stacked, {@code false}
	 * otherwise.
	 */
	private boolean isStackable(ItemDefinition def) {
		if (mode == StackMode.STACK_ALWAYS)
			return true;
		else if (mode == StackMode.STACK_STACKABLE_ITEMS)
			return def.isStackable();
		else
			return false;
	}

	@Override
	public Iterator<Item> iterator() {
		return new InventoryIterator();
	}

	/**
	 * Notifies listeners that the capacity of this inventory has been exceeded.
	 */
	private void notifyCapacityExceeded() {
		if (firingEvents)
			for (final InventoryListener listener : listeners)
				listener.capacityExceeded(this);
	}

	/**
	 * Notifies listeners that all the items have been updated.
	 */
	private void notifyItemsUpdated() {
		if (firingEvents)
			for (final InventoryListener listener : listeners)
				listener.itemsUpdated(this);
	}

	/**
	 * Notifies listeners that the specified slot has been updated.
	 * @param slot The slot.
	 */
	private void notifyItemUpdated(int slot) {
		if (firingEvents) {
			final Item item = items[slot];
			for (final InventoryListener listener : listeners)
				listener.itemUpdated(this, slot, item);
		}
	}

	/**
	 * Removes one item with the specified id.
	 * @param id The id.
	 * @return {@code true} if the item was removed, {@code false} otherwise.
	 */
	public boolean remove(int id) {
		return remove(id, 1) == 1;
	}

	/**
	 * Removes {@code amount} of the item with the specified {@code id}. If the
	 * item is stackable, it will remove it
	 * from the stack. If not, it'll remove {@code amount} items.
	 * @param id The id.
	 * @param amount The amount.
	 * @return The amount that was removed.
	 */
	public int remove(int id, int amount) {
		if (id > ItemDefinition.count())
			return 0;
		final ItemDefinition def = ItemDefinition.forId(id);
		final boolean stackable = isStackable(def);
		if (stackable) {
			for (int slot = 0; slot < capacity; slot++) {
				final Item item = items[slot];
				if (item != null && item.getId() == id)
					if (amount >= item.getAmount()) {
						set(slot, null);
						return item.getAmount();
					}
					else {
						final int newAmount = item.getAmount() - amount;
						set(slot, new Item(item.getId(), newAmount));
						return amount;
					}
			}
			return 0;
		}
		int removed = 0;
		for (int slot = 0; slot < capacity; slot++) {
			final Item item = items[slot];
			if (item != null && item.getId() == id) {
				set(slot, null);
				removed++;
			}
			if (removed >= amount)
				break;
		}
		return removed;
	}

	/**
	 * An alias for {@code remove(item.getId(), item.getAmount())}.
	 * @param item The item to remove.
	 * @return The amount that was removed.
	 */
	public int remove(Item item) {
		return remove(item.getId(), item.getAmount());
	}

	/**
	 * Removes all the items.
	 * @param items The items to remove.
	 */
	public void remove(List<Item> items) {
		for (final Item item : items)
			remove(item);
	}

	/**
	 * Attempts to remove all items in the specified inventory to this one,
	 * without removing any from the specifed. This
	 * method has a slight performance benefit over the
	 * {@link #addInventory(Inventory)} method, at the cost of not
	 * knowing what items weren't added.
	 * @param inventory The inventory of items to remove.
	 * @return {@code true} if succesfully removed, {@code false} if in any case
	 * an item cannot be removed.
	 */
	public boolean removeAll(Inventory inventory) {
		if (inventory.size == 0)
			return true;
		final boolean oldFiringEvents = firingEvents;
		firingEvents = inventory.size == 1;
		try {
			for (final Item item : inventory.items)
				if (item != null && remove(item) != item.getAmount())
					return false;
		} finally {
			firingEvents = oldFiringEvents;
		}
		return true;
	}

	/**
	 * Removes all the listeners.
	 */
	public void removeAllListeners() {
		listeners.clear();
	}

	/**
	 * Attempts to remove all items in the specified inventory to this one,
	 * without removing any from the specified.
	 * @param inventory The inventory of items to remove.
	 * @return An array of items which have not been removed ({@code null} if
	 * everything was removed).
	 */
	public Item[] removeInventory(Inventory inventory) {
		final List<Item> remainder = new ArrayList<Item>(capacity - size);
		for (final Item item : inventory.items) {
			final int remaining = remove(item);
			if (remaining != item.getAmount())
				remainder.add(new Item(item.getId(), remaining));
		}
		return remainder.isEmpty() ? null : (Item[]) remainder.toArray();
	}

	/**
	 * Removes a listener.
	 * @param listener The listener to remove.
	 */
	public void removeListener(InventoryListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Removes {@code amount} of the item at the specified {@code slot}. If the
	 * item is not stacked, it will only remove
	 * the single item at the slot (meaning it will ignore any amount higher
	 * than 1). This means that this method will
	 * under no circumstances make any changes to other slots.
	 * @param slot The slot.
	 * @param amount The amount to remove.
	 * @return The amount that was removed (0 if nothing was removed).
	 */
	public int removeSlot(int slot, int amount) {
		if (amount == 0)
			return 0;
		final Item item = items[slot];
		if (item != null) {
			final int currentAmount = item.getAmount();
			int removed = currentAmount;
			if (removed > amount)
				removed = amount;
			final int remainder = currentAmount - removed;
			set(slot, remainder > 0 ? new Item(item.getId(), remainder) : null);
			return removed;
		}
		return 0;
	}

	/**
	 * Removes {@code amount} of the item at the specified {@code slot}, under
	 * the condition that this item matches the
	 * specified {@code id}. If the item is not stacked, it will only remove the
	 * single item at the slot (meaning it
	 * will ignore any amount higher than 1). This means that this method will
	 * under no circumstances make any changes
	 * to other slots.
	 * @param slot The slot.
	 * @param id The item id.
	 * @param amount The amount to remove.
	 * @return The amount that was removed (0 if nothing was removed).
	 */
	public int removeSlot(int slot, int id, int amount) {
		if (amount == 0)
			return 0;
		final Item item = items[slot];
		if (item != null && item.getId() == id) {
			final int currentAmount = item.getAmount();
			int removed = currentAmount;
			if (removed > amount)
				removed = amount;
			final int remainder = currentAmount - removed;
			set(slot, remainder > 0 ? new Item(item.getId(), remainder) : null);
			return removed;
		}
		return 0;
	}

	/**
	 * Removes {@code amount} of the item at the specified {@code slot}, under
	 * the condition that this item matches the
	 * specified {@code id}. If the item is not stacked, it will only remove the
	 * single item at the slot (meaning it
	 * will ignore any amount higher than 1). This means that this method will
	 * under no circumstances make any changes
	 * to other slots.
	 * @param slot The slot.
	 * @param item The item.
	 * @return The amount that was removed (0 if nothing was removed).
	 */
	public int removeSlot(int slot, Item item) {
		return removeSlot(slot, item.getId(), item.getAmount() == 0 ? 1 : item.getAmount());
	}

	/**
	 * Removes the item (if any) that is in the specified slot.
	 * @param slot
	 * @return The item that was in the slot.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
	 */
	public Item reset(int slot) {
		checkBounds(slot);
		final Item old = items[slot];
		if (old != null) {
			size--;
			items[slot] = null;
		}
		notifyItemUpdated(slot);
		return old;
	}

	/**
	 * Sets the item that is in the specified slot.
	 * @param slot The slot.
	 * @param item The item, or {@code null} to remove the item that is in the
	 * slot.
	 * @return The item that was in the slot.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
	 */
	public Item set(int slot, Item item) {
		if (item == null)
			return reset(slot);
		checkBounds(slot);
		final Item old = items[slot];
		if (old == null)
			size++;
		items[slot] = item;
		notifyItemUpdated(slot);
		return old;
	}

	/**
	 * Shifts all items to the top left of the container, leaving no gaps.
	 */
	public void shift() {
		final Item[] old = items;
		items = new Item[capacity];
		for (int i = 0, pos = 0; i < items.length; i++)
			if (old[i] != null)
				items[pos++] = old[i];
		if (firingEvents)
			notifyItemsUpdated();
	}

	/**
	 * Gets the size of this inventory - the number of used slots.
	 * @return The size.
	 */
	public int size() {
		return size;
	}

	/**
	 * Starts the firing of events.
	 */
	public void startFiringEvents() {
		firingEvents = true;
	}

	/**
	 * Stops the firing of events.
	 */
	public void stopFiringEvents() {
		firingEvents = false;
	}

	/**
	 * Swaps the two items at the specified slots.
	 * @param insert If the swap should be done in insertion mode.
	 * @param oldSlot The old slot.
	 * @param newSlot The new slot.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
	 */
	public void swap(boolean insert, int oldSlot, int newSlot) {
		checkBounds(oldSlot);
		checkBounds(newSlot);
		if (insert) {
			if (newSlot > oldSlot)
				for (int slot = oldSlot; slot < newSlot; slot++)
					swap(slot, slot + 1);
			else if (oldSlot > newSlot)
				for (int slot = oldSlot; slot > newSlot; slot--)
					swap(slot, slot - 1);
			forceRefresh();
		}
		else {
			final Item temp = items[oldSlot];
			items[oldSlot] = items[newSlot];
			items[newSlot] = temp;
			notifyItemsUpdated(); // TODO can we just fire for the two slots?
		}
	}

	/**
	 * Swaps the two items at the specified slots.
	 * @param oldSlot The old slot.
	 * @param newSlot The new slot.
	 * @throws IndexOutOfBoundsException if the slot is out of bounds.
	 */
	public void swap(int oldSlot, int newSlot) {
		swap(false, oldSlot, newSlot);
	}

	/**
	 * Creates a list representing this inventory.
	 * @return The list.
	 */
	public List<Item> toList() {
		final List<Item> list = new ArrayList<Item>(size);
		for (final Item item : items)
			list.add(item);
		return list;
	}
}
