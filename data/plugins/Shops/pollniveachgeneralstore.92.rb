# The Pollniveach_General_Store

# Define our values
id = 92
name = "Pollniveach General Store"
items = {
  # item id => item amount,
  1931 => 30,
  1935 => 10,
  1825 => 10,
  1833 => 10,
  1837 => 10,
  1925 => 10,
  4593 => 10,
  4591 => 10,
  1985 => 10,
  2120 => 10,
  1982 => 10,
  1937 => 10,
  1921 => 10,
  1929 => 10,
}
type = STORE_MIXED

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)