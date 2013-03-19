# The Peksa's_Helmet_Shop

# Define our values
id = 40
name = "Peksa's Helmet Shop"
items = {
  # item id => item amount,
  1139 => 10,
  1137 => 10,
  1141 => 10,
  1143 => 10,
  1145 => 10,
  1155 => 10,
  1153 => 10,
  1157 => 10,
  1159 => 10,
  1161 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)