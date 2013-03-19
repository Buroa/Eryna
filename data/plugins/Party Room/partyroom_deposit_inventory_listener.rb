require 'java'
java_import 'org.apollo.game.model.inv.InventoryAdapter'
java_import 'org.apollo.game.model.SlottedItem'
java_import 'org.apollo.game.event.impl.UpdateSlottedItemsEvent'
java_import 'org.apollo.game.event.impl.UpdateItemsEvent'

class PartyroomPlayerListener < InventoryAdapter
  attr_reader :player

  def initialize(player)
    super()
    @player = player
  end

  def itemsUpdated(inventory)
    player.send UpdateItemsEvent.new(2274, inventory.items)
  end

  def itemUpdated(container, slot, item)
    player.send UpdateSlottedItemsEvent.new(2274, SlottedItem.new(slot, item))
  end
end