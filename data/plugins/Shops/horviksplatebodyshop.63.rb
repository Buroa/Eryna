# The Horviks_Platebody_Shop

# Define our values
id = 63
name = "Horviks Platebody Shop"
items = {
  # item id => item amount,
  1103 => 10,
  1101 => 10,
  1139 => 10,
  1137 => 10,
  1115 => 10,
  1117 => 10,
  1173 => 10,
  1175 => 10,
  1075 => 10,
  1067 => 10,
  1087 => 10,
  1081 => 10,
  1119 => 10,
  1125 => 10,
  1121 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)