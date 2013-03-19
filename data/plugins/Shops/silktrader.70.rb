# The Silk_Trader

# Define our values
id = 70
name = "Silk Trader"
items = {
  # item id => item amount,
  950 => 100,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)