# The Clothes_Store

# Define our values
id = 15
name = "Clothes Store"
items = {
  # item id => item amount,
  1005 => 10,
  1129 => 10,
  1059 => 10,
  1061 => 10,
  1757 => 10,
  1013 => 10,
  1015 => 10,
  578 => 10,
  1007 => 10,
  950 => 10,
  426 => 10,
  428 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)