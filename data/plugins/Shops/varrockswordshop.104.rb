# The Varrock_Sword_Shop

# Define our values
id = 104
name = "Varrock Sword Shop"
items = {
  # item id => item amount,
  1277 => 10,
  1279 => 10,
  1281 => 10,
  1283 => 10,
  1285 => 10,
  1287 => 10,
  1291 => 10,
  1293 => 10,
  1295 => 10,
  1297 => 10,
  1299 => 10,
  1301 => 10,
  1205 => 10,
  1203 => 10,
  1207 => 10,
  1209 => 10,
  1211 => 10,
}
type = STORE_NORMAL

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)