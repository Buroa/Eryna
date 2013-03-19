# The Louie_Legs

# Define our values
id = 64
name = "Louie Legs"
items = {
  # item id => item amount,
  1075 => 10,
  1067 => 10,
  1069 => 10,
  1071 => 10,
  1073 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)