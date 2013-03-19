# The East_Ardougne_General_Store

# Define our values
id = 77
name = "East Ardougne General Store"
items = {
  # item id => item amount,
  227 => 10,
  1265 => 10,
  1349 => 10,
  2142 => 10,
  590 => 10,
  1759 => 100,
  882 => 1000,
  954 => 10,
  970 => 10,
  946 => 10,
  1935 => 10,
}
type = STORE_MIXED

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)