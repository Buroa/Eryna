# The Battle_Runes

# Define our values
id = 55
name = "Battle Runes"
items = {
  # item id => item amount,
  554 => 100,
  555 => 100,
  556 => 100,
  557 => 100,
  559 => 100,
  558 => 100,
  562 => 30,
  560 => 30,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)