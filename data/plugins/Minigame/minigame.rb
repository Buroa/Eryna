require 'java'
java_import 'java.util.BitSet'

class Minigame
  attr_reader :listeners

  def initialize(name, teams, pulses)
    @name = name
    @pulses = pulses
    @started = false
    @minigametask = nil
    @minigamelistener = nil
    @listeners = []
    @characters = {}
    for i in 0..teams
      if i != teams
        @characters[i] = []
      end
    end
    @attributes = BitSet.new
    @firing_events = true
  end

  def set_firing_events(firing_events)
    @firing_events = firing_events
  end

  def get_attribute(key)
    return @attributes.get key
  end

  def set_attribute(key, value)
    @attributes.set key, value
  end

  def add_listener(minigame_listener)
    @listeners.push minigame_listener
  end

  def remove_listener(minigame_listener)
    @listeners.delete minigame_listener
  end

  def add(team, character)
    if character == nil
      return false
    end
    if not @characters[team].include? character
      @characters[team].push character
      if @firing_events
        @listeners.each do |listener|
          listener.character_added team, character
        end
      end
      return true
    end
    return false
  end

  def remove(team, character)
    if character == nil
      return false
    end
    if @characters[team].include? character
      @characters[team].delete character
      if @firing_events
        @listeners.each do |listener|
          listener.character_removed team, character
        end
      end
      return true
    end
    return false
  end

  def _remove(character)
    if character == nil
      return false
    end
    @characters.each do |id, team|
      if team.include? character
        @characters[id].delete character
        if @firing_events
          @listeners.each do |listener|
            listener.character_removed id, character
          end
        end
        return true
      end
    end
    return false
  end

  def get_characters(teams)
    charactersarr = []
    if teams.kind_of?(Array)
      teams.each do |team|
        @characters[team].each do |character|
          charactersarr.push character
        end
      end
    else
      @characters[teams].each do |character|
        charactersarr.push character
      end
    end
    return charactersarr
  end

  def get_team(character)
    @characters.each do |team, chars|
      if chars.include? character
        return team
      end
    end
    return -1
  end

  def pulse
    # if pulse is not overridden, stop this minigame
    stop
  end

  def start
    if not @started
      @started = true

      @minigamelistener = MinigamePlayerListener.new self
      World.world.register @minigamelistener
      schedule @pulses do |task|
        @minigametask = task
        pulse
      end
    end
  end

  def stop
    if @started
      @minigametask.stop
      World.world.unregister @minigamelistener
      @minigamelistener = nil
      @started = false
    end
  end
end