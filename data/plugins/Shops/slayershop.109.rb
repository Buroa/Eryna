# The Slayer_Shop

# Define our values
id = 109
name = "Slayer Shop"
items = {
  # item id => item amount,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)