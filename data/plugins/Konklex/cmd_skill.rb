require 'java'
java_import 'org.apollo.game.model.SkillSet'

on :command, :max, RIGHTS_DEV do |player, command|
  skills = player.skill_set
  (0...skills.size).each do |skill|
    skills.add_experience(skill, SkillSet::MAXIMUM_EXP)
  end
end

on :command, :skill, RIGHTS_DEV do |player, command|
  skills = player.skill_set
  if args.length == 2
    skill = args[0].to_i
    exp = args[0].to_i
    skills.set_skill(skill, exp)
  else
    player.send_message "Syntax: ::skill [id] [exp]"
  end
end
