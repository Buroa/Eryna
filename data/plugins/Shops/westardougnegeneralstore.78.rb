# The West_Ardougne_General_Store

# Define our values
id = 78
name = "West Ardougne General Store"
items = {
  # item id => item amount,
  1931 => 10,
  954 => 10,
  1265 => 10,
  1925 => 30,
  590 => 10,
  1415 => 10,
  1061 => 10,
  841 => 10,
  882 => 100,
  329 => 10,
  2327 => 10,
  2309 => 10,
  2142 => 10,
}
type = STORE_MIXED

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)