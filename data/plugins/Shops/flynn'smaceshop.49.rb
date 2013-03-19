# The Flynn's Mace Shop

# Define our values
id = 49
name = "Flynn's Mace Shop"
items = {
  # item id => item amount,
  1422 => 10,
  1420 => 10,
  1424 => 8,
  1428 => 6,
  1430 => 4,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)