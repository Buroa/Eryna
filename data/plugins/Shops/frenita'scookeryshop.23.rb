# The Frenita's_Cookery_Shop

# Define our values
id = 23
name = "Frenita's Cookery Shop"
items = {
  # item id => item amount,
  2313 => 10,
  1955 => 0,
  1887 => 10,
  1923 => 10,
  1942 => 10,
  590 => 10,
  1935 => 10,
  1931 => 30,
  1973 => 10,
  1933 => 500,
  1980 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)