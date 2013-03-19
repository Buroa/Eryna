# The The_Lighthouse_Store

# Define our values
id = 86
name = "The Lighthouse Store"
items = {
  # item id => item amount,
  954 => 10,
  2347 => 10,
  1755 => 10,
  946 => 10,
  952 => 10,
  590 => 10,
  36 => 10,
  273 => 10,
  233 => 10,
  1931 => 30,
  1925 => 30,
  1929 => 10,
  1935 => 10,
  1937 => 300,
  229 => 10,
  227 => 10,
  2019 => 10,
  2021 => 10,
  2015 => 10,
  1915 => 10,
  2017 => 10,
  1909 => 10,
  1913 => 10,
  1907 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)