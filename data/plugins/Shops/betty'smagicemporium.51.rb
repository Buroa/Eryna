# The Betty's_Magic_emporium

# Define our values
id = 51
name = "Betty's Magic emporium"
items = {
  # item id => item amount,
  554 => 300,
  555 => 300,
  556 => 300,
  557 => 100,
  558 => 100,
  562 => 30,
  560 => 10,
  221 => 100,
  579 => 10,
  1017 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)