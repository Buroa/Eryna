# The Aubury_Magic_Shop

# Define our values
id = 52
name = "Aubury Magic Shop"
items = {
  # item id => item amount,
  554 => 300,
  555 => 300,
  556 => 300,
  557 => 300,
  558 => 100,
  559 => 100,
  562 => 30,
  560 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)