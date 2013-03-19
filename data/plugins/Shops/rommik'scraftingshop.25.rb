# The Rommik's Crafting shop

# Define our values
id = 25
name = "Rommik's Crafting shop"
items = {
  # item id => item amount,
  1755 => 10,
  1592 => 10,
  1597 => 10,
  1595 => 10,
  1733 => 50,
  1734 => 1000,
  1599 => 10,
  5523 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)