# The Drogo's_Mining_Emporium

# Define our values
id = 60
name = "Drogo's Mining Emporium"
items = {
  # item id => item amount,
  2347 => 10,
  1265 => 10,
  436 => 0,
  438 => 0,
  440 => 0,
  453 => 0,
  2349 => 0,
  2351 => 0,
  2357 => 0,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)