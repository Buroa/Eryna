# The Candle_Shop

# Define our values
id = 10
name = "Candle Shop"
items = {
  # item id => item amount,
  36 => 10,
  38 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)