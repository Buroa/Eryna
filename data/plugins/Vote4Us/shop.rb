# Define our values
id = 201
name = "Voting Rewards"
items = {}

# construct the store
VOTE_ITEMS.each do |item, price|
  items[item] = 1
end

# set the type
type = STORE_REDUNDANT

# Ship out the shop to the world.
shop = appendShop Shops.new(id, name, items, type)
shop.set_payment VotePayment.new