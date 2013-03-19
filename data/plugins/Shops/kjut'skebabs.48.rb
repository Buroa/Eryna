# The Kjut's_Kebabs

# Define our values
id = 48
name = "Kjut's Kebabs"
items = {
  # item id => item amount,
  1971 => 1000,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)