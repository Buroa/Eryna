# The Fishmonger

# Define our values
id = 33
name = "Fishmonger"
items = {
  # item id => item amount,
  305 => 10,
  309 => 10,
  307 => 10,
  311 => 10,
  301 => 10,
  313 => 1000,
  314 => 1000,
  305 => 10,
  317 => 0,
  327 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)