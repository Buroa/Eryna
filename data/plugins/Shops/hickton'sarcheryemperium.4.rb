# The Hickton's Archery Emperium

# Define our values
id = 4
name = "Hickton's Archery Emperium"
items = {
  # item id => item amount,
  882 => 2000,
  884 => 2000,
  886 => 2000,
  888 => 1000,
  890 => 800,
  39 => 1000,
  40 => 1000,
  41 => 1000,
  42 => 1000,
  43 => 500,
  44 => 500,
  841 => 50,
  839 => 50,
  843 => 50,
  847 => 50,
  845 => 50,
  851 => 50,
  849 => 50,
  853 => 50,
  851 => 50,
  857 => 50,
  855 => 50,
  1129 => 50,
  1133 => 50,
  1135 => 10,
  1095 => 50,
  1097 => 50,
  1099 => 20,
  1063 => 50,
  1065 => 20,
  1167 => 100,
  1169 => 100,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)