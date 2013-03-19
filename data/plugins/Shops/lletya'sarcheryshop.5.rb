# The Lletya's_Archery_Shop

# Define our values
id = 5
name = "Lletya's Archery Shop"
items = {
  # item id => item amount,
  884 => 1000,
  886 => 1000,
  888 => 1000,
  890 => 500,
  892 => 300,
  843 => 10,
  845 => 10,
  837 => 10,
  849 => 10,
  847 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)