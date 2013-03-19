# The Sams_Cape_Shop

# Define our values
id = 110
name = "Sams Cape Shop"
items = {
  # item id => item amount,
  4333 => 10,
  4353 => 10,
  4373 => 10,
  4393 => 10,
  4413 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)