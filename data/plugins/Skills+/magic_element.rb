require 'java'
java_import 'org.apollo.game.model.EquipmentConstants'

AIR_ELEMENTS = {}
WATER_ELEMENTS = {}
EARTH_ELEMENTS = {}
FIRE_ELEMENTS = {}

AIR_RUNE	= 556
WATER_RUNE	= 555
EARTH_RUNE	= 557
FIRE_RUNE	= 554

MIND_RUNE	= 558
CHAOS_RUNE	= 562
DEATH_RUNE	= 560
BLOOD_RUNE	= 565

COSMIC_RUNE	= 564
LAW_RUNE	= 563
NATURE_RUNE	= 561
SOUL_RUNE	= 566

MIST_RUNE	= 4695
DUST_RUNE	= 4696
SMOKE_RUNE	= 4697
MUD_RUNE	= 4698
STEAM_RUNE	= 4694
LAVA_RUNE	= 4699

class Elemen
  attr_reader :runes, :staffs, :name
  
  def initialize(runes, staffs, name="Null")
    @runes = runes
	@staffs = staffs
	@name = name
	@name.freeze
  end
  
  def check_remove(player, amount, remove)
    if @staffs != nil
      staff = player.equipment.get EquipmentConstants::WEAPON
	  if staff != nil
	    id = staff.id
	    @staffs.each do |s|
	      if id == s
		    return true
		  end
	    end
	  end
	end
	
	inventory = player.inventory

	found = {}
	counter = 0
	
	inventory.items.each do |item|
	  break unless counter < amount
	  
	  if item == nil
	    next
	  end
	  
	  amt = item.amount
	  @runes.each do |rune|
	    break unless counter < amount
		
		id = item.id
		if id == rune
		  if amt >= amount
		    if remove
			  inventory.remove id, amount
			end
			return true
		  else
			found[id] = amt
			counter += amt
		  end
		end
	  end
	end
	
	if counter >= amount
	  if remove
	    found.each do |id, amt|
		  inventory.remove id, amt
		end
	  end
	  return true
	end
	
	return false
  end
end

AIR_RUNES	= [ 556, 4695, 4696, 4697 ]
WATER_RUNES	= [ 555, 4695, 4698, 4694 ]
EARTH_RUNES	= [ 557, 4696, 4697, 4698 ]
FIRE_RUNES	= [ 554, 4697, 4694, 4699 ]

AIR_STAFFS	 = [ 1381, 1397, 1405 ]
WATER_STAFFS = [ 1383, 1395, 1403 ]
EARTH_STAFFS = [ 1385, 1399, 1407, 3053, 3054 ]
FIRE_STAFFS	 = [ 1387, 1393, 1401, 3053, 3054 ]

AIR	= Elemen.new(AIR_RUNES, AIR_STAFFS, "Air rune")
WATER	= Elemen.new(WATER_RUNES, WATER_STAFFS, "Water rune")
EARTH	= Elemen.new(EARTH_RUNES, EARTH_STAFFS, "Earth rune")
FIRE	= Elemen.new(FIRE_RUNES, FIRE_STAFFS, "Fire rune")

MIND	= Elemen.new([MIND_RUNE], nil, "Mind rune")
CHAOS	= Elemen.new([CHAOS_RUNE], nil, "Chaos rune")
DEATH	= Elemen.new([DEATH_RUNE], nil, "Death rune")
BLOOD	= Elemen.new([BLOOD_RUNE], nil, "Blood rune")

COSMIC	= Elemen.new([COSMIC_RUNE], nil, "Cosmic rune")
LAW	= Elemen.new([LAW_RUNE], nil, "Law rune")
NATURE	= Elemen.new([NATURE_RUNE], nil, "Nature rune")
SOUL	= Elemen.new([SOUL_RUNE], nil, "Soul rune")
