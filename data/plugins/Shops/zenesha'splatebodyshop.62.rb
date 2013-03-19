# The Zenesha's_Platebody_Shop

# Define our values
id = 62
name = "Zenesha's Platebody Shop"
items = {
  # item id => item amount,
  1117 => 10,
  1115 => 10,
  1119 => 10,
  1125 => 10,
  1121 => 10,
  1123 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)