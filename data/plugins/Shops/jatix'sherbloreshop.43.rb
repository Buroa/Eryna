# The Jatix's_Herblore_Shop

# Define our values
id = 43
name = "Jatix's Herblore Shop"
items = {
  # item id => item amount,
  229 => 10,
  233 => 10,
  221 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)