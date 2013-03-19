# The Jiminua's_Jungle_Store

# Define our values
id = 97
name = "Jiminua's Jungle Store"
items = {
  # item id => item amount,
  590 => 10,
  36 => 10,
  1931 => 30,
  954 => 10,
  1129 => 10,
  1059 => 10,
  1061 => 10,
  2142 => 10,
  2309 => 10,
  229 => 300,
  227 => 300,
  233 => 10,
  175 => 10,
  970 => 10,
  973 => 10,
  946 => 10,
  2347 => 10,
  975 => 10,
  1755 => 10,
  952 => 10,
  1351 => 10,
  1265 => 10,
  1349 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)