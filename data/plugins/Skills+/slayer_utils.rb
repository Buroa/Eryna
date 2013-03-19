require 'java'
java_import 'org.apollo.util.TextUtil'
java_import 'org.apollo.game.model.def.NpcDefinition'
java_import 'org.apollo.game.model.Skill'

def addPoints(source, npc)
  points = SLAYERPOINTS[source.name]
  task = SLAYERTASK[source.name]
  if task != nil
    if not task.completed
      source.skill_set.add_experience Skill::SLAYER, npc.health_max
      if points != nil
        SLAYERPOINTS[source.name] += (task.slaynpc.difficulty * 4)
      else
        SLAYERPOINTS[source.name] = (task.slaynpc.difficulty * 4)
      end
    end
  end
end

def getPoints(source)
  points = SLAYERPOINTS[source.name]
  if points != nil
    source.send_message "You currently have #{points} slayer points."
  end
end

def getKills(source)
  ourtask = SLAYERTASK[source.name]
  if ourtask != nil
    npc = ourtask.slaynpc.id
    definition = NpcDefinition.for_id npc
    
    source.send_message "You have killed #{ourtask.kills} out of #{ourtask.needed} #{definition.name}s."
  end
end

def getTask(source)
  ourtask = SLAYERTASK[source.name]
  if ourtask != nil
    npc = ourtask.slaynpc.id
    definition = NpcDefinition.for_id npc
    if not ourtask.completed
      source.send_message "You have been assigned to kill #{ourtask.needed} #{definition.name}s."
      source.send_message "You can find #{definition.name}s at the #{ourtask.slaynpc.location}."
    else
      source.send_message "You have completed your task of killing #{ourtask.needed} #{definition.name}."
      source.send_message "You can now generate a new task by executing the command[::slayer new]."
    end
  else
    source.send_message "You do not have a task!"
  end
end

def addTask(source, task)
  ourtask = SLAYERTASK[source.name]
  if ourtask != nil
    source.send_message "You already have a task pending!"
  else
    SLAYERTASK[source.name] = task
    getTask source
  end
end

def addKill(source)
  task = SLAYERTASK[source.name]
  if task != nil
    task.addKill
    if task.completed
      getTask source
      SLAYERTASK[source.name] = nil
    end
  end
end

def generateTask(source)
  task = nil
  level = source.skill_set.combat_level
  slayerlevel = source.skill_set.skill(Skill::SLAYER).maximum_level
  if level > 0 and level <= 45
    values = EASYSNPCS.values
    tasks = []
    values.each do |taskv|
      if slayerlevel >= taskv.req
        tasks = tasks.to_a.push taskv
      end
    end
    task = tasks[rand(tasks.length)]
  elsif level > 45 and level <= 90
    values = MEDIUMSNPCS.values
    tasks = []
    values.each do |taskv|
      if slayerlevel >= taskv.req
        tasks = tasks.to_a.push taskv
      end
    end
    task = tasks[rand(tasks.length)]
  elsif level > 90
    values = HARDSNPCS.values
    tasks = []
    values.each do |taskv|
      if slayerlevel >= taskv.req
        tasks = tasks.to_a.push taskv
      end
    end
    task = tasks[rand(tasks.length)]
  end
  if task != nil
    addTask source, SlayerTask.new(task, generateCount(task.difficulty))
  end
end

def generateCount(diff)
  case diff
    when 1
      return 25+TextUtil.random(75)
    when 2
      return 45+TextUtil.random(55)
    when 3
      return 55+TextUtil.random(65)
  end
end

# Contains important variables for slayer
class SlayerTask
  attr_reader :slaynpc, :needed, :kills

  def initialize(slaynpc, needed)
    @slaynpc = slaynpc
    @needed = needed
    @kills = 0
  end

  def addKill
    @kills += 1
  end

  def completed
    return @kills >= @needed
  end
end