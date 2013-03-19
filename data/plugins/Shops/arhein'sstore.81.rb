# The Arhein's_Store

# Define our values
id = 81
name = "Arhein's Store"
items = {
  # item id => item amount,
  1925 => 30,
  1265 => 10,
  1923 => 10,
  1887 => 10,
  590 => 10,
  1755 => 10,
  2347 => 10,
  954 => 10,
  1931 => 30,
  946 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)