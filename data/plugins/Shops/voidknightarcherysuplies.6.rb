# The Void Knight Archery Suplies  

# Define our values
id = 6
name = "Void Knight Archery Suplies  "
items = {
  # item id => item amount,
  825 => 1000,
  826 => 1000,
  827 => 1000,
  828 => 1000,
  829 => 1000,
  830 => 1000,
  39 => 5000,
  40 => 5000,
  41 => 5000,
  42 => 5000,
  43 => 5000,
  44 => 5000,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)