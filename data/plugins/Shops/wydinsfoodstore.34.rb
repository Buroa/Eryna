# The Wydins_Food_Store

# Define our values
id = 34
name = "Wydins Food Store"
items = {
  # item id => item amount,
  1933 => 500,
  2132 => 10,
  2138 => 10,
  1965 => 10,
  1963 => 10,
  1951 => 10,
  2309 => 10,
  1973 => 10,
  1985 => 10,
  1982 => 10,
  1942 => 10,
  1550 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)