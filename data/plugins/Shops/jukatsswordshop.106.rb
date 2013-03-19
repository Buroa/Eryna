# The Jukats_Sword_Shop

# Define our values
id = 106
name = "Jukats Sword Shop"
items = {
  # item id => item amount,
  1305 => 10,
  1215 => 30,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)