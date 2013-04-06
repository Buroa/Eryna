# Contains the tzar fight pits minigame.
 
require 'java'
java_import 'org.apollo.game.event.impl.ConfigEvent'
 
class FightPitsMinigame < Minigame

  attr_reader :opponents, :champion, :cooldown

  def initialize
    super("Fight pits", 2, 1)
    @champion = "Nobody"
    @cooldown = 0
    @opponents = 0
  end

  def before_pulse
    if cooldown != 0 and cooldown % 60 == 0
      set_attribute PITS_TIME_UPDATE, true
    end
  end

  def pulse
    before_pulse

    if get_attribute PITS_START_GAME
      append_start_game
    end
    if get_attribute PITS_END_GAME
      append_end_game
    end
    if get_attribute PITS_CHAMPION_UPDATE
      append_champion_update
    end
    if get_attribute PITS_PLAYERS_COUNT_UPDATE
      append_players_count_update
    end
    if get_attribute PITS_TIME_UPDATE
      append_time_update
    end

    after_pulse
  end

  def after_pulse
    if cooldown > 0
      @cooldown -= 1
      if cooldown == 0
        size = minigame.get_characters(PITS_LOBBY_TEAM).size
        if size > PITS_MIN_NEEDED
          minigame.set_attribute PITS_START_GAME, true
        end
      end
    end
  end

  def append_start_game
    @cooldown = 0
    @champion = "Nobody"
    set_attribute PITS_CHAMPION_UPDATE, true
    get_characters(PITS_LOBBY_TEAM).each do |character|
      remove PITS_LOBBY_TEAM, character
      add PITS_GAME_TEAM, character
      character.settings.set_attackable true
      character.teleport PITS_GAME_ENTER_POSITION

      # add a listener for deaths
      get_combat_set(character).add_listener FightPitsCombatListener.new(self)
      @opponents += 1
    end
    set_attribute PITS_PLAYERS_COUNT_UPDATE, true
    set_attribute PITS_START_GAME, false
  end

  def append_end_game
    @cooldown = 180
    winner = get_characters(PITS_GAME_TEAM)[0]
    @champion = winner.name
    winner.send ConfigEvent.new(560, 0)
    remove PITS_GAME_TEAM, winner
    add PITS_LOBBY_TEAM, winner
    winner.teleport PITS_GAME_LEAVE_POSITION
    winner.inventory.add 6529, opponents
    @opponents = 0
    set_attribute PITS_CHAMPION_UPDATE, true
    set_attribute PITS_END_GAME, false
  end

  def append_champion_update
    game_team = get_characters PITS_GAME_TEAM
    get_characters([PITS_GAME_TEAM, PITS_LOBBY_TEAM]).each do |character|
      if not champion == character.name
        character.send ConfigEvent.new(560, REMOVE_FOES_REMAINING)
      end
      character.send SetInterfaceTextEvent.new(2805, "Current Champion: #{@champion}")
    end
    set_attribute PITS_CHAMPION_UPDATE, false
  end

  def append_players_count_update
    size = get_characters(PITS_GAME_TEAM).size-1
    if size > 0
      get_characters([PITS_GAME_TEAM, PITS_LOBBY_TEAM]).each do |character|
        character.send ConfigEvent.new(560, size)
      end
    end
    if size == 0
      set_attribute PITS_END_GAME, true
    end
    set_attribute PITS_PLAYERS_COUNT_UPDATE, false
  end

  def append_time_update
    time = cooldown / 60
    if time != 0
      get_characters(PITS_LOBBY_TEAM).each do |character|
        character.send_message "Next round starts in #{time} minute#{time == 1 ? "" : "s"}!"
      end
    end
    set_attribute PITS_TIME_UPDATE, false
  end

end

game = FightPitsMinigame.new
game.add_listener FightPitsMinigameListener.new(game)
game.start

on :event, :object_action do |ctx, player, event|
  if event.option == 1
    if event.id == ENTER_GAME_LOBBY or event.id == LEAVE_GAME_START or event.id == VIEWING_ORB
      player.start_action FightPitsDistancedAction.new(player, game, event.position, event.id)
    end
  end
end

# uncomment the top to let this minigame run
# there is bugs atm steve: fix them