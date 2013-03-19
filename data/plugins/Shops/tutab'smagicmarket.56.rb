# The Tutab's_Magic_Market

# Define our values
id = 56
name = "Tutab's Magic Market"
items = {
  # item id => item amount,
  554 => 1000,
  555 => 1000,
  556 => 1000,
  557 => 1000,
  563 => 100,
  221 => 10,
  4006 => 10,
  4023 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)