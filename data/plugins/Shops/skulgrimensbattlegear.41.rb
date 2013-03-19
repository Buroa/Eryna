# The Skulgrimens_Battle_Gear

# Define our values
id = 41
name = "Skulgrimens Battle Gear"
items = {
  # item id => item amount,
  1337 => 10,
  1335 => 10,
  1339 => 10,
  1341 => 10,
  1343 => 10,
  1345 => 10,
  1347 => 10,
  3749 => 10,
  3751 => 10,
  3753 => 10,
  3755 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)