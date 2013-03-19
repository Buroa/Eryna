# The Ifaba's_General_Store

# Define our values
id = 76
name = "Ifaba's General Store"
items = {
  # item id => item amount,
  1931 => 30,
  1935 => 10,
  954 => 10,
  1925 => 30,
  590 => 10,
  2347 => 10,
}
type = STORE_MIXED

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)