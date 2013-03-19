# This class holds the world shop adding.
# Specify the payment type by executing shop.set_payment(payment)
# while; payment extends org.apollo.game.model.inter.store.ShopPaymentType

# Default shop types.
# SHOP_UBUYONLY -> Unlimited buy only.
# SHOP_BUYANDSELL -> Like a regular store.

require 'java'
java_import 'org.apollo.game.model.inter.store.Shop'
java_import 'org.apollo.game.model.Item'
java_import 'org.apollo.game.model.def.ItemDefinition'
java_import 'org.apollo.game.model.World'

MAX_ITEM_ID = ItemDefinition.count

class Shops

 attr_reader :id, :name, :items, :type

  def initialize(id, name, items, type)
    @id = id
    @name = name
    @items = items
    @type = type
  end

end

def appendShop(shop)
  worldshop = Shop.new shop.name, shop.type
  World.world.stores.add_shop shop.id, worldshop
  shop.items.each do |item, amount|
    if item >= 0 and item < MAX_ITEM_ID
      worldshop.add_item Item.new(item, amount)
    end
  end
  return worldshop
end
