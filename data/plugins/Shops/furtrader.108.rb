# The Fur_Trader

# Define our values
id = 108
name = "Fur Trader"
items = {
  # item id => item amount,
  948 => 10,
  958 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)