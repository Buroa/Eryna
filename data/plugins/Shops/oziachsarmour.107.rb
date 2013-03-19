# The Oziachs_Armour

# Define our values
id = 107
name = "Oziachs Armour"
items = {
  # item id => item amount,
  1127 => 10,
  1135 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)