# The Brian's Battleaxe Bazaar

# Define our values
id = 9
name = "Brian's Battleaxe Bazaar"
items = {
  # item id => item amount,
  1375 => 10,
  1363 => 10,
  1365 => 8,
  1367 => 6,
  1369 => 5,
  1371 => 4,
  1373 => 2,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)