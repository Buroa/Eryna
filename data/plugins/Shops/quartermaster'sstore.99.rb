# The Quartermaster's_Store

# Define our values
id = 99
name = "Quartermaster's Store"
items = {
  # item id => item amount,
  1931 => 30,
  1935 => 10,
  1735 => 10,
  590 => 10,
  2309 => 10,
  3190 => 10,
  3192 => 10,
  3194 => 10,
  3196 => 10,
  3198 => 10,
  3200 => 10,
  3202 => 10,
  3204 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)