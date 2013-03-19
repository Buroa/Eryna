# The Sigmund_the_Merchant

# Define our values
id = 94
name = "Sigmund the Merchant"
items = {
  # item id => item amount,
  590 => 10,
  954 => 1,
  1931 => 30,
  2142 => 10,
  2309 => 10,
  952 => 10,
  36 => 10,
  1755 => 10,
  229 => 10,
  227 => 10,
  1925 => 10,
  1944 => 10,
  1942 => 10,
  233 => 10,
  2347 => 10,
  1929 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)