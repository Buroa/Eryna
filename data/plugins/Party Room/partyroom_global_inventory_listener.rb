require 'java'
java_import 'org.apollo.game.model.inv.InventoryAdapter'
java_import 'org.apollo.game.model.inter.InterfaceAdapter'
java_import 'org.apollo.game.model.SlottedItem'
java_import 'org.apollo.game.event.impl.UpdateSlottedItemsEvent'
java_import 'org.apollo.game.event.impl.UpdateItemsEvent'

PARTYROOM_INVENTORY_ID = 2273

class PartyroomGlobalListener < InventoryAdapter
  attr_reader :party_room

  def initialize(party_room)
    super()
    @party_room = party_room
  end

  def itemsUpdated(inventory)
    party_room.viewers.each do |player, deposit_inventory|
      player.send UpdateItemsEvent.new(2273, inventory.items)
    end
  end

  def itemUpdated(container, slot, item)
    party_room.viewers.each do |player, deposit_inventory|
      player.send UpdateSlottedItemsEvent.new(2273, SlottedItem.new(slot, item))
    end
  end

end

class PartyroomClosedListener < InterfaceAdapter
  attr_reader :party_room

  def initialize(party_room)
    super()
    @party_room = party_room
  end

  def interfaceClosed(player, manually)
    player.inventory.add_all party_room.viewers[player]
    party_room.remove_viewer player
    player.inventory.force_refresh
  end
end