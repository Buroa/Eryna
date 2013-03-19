# The Gunslik's_Assorted_Items

# Define our values
id = 85
name = "Gunslik's Assorted Items"
items = {
  # item id => item amount,
  1935 => 10,
  1925 => 30,
  590 => 10,
  1755 => 10,
  2347 => 10,
  36 => 10,
  973 => 10,
  1059 => 10,
  229 => 300,
  233 => 10,
  954 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)