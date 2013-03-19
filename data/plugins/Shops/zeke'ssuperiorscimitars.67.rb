# The Zeke's Superior Scimitars

# Define our values
id = 67
name = "Zeke's Superior Scimitars"
items = {
  # item id => item amount,
  1321 => 10,
  1323 => 10,
  1325 => 10,
  1329 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)