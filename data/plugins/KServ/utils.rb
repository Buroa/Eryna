require 'java'
java_import 'org.apollo.security.PlayerCredentials'
java_import 'org.apollo.io.player.impl.DummyPlayerLoader'

  def skillNameToId(skill)
    if skill == "att" or skill == "attack"
      return 0
    elsif skill == "def" or skill == "defence"
      return 1
    elsif skill == "str" or skill == "strength"
      return 2
    elsif skill == "hp" or skill == "hitpoints"
      return 3
    elsif skill == "range" or skill == "ranged"
      return 4
    elsif skill == "pray" or skill == "prayer"
      return 5
    elsif skill == "mage" or skill == "magic"
      return 6
    elsif skill == "cook" or skill == "cooking"
      return 7
    elsif skill == "wc" or skill == "woodcut" or skill == "woodcutting"
      return 8
    elsif skill == "fletch" or skill == "fletching"
      return 9
    elsif skill == "fish" or skill == "fishing"
      return 10
    elsif skill == "firemaking"
      return 11
    elsif skill == "craft" or skill == "crafting"
      return 12
    elsif skill == "smith" or skill == "smithing"
      return 13
    elsif skill == "mine" or skill == "mining"
      return 14
    elsif skill == "herb" or skill == "herblore"
      return 15
    elsif skill == "agil" or skill == "agility" 
      return 16
    elsif skill == "thieve" or skill == "thieving"
      return 17
    elsif skill == "slay" or skill == "slayer"
      return 18
    elsif skill == "farm" or skill == "farming"
      return 19
    elsif skill == "rc" or skill == "rcing" or skill == "runecrafting"
      return 20
    end
    return -1
  end

  def loadPlayer(player)
    loader = DummyPlayerLoader.new
    credentials = PlayerCredentials.new player, "", 1, 1
    return loader.load_player credentials
  end

  def extract(command)
    builder = StringBuilder.new()
    command.arguments.each do |message|
      builder.append(message << " ")
    end
    return builder.to_string
  end

  def get_user(nickname, channel)
    user = nil
    users = $pirc.get_users channel
    users.each do |nick|
      if nick.get_nick.downcase == nickname.downcase
        user = nick
      end
    end
    return user
  end