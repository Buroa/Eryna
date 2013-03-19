# The Wayne's Chains

# Define our values
id = 11
name = "Wayne's Chains"
items = {
  # item id => item amount,
  1103 => 10,
  1101 => 10,
  1105 => 10,
  1107 => 10,
  1109 => 8,
  1111 => 5,
  1113 => 2,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)