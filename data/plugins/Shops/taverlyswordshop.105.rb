# The Taverly_Sword_Shop

# Define our values
id = 105
name = "Taverly Sword Shop"
items = {
  # item id => item amount,
  1307 => 10,
  1309 => 10,
  1311 => 10,
  1313 => 10,
  1315 => 10,
  1317 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)