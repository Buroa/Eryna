require 'java'
java_import 'org.apollo.io.player.PlayerListener'

class MinigamePlayerListener < PlayerListener

  def initialize(minigame)
    super()
    @minigame = minigame
  end

  def login(player)
  end

  def logout(player)
    @minigame.listeners.each do |listener|
      listener.character_disconnected player
    end
  end
end