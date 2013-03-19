# The Ali_the_Kebab_seller

# Define our values
id = 46
name = "Ali the Kebab seller"
items = {
  # item id => item amount,
  1971 => 1000,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)