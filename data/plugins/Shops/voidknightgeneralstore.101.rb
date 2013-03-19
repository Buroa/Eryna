# The Void_Knight_General_Store

# Define our values
id = 101
name = "Void Knight General Store"
items = {
  # item id => item amount,
  1931 => 30,
  1935 => 10,
  1735 => 10,
  1925 => 30,
  1923 => 10,
  590 => 10,
  1755 => 10,
  2347 => 10,
  1351 => 10,
  7934 => 10,
}
type = STORE_MIXED

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)