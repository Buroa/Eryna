## This script deals with all the home functions of the old
## corruption rsps emulator

require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.Npc'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.Position'

class HomeObjectDialogueListener
  java_implements 'org.apollo.game.model.inter.dialog.DialogueListener'
  attr_reader :id

  def initialize(id)
    @id = id
  end

  def buttonClicked(player, button)
    if id == 1
      if button == 2482
        player.teleport Position.new(3512, 3480, 0), true
      elsif button == 2483
        player.teleport Position.new(2852, 2954, 0), true
      elsif button == 2484
        player.teleport Position.new(2900, 2901, 0), true
      elsif button == 2485
        player.teleport Position.new(2870, 2969, 0), true
      end
    elsif id == 2 # Second portal, main portal
      if button == 2482
        player.teleport Position.new(2670, 3709, 0), true
      elsif button == 2483
        player.teleport Position.new(2540, 4716, 0), true
      elsif button == 2484
        player.teleport Position.new(3349, 3672, 0), true
      elsif button == 2485
        player.teleport Position.new(3436, 3572, 0), true
      end
    end
    player.interface_set.close
  end

  def interfaceClosed(player, manually)
  end

  def continued(player)
  end
end

class HomeObjectAction < DistancedAction
  attr_reader :position, :id

  def initialize(character, position, id)
    super 0, true, character, position, 1
    @position = position
    @id = id
  end

  def executeAction
    character.turn_to position
    if id == 4156
      if position.x == 3091
        if position.y == 3493
          if character.settings.members
            character.interface_set.send_option HomeObjectDialogueListener.new(1), "Member Train", "Member Hangout", "Member Broodoo", "Member Mall"
          else
            character.send_message "You need to be a member to use this portal."
          end
        elsif position.y == 3497
          character.interface_set.send_option HomeObjectDialogueListener.new(2), "Rock Crabs", "Mage Bank", "Green Dragons", "Slayer Tower"
        end
      end
    end
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end
end

class HomeNpcDialogueListener
  java_implements 'org.apollo.game.model.inter.dialog.DialogueListener'
  attr_reader :npc

  def initialize(npc)
    @npc = npc
  end

  def buttonClicked(player, button)
    if npc.id == 2024
      if button == 2461
        player.teleport Position.new(3565, 3313, 0), true
      end
    elsif npc.id == 2624
      if button == 2471
        player.teleport Position.new(2438, 5169, 0), true
      elsif button == 2472
        player.teleport Position.new(2399, 5177, 0), true
      end
    end
    player.interface_set.close
  end

  def interfaceClosed(player, manually)
  end

  def continued(player)
    if npc.id == 2024
      listener = HomeNpcDialogueListener.new npc
      player.interface_set.send_option listener, "Yes", "No"
    end
  end
end

class HomeNpcOption < DistancedAction
  attr_reader :position, :npc

  def initialize(character, position, npc)
    super 0, true, character, position, 1
    @position = position
    @npc = npc
  end

  def executeAction
    npc.turn_to character.position
    character.turn_to position
    listener = HomeNpcDialogueListener.new npc
    if npc.id == 2624
      character.interface_set.send_option listener, "I want to visit the Tzhaar", "I want to visit the Fight Pits Minigame", "Nothing Thanks."
    elsif npc.id == 2024
      character.interface_set.send_statement listener, "Would you like to go to Barrows mate?"
    elsif npc.id == 162
      ## GNOME
      ## character.interface_set.send_option listener, "...", "..."
    elsif npc.id == 801
      character.interface_set.send_option listener, "Dice Game", "Blackjack"
    end
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end
end

on :event, :object_action do |ctx, player, event|
  if event.option == 1
    position = event.position
    if position.x == 3091 and (position.y == 3493 or position.y == 3497)
      player.start_action HomeObjectAction.new player, position, event.id
    end
  end
end

on :event, :npc_option do |ctx, player, event|
  if event.option == 2
    position = event.npc.position
    if (position.x == 3090 or position.x == 3091) and (position.y == 3494 or position.y == 3495 or position.y == 3496 or position.y == 3488)
      player.start_action HomeNpcOption.new player, position, event.npc
    end
  end
end

CRSPS_NPCS = {
  Npc.new(2624, Position.new(3090, 3494)) => 4,
  Npc.new(2024, Position.new(3090, 3495)) => 4,
  Npc.new(162 , Position.new(3090, 3496)) => 4,
}

CRSPS_NPCS.each do |npc, face|
  npc.set_face face
  World.world.register npc
end