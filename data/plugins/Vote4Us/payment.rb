require 'java'
java_import 'org.apollo.game.model.inter.store.ShopPaymentType'

class VotePayment < ShopPaymentType
  def buyItem(player, item)
    points = player.voting_points
    value = buyValue item.item.id
    if points >= value
      player.set_voting_points points-value
      return true
    end
    return false
  end

  def buyValue(item)
    price = VOTE_ITEMS[item]
    if price != nil
      return price
    end
    return 0
  end

  def getName
    return "vote points"
  end

  def sellItem(player, item)
    return false
  end

  def sellValue(item)
    return 0
  end
end