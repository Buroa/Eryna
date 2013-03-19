# The Shilo_Fishing_Store

# Define our values
id = 30
name = "Shilo Fishing Store"
items = {
  # item id => item amount,
  305 => 10,
  309 => 10,
  307 => 10,
  311 => 10,
  301 => 10,
  313 => 1000,
  314 => 1000,
  317 => 0,
  327 => 0,
  335 => 0,
  321 => 0,
  331 => 0,
  359 => 0,
  377 => 0,
  371 => 0,
  7944 => 0,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)