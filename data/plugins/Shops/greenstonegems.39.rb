# The Greenstone_Gems

# Define our values
id = 39
name = "Greenstone Gems"
items = {
  # item id => item amount,
  1607 => 0,
  1605 => 0,
  1603 => 0,
  1601 => 0,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)