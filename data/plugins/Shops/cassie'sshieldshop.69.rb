# The Cassie's_Shield_Shop

# Define our values
id = 69
name = "Cassie's Shield Shop"
items = {
  # item id => item amount,
  1171 => 10,
  1173 => 10,
  1189 => 10,
  1175 => 10,
  1191 => 10,
  1177 => 10,
  1193 => 10,
  1181 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)