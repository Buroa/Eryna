# The Harry's Fishing Shop

# Define our values
id = 32
name = "Harry's Fishing Shop"
items = {
  # item id => item amount,
  303 => 50,
  307 => 50,
  311 => 50,
  301 => 50,
  313 => 1000,
  317 => 500,
  327 => 500,
  345 => 500,
  321 => 500,
  335 => 200,
  349 => 200,
  331 => 200,
  359 => 100,
  377 => 100,
  371 => 100,
  7942 => 0,
  383 => 0,
  389 => 0,
  339 => 200,
  355 => 200,
  333 => 200,
  329 => 100,
  365 => 100,
  361 => 100,
  379 => 50,
  373 => 50,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)