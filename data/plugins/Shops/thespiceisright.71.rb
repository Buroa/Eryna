# The The_Spice_is_Right

# Define our values
id = 71
name = "The Spice is Right"
items = {
  # item id => item amount,
  1931 => 30,
  2169 => 10,
  5970 => 0,
  175 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)