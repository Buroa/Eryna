POTIONS = {}
  
class Potclass
    attr_reader :id, :id2
 
    def initialize(id, id2)
      @id = id
      @id2 = id2
    end
  end
  
def append_potions(pots)
  POTIONS[pots.id] = pots
end
  
append_potions Potclass.new(2440, 157)      # Super-Strength (4)
append_potions Potclass.new(157, 159)     # Super-Strength (3)
append_potions Potclass.new(159, 161)      # Super-Strength (2)
append_potions Potclass.new(161, 229)     # Super-Strength (1)
  
append_potions Potclass.new(2436, 145)      # Super-Attack (4)
append_potions Potclass.new(145, 147)     # Super-Attack (3)
append_potions Potclass.new(147, 149)      # Super-Attack (2)
append_potions Potclass.new(149, 229)     # Super-Attack (1)
  
append_potions Potclass.new(2442, 163)      # Super-Defence (4)
append_potions Potclass.new(163, 165)     # Super-Defence (3)
append_potions Potclass.new(165, 167)      # Super-Defence (2)
append_potions Potclass.new(167, 229)     # Super-Defence (1)
  
append_potions Potclass.new(2434, 139)      # Prayer (4)
append_potions Potclass.new(139, 141)     # Prayer (3)
append_potions Potclass.new(141, 143)      # Prayer (2)
append_potions Potclass.new(143, 229)     # Prayer (1)

append_potions Potclass.new(3040, 3042)      # Magic (4)
append_potions Potclass.new(3042, 3044)     # Magic (3)
append_potions Potclass.new(3044, 3046)      # Magic (2)
append_potions Potclass.new(3046, 229)     # Magic (1)

append_potions Potclass.new(2444, 169)      # Ranging (4)
append_potions Potclass.new(169, 171)     # Ranging (3)
append_potions Potclass.new(173, 175)      # Ranging (2)
append_potions Potclass.new(175, 229)     # Ranging (1)

require 'java'
java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.event.impl.ItemActionEvent'
java_import 'org.apollo.game.model.Skill'

  class Potions < Action
    attr_reader :started
    def initialize(character, slot, item)
      super(0, true, character)
      @character = character
      @slot = slot
      @item = item
      @started = false
      item_def = ItemDefinition.for_id(@item.id).name.downcase
      character.send_message "You drink the #{item_def}."
      character.play_animation EAT_ANIMATION
    end
    
    def execute
      @started = true
      if character.inventory.get(@slot).id == @item.id
        character.inventory.removeSlot @slot, 1
        if @item.id == 2440 || @item.id == 157 || @item.id == 159 || @item.id == 161
          calculatestr
          character.inventory.add @item.id2, 1
        elsif @item.id == 2436 || @item.id == 145 || @item.id == 147 || @item.id == 149
          character.inventory.add @item.id2, 1
          calculateatt
        elsif @item.id == 2442 || @item.id == 163 || @item.id == 165 || @item.id == 167
          character.inventory.add @item.id2, 1
          calculatedef
        elsif @item.id == 3040 || @item.id == 3042 || @item.id == 3044 || @item.id == 3046
          character.inventory.add @item.id2, 1
          calculatemagic
        elsif @item.id == 2444 || @item.id == 169 || @item.id == 171 || @item.id == 173
          character.inventory.add @item.id2, 1
          calculaterange
        elsif @item.id == 2434 || @item.id == 139 || @item.id == 141 || @item.id == 143
          character.inventory.add @item.id2, 1
          calculateprayer
        end
      stop
      end
    end
  end
  
    def calculatestr
      skills = character.skill_set
      str_level = skills.get_skill(Skill::STRENGTH)
      if str_level.current_level > str_level.maximum_level
        reset = str_level.maximum_level - str_level.current_level
        skills.increase_skill(Skill::STRENGTH, reset)
        newstr = (str_level.maximum_level*15/100) + 5
        skills.increase_skill(Skill::STRENGTH, newstr)
      else
        newstr = (str_level.maximum_level*15/100) + 5
        skills.increase_skill(Skill::STRENGTH, newstr)
      end
    end
    def calculateatt
      skills = character.skill_set
      att_level = skills.get_skill(Skill::ATTACK)
      if att_level.current_level > att_level.maximum_level
        reset = att_level.maximum_level - att_level.current_level
        skills.increase_skill(Skill::ATTACK, reset)
        newatt = (att_level.maximum_level*15/100) + 5
        skills.increase_skill(Skill::ATTACK, newatt)
      else
        newatt = (att_level.maximum_level*15/100) + 5
        skills.increase_skill(Skill::ATTACK, newatt)
      end
    end
    def calculatedef
        skills = character.skill_set
        def_level = skills.get_skill(Skill::DEFENCE)
        if def_level.current_level > def_level.maximum_level
          reset = def_level.maximum_level - def_level.current_level
          skills.increase_skill(Skill::DEFENCE, reset)
          newdef = (def_level.maximum_level*15/100) + 5
          skills.increase_skill(Skill::DEFENCE, newdef)
        else
          newdef = (def_level.maximum_level*15/100) + 5
          skills.increase_skill(Skill::DEFENCE, newdef)
        end
    end
    def calculatemagic
        skills = character.skill_set
        magic_level = skills.get_skill(Skill::MAGIC)
        if magic_level.current_level > magic_level.maximum_level
          reset = magic_level.maximum_level - magic_level.current_level
          skills.increase_skill(Skill::MAGIC, reset)
          newmagic = 5
          skills.increase_skill(Skill::MAGIC, newmagic)
        else
          newmagic = 5
          skills.increase_skill(Skill::MAGIC, newmagic)
        end
    end
    def calculaterange
        skills = character.skill_set
        range_level = skills.get_skill(Skill::RANGED)
        if range_level.current_level > range_level.maximum_level
          reset = range_level.maximum_level - range_level.current_level
          skills.increase_skill(Skill::RANGED, reset)
          newrange = (range_level.maximum_level*10/100) + 4
          skills.increase_skill(Skill::RANGED, newrange)
        else
          newrange = (range_level.maximum_level*10/100) + 4
          skills.increase_skill(Skill::RANGED, newrange)
        end
    end
    def calculateprayer
        skills = character.skill_set
        prayer_level = skills.get_skill(Skill::PRAYER)
        newprayer = (prayer_level.maximum_level*25/100) + 7
        if ((prayer_level.current_level + newprayer) >= prayer_level.maximum_level)
          reset = prayer_level.maximum_level - prayer_level.current_level
          skills.increase_skill(Skill::PRAYER, reset)
        else
          skills.increase_skill(Skill::PRAYER, newprayer)
        end
    end
    
  on :event, :item_option do |ctx, player, event|
    if event.option == 1
      item = POTIONS[event.id]
        if item != nil
          player.startAction Potions.new(player, event.slot, item)
        end
    end    
  end