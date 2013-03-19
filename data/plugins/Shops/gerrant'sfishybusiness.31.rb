# The Gerrant's Fishy Business

# Define our values
id = 31
name = "Gerrant's Fishy Business"
items = {
  # item id => item amount,
  314 => 1000,
  313 => 1000,
  303 => 100,
  307 => 100,
  309 => 100,
  301 => 100,
  311 => 100,
  317 => 500,
  327 => 500,
  345 => 500,
  321 => 500,
  335 => 500,
  349 => 500,
  331 => 500,
  359 => 500,
  377 => 500,
  371 => 500,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)