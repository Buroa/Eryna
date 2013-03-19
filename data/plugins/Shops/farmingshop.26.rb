# The Farming_Shop

# Define our values
id = 26
name = "Farming Shop"
items = {
  # item id => item amount,
  5376 => 10,
  6032 => 10,
  5418 => 10,
  6036 => 10,
  5350 => 10,
  5341 => 10,
  5329 => 10,
  5343 => 10,
  952 => 10,
  5325 => 10,
  1925 => 10,
  5331 => 10,
  5996 => 0,
  6006 => 0,
  1965 => 0,
  5994 => 0,
  5931 => 0,
  6000 => 0,
  1957 => 0,
  1942 => 0,
  5504 => 0,
  5986 => 0,
  1982 => 0,
  5982 => 0,
  6002 => 0,
  5998 => 0,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)