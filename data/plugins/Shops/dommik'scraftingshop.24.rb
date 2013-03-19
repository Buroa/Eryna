# The Dommik's Crafting Shop

# Define our values
id = 24
name = "Dommik's Crafting Shop"
items = {
  # item id => item amount,
  1755 => 100,
  1733 => 100,
  1734 => 10000,
  5523 => 100,
  1592 => 100,
  1595 => 100,
  1597 => 100,
  1599 => 100,
  1592 => 100,
  1595 => 100,
  1597 => 100,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)