class AgilityCourse

  attr_reader :max, :bonus, :click

  def initialize(max, bonus)
    @max = max+1
    @bonus = bonus
    @click = {}
  end

  def raise(player, cli)
    clicks = get_clicks player
    if @max != clicks
      if (cli - 1) == clicks
        @click[player] += 1
      end
    end
  end

  def complete(player)
    clicks = get_clicks player
    if @max == clicks
      @click[player] = 0
      return true
    end
    return false
  end

  def get_clicks(player)
    clicks = @click[player]
    if clicks != nil
      return clicks
    else
      @click[player] = 0
      return 0
    end
  end
end