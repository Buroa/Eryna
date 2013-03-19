# The Lowe's Archery Emporium

# Define our values
id = 3
name = "Lowe's Archery Emporium"
items = {
  # item id => item amount,
  882 => 5000,
  884 => 5000,
  886 => 2000,
  888 => 1000,
  890 => 1000,
  837 => 10,
  841 => 10,
  839 => 10,
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