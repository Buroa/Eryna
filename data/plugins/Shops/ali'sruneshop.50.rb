# The Ali's_rune_shop

# Define our values
id = 50
name = "Ali's rune shop"
items = {
  # item id => item amount,
  554 => 1000,
  555 => 1000,
  556 => 1000,
  557 => 1000,
  558 => 1000,
  559 => 1000,
  562 => 300,
  561 => 300,
  560 => 1000,
  565 => 100,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)