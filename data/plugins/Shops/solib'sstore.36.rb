# The Solib's_Store

# Define our values
id = 36
name = "Solib's Store"
items = {
  # item id => item amount,
  4012 => 10,
  1963 => 10,
  4016 => 10,
  4014 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)