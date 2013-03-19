# The Range guild exchange store

# Define our values
id = 111
name = "Range guild exchange store"
items = {
  # item id => item amount,
  9244 => 100,
  9245 => 50,
  6543 => 1,
  2581 => 1,
  2577 => 1,
  6724 => 1,
  11235 => 1,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)