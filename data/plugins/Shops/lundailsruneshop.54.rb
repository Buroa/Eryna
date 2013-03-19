# The Lundails_Rune_Shop

# Define our values
id = 54
name = "Lundails Rune Shop"
items = {
  # item id => item amount,
  554 => 1000,
  555 => 1000,
  556 => 1000,
  557 => 1000,
  558 => 1000,
  559 => 1000,
  561 => 300,
  562 => 300,
  563 => 100,
  564 => 100,
  560 => 300,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)