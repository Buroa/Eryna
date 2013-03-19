# The Pickaxe_is_Mine

# Define our values
id = 61
name = "Pickaxe is Mine"
items = {
  # item id => item amount,
  1265 => 10,
  1269 => 10,
  1273 => 10,
  1271 => 10,
  1275 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)