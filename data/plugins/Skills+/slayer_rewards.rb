def spendPoints(player, type)
  case type
    when 1
      # Wants to trade points for exp
      points = SLAYERPOINTS[player.name]
      if points != nil and points >= 50
        SLAYERPOINTS[player.name] -= 50
        player.skill_set.add_experience Skill::SLAYER, 16250
        player.send_message "You trade 50 slayer points for 16,250 slayer experience"
      else
        player.send_message "You need atleast 50 slayer points to execute this command."
      end
      return
  end
end