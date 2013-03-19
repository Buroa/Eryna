# The Rufus's Meat Emperium

# Define our values
id = 35
name = "Rufus's Meat Emperium"
items = {
  # item id => item amount,
  4287 => 100,
  4289 => 30,
  2134 => 30,
  331 => 0,
  335 => 0,
  383 => 0,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)