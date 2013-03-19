# The Wizards_Guild_Shop

# Define our values
id = 53
name = "Wizards Guild Shop"
items = {
  # item id => item amount,
  554 => 1000,
  555 => 1000,
  556 => 1000,
  557 => 1000,
  558 => 1000,
  559 => 1000,
  562 => 300,
  561 => 300,
  560 => 1000,
  563 => 100,
  565 => 100,
  1387 => 10,
  1383 => 10,
  1381 => 10,
  1385 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)