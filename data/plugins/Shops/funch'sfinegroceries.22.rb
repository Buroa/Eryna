# The Funch's_Fine_Groceries

# Define our values
id = 22
name = "Funch's Fine Groceries"
items = {
  # item id => item amount,
  2021 => 10,
  2019 => 10,
  2015 => 10,
  2017 => 10,
  2114 => 10,
  2128 => 10,
  2108 => 10,
  2102 => 10,
  2120 => 10,
  2126 => 10,
  2025 => 10,
  1973 => 10,
  1975 => 10,
  2130 => 10,
  1927 => 10,
  946 => 10,
  2026 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)