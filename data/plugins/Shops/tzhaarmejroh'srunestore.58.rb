# The TzHaar_Mej_Roh's_Rune_Store

# Define our values
id = 58
name = "TzHaar Mej Roh's Rune Store"
items = {
  # item id => item amount,
  554 => 1000,
  555 => 1000,
  556 => 1000,
  557 => 1000,
  558 => 1000,
  559 => 1000,
  562 => 1000,
  560 => 1000,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)