# The Razmire's_General_Store

# Define our values
id = 89
name = "Razmire's General Store"
items = {
  # item id => item amount,
  1931 => 30,
  1935 => 10,
  1735 => 10,
  1925 => 10,
  590 => 10,
  1755 => 10,
  2347 => 10,
  3424 => 500,
  227 => 300,
  1933 => 10,
  3678 => 10,
}
type = STORE_MIXED

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)