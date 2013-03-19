# The Bob's Brilliant Axes

# Define our values
id = 8
name = "Bob's Brilliant Axes"
items = {
  # item id => item amount,
  1265 => 10,
  1351 => 10,
  1349 => 10,
  1353 => 6,
  1355 => 4,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)