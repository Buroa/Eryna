# The Obli's_General_Store

# Define our values
id = 96
name = "Obli's General Store"
items = {
  # item id => item amount,
  590 => 10,
  229 => 10,
  233 => 10,
  1931 => 30,
  1351 => 10,
  1265 => 10,
  1349 => 10,
  1129 => 10,
  1059 => 10,
  2142 => 10,
  2309 => 10,
  952 => 10,
  36 => 10,
  1755 => 10,
  2347 => 10,
  970 => 10,
  973 => 10,
  227 => 10,
  975 => 10,
  954 => 10,
}
type = STORE_MIXED

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)