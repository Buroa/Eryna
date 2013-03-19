# The Arnold's_Ecletic_Supplies

# Define our values
id = 91
name = "Arnold's Ecletic Supplies"
items = {
  # item id => item amount,
  303 => 10,
  311 => 1,
  2309 => 10,
  1925 => 30,
  1927 => 10,
  1733 => 10,
  1734 => 1000,
  1917 => 10,
  1785 => 10,
  946 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)