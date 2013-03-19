# The Zaff's_Superior_Staves

# Define our values
id = 72
name = "Zaff's Superior Staves"
items = {
  # item id => item amount,
  1379 => 10,
  1389 => 10,
  1381 => 10,
  1383 => 10,
  1385 => 10,
  1387 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)