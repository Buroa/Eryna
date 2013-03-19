require 'java'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Inventory'

NPC_ATTACK_ANIMATIONS = {}
NPC_BLOCK_ANIMATIONS = {}
NPC_DEATH_ANIMATIONS = {}
NPC_DROPS = {}

def append_attack_animation(id, animation)
  NPC_ATTACK_ANIMATIONS[id] = Animation.new(animation)
end

def append_block_animation(id, animation)
  NPC_BLOCK_ANIMATIONS[id] = Animation.new(animation)
end

def append_death_animation(id, animation)
  NPC_DEATH_ANIMATIONS[id] = Animation.new(animation)
end

def append_drops(id, drops)
  inventory = Inventory.new(drops.size)
  drops.each do |item, amount|
    inventory.add item, amount
  end
  NPC_DROPS[id] = inventory
end

# rock crab
append_attack_animation 1265, 1312
append_block_animation 1265, 1311
append_death_animation 1265, 1314

#crawling hand
append_attack_animation 1648, 1592
append_block_animation 1648, 1591
append_death_animation 1648, 1590