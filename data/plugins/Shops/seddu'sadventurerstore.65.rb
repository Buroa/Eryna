# The Seddu's_Adventurer_Store

# Define our values
id = 65
name = "Seddu's Adventurer Store"
items = {
  # item id => item amount,
  1093 => 10,
  1079 => 10,
  1113 => 10,
  1099 => 10,
  1065 => 10,
  1193 => 10,
  1151 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)