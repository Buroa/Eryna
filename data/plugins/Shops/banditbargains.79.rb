# The Bandit_Bargains

# Define our values
id = 79
name = "Bandit Bargains"
items = {
  # item id => item amount,
  1831 => 30,
  1823 => 30,
  1937 => 10,
  1921 => 10,
  1935 => 10,
  1923 => 10,
  1925 => 30,
  1837 => 10,
  1833 => 10,
  1835 => 10,
  946 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)