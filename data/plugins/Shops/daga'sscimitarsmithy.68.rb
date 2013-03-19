# The Daga's Scimitar Smithy

# Define our values
id = 68
name = "Daga's Scimitar Smithy"
items = {
  # item id => item amount,
  1321 => 10,
  1323 => 10,
  1325 => 10,
  1329 => 10,
  4587 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)