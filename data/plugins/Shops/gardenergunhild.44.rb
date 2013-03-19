# The Gardener_Gunhild

# Define our values
id = 44
name = "Gardener Gunhild"
items = {
  # item id => item amount,
  3899 => 100,
  5341 => 100,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)