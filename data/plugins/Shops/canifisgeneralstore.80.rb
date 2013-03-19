# The Canifis_General_Store

# Define our values
id = 80
name = "Canifis General Store"
items = {
  # item id => item amount,
  1733 => 10,
  1734 => 1000,
  1931 => 30,
  1925 => 30,
  1935 => 10,
  590 => 10,
  1755 => 10,
  2347 => 10,
  3377 => 10,
  946 => 10,
}
type = STORE_MIXED

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)