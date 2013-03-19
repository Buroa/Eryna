# The Grand_Tree_Groceries

# Define our values
id = 21
name = "Grand Tree Groceries"
items = {
  # item id => item amount,
  2171 => 10,
  2128 => 10,
  1933 => 10,
  2169 => 10,
  1957 => 10,
  1942 => 10,
  1965 => 10,
  1982 => 10,
  1985 => 10,
  2120 => 10,
  2108 => 10,
  2102 => 10,
  2114 => 10,
  2126 => 10,
  2025 => 10,
  1973 => 10,
  2130 => 10,
  1927 => 10,
  946 => 10,
  2167 => 10,
  2164 => 10,
  2165 => 10,
  2166 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)