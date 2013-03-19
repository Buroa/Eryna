require 'java'
java_import 'org.apollo.game.model.Inventory'

PARTYROOM_WINDOW_ID = 2156

class Partyroom
  attr_reader :inventory, :balloons_inventory, :viewers

  def initialize
    @inventory = Inventory.new 126, Inventory::StackMode::STACK_ALWAYS
    @viewers = {}
  end

  def add_viewer(viewer, inventory)
    viewers[viewer] = inventory
  end

  def remove_viewer(viewer)
    viewers.delete viewer
  end

  def drop_activated
    @balloons_inventory = inventory.clone
    inventory.clear
  end
end

PARTY_ROOM = Partyroom.new
PARTY_ROOM.inventory.add_listener PartyroomGlobalListener.new(PARTY_ROOM)

on :event, :object_action do |ctx, player, event|
  if event.option == 1
    if event.id == 2417
      # creates a new inventory for depositing items
      deposit_inventory = Inventory.new 8
      deposit_inventory.add_listener PartyroomPlayerListener.new(player)
      PARTY_ROOM.add_viewer player, deposit_inventory

      # clear the deposit inventory
      deposit_inventory.force_refresh

      # updates the interface
      player.send UpdateItemsEvent.new(2273, PARTY_ROOM.inventory.items)
      player.send UpdateItemsEvent.new(5064, player.inventory.items)
      player.interface_set.open_window_with_sidebar PartyroomClosedListener.new(PARTY_ROOM), PARTYROOM_WINDOW_ID, 5063
    end
  end
end

# needs to be before the next item action
on :event, :item_action do |ctx, player, event|
  if player.interface_set.contains PARTYROOM_WINDOW_ID
    inventory = PARTY_ROOM.viewers[player]
    if inventory == nil
      ctx.break_handler_chain
      return
    end

    # set the inventory
    if event.interface_id == 5064
      inventory = player.inventory
      # is storing
    elsif event.interface_id == 2274
      # is withdrawing
    else
      return
    end

    # check the bound
    slot = event.slot
    if slot < 0 or slot > inventory.capacity
      ctx.break_handler_chain
      return
    end

    # check the slot
    item = inventory.get slot
    if item == nil or item.id != event.id
      ctx.break_handler_chain
      return
    end
  end
end

# after verification, we can continue
on :event, :item_action do |ctx, player, event|
  if player.interface_set.contains PARTYROOM_WINDOW_ID
    inventory = PARTY_ROOM.viewers[player]
    if inventory == nil
      ctx.break_handler_chain
      return
    end

    if event.interface_id == 5064
      inventory = player.inventory
      item = inventory.get event.slot
      capacity = PARTY_ROOM.viewers[player].size
      if PARTY_ROOM.viewers[player].add(item) == nil
        inventory.set event.slot, nil
        player.send UpdateSlottedItemsEvent.new(5064, SlottedItem.new(event.slot, nil))
      end
      # is storing
    elsif event.interface_id == 2274
      item = inventory.get event.slot
      if player.inventory.add(item) == nil
        inventory.set event.slot, nil
        player.send UpdateItemsEvent.new(5064, player.inventory.items)
      end
      # is withdrawing
    end
  end
end

on :button, 2246 do |player|
  if player.interface_set.contains PARTYROOM_WINDOW_ID
    inventory = PARTY_ROOM.viewers[player]
    if inventory == nil
      return
    end

    if inventory.size > 0
      PARTY_ROOM.inventory.add_all inventory
      PARTY_ROOM.inventory.force_refresh
      inventory.clear
    end
  end
end