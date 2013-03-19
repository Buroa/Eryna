# The Amulet_Store

# Define our values
id = 2
name = "Amulet Store"
items = {
  # item id => item amount,
  1718 => 100,
  1727 => 100,
  1729 => 100,
  1725 => 100,
  1731 => 100,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)