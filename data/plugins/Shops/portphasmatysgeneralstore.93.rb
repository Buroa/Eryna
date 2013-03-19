# The Port_Phasmatys_General_Store

# Define our values
id = 93
name = "Port Phasmatys General Store"
items = {
  # item id => item amount,
  1931 => 300,
  1925 => 300,
  1735 => 10,
  1935 => 10,
  590 => 10,
  1735 => 10,
  2347 => 10,
}
type = STORE_MIXED

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)