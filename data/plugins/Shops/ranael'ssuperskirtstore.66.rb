# The Ranael's Super Skirt Store

# Define our values
id = 66
name = "Ranael's Super Skirt Store"
items = {
  # item id => item amount,
  1087 => 10,
  1081 => 10,
  1083 => 10,
  1089 => 10,
  1085 => 10,
  1091 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)