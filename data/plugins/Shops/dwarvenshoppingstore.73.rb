# The Dwarven_Shopping_Store

# Define our values
id = 73
name = "Dwarven Shopping Store"
items = {
  # item id => item amount,
  1931 => 30,
  1935 => 10,
  1735 => 10,
  1925 => 10,
  590 => 10,
  1755 => 10,
  2347 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)