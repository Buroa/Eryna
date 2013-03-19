# The Bolkoy's_Village_Shop

# Define our values
id = 98
name = "Bolkoy's Village Shop"
items = {
  # item id => item amount,
  1931 => 30,
  1265 => 10,
  1935 => 10,
  1735 => 10,
  1925 => 30,
  590 => 10,
  1755 => 10,
  2347 => 10,
  882 => 1000,
  2142 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)