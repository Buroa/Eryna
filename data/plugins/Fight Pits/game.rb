# Contains the tzar fight pits minigame.
 
require 'java'
java_import 'org.apollo.game.event.impl.ConfigEvent'
 
class FightPitsMinigame < Minigame

  attr_reader :champion, :cooldown

  def initialize
    super("Fight pits", 2, 1)
    @champion = "Nobody"
    @cooldown = 0
  end

  def pulse
    if get_attribute PITS_START_GAME
      append_start_game
    end
    if get_attribute PITS_END_GAME
      append_end_game
    end
    if get_attribute PITS_CHAMPION_UPDATE
      append_campion_update
    end
    if get_attribute PITS_PLAYERS_COUNT_UPDATE
      append_players_count_update
    end
    if get_attribute PITS_TIME_UPDATE
      append_time_update
    end
  end

  def append_start_game
    @champion = "Nobody"
    set_attribute PITS_START_GAME, false
  end

  def append_end_game
    set_attribute PITS_END_GAME, false
  end

  def append_champion_update
    game_team = get_characters PITS_GAME_TEAM
    if game_team.size.equl? 1
      champion = game_team.get(0)
      champion.send ConfigEvent.new(560, 0)
      get_characters([PITS_GAME_TEAM, PITS_LOBBY_TEAM]).each do |player|
        if not player.equl? champion
          player.send SetInterfaceTextEvent.new(2805, "Current Champion: #{champion.name.capitalize}")
        end
      end
    end
    set_attribute PITS_CHAMPION_UPDATE, false
  end

  def append_players_count_update
    set_attribute PITS_PLAYERS_COUNT_UPDATE, false
  end

  def append_time_update
    set_attribute PITS_TIME_UPDATE, false
  end

end

game = FightPitsMinigame.new
game.add_listener FightPitsMinigameListener.new(game)
game.start

on :event, :object_action do |ctx, player, event|
  if event.option == 1
    if event.id == 9369 or event.id == 9368
      player.start_action FightPitsDistancedAction.new(player, game, event.position, event.id)
    end
  end
end

# uncomment the top to let this minigame run
# there is bugs atm steve: fix them