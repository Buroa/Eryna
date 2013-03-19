# The Frincos_Fabulous_Herb_Store

# Define our values
id = 42
name = "Frincos Fabulous Herb Store"
items = {
  # item id => item amount,
  229 => 10,
  233 => 10,
  221 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)