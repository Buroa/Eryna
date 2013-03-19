# The Shantay_Store

# Define our values
id = 103
name = "Shantay Store"
items = {
  # item id => item amount,
  1854 => 100,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)