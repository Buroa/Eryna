# The Brians's_Archery_Shop

# Define our values
id = 7
name = "Brians's Archery Shop"
items = {
  # item id => item amount,
  886 => 100,
  888 => 50,
  890 => 30,
  843 => 10,
  845 => 10,
  849 => 10,
  847 => 10,
  853 => 10,
  851 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)