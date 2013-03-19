require 'java'
java_import 'java.lang.StringBuilder'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.Player'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.security.PlayerCredentials'
java_import 'org.apollo.game.model.def.ItemDefinition'

class PlayersCommandListener < CommandListener
  def execute(pirc, command)
    players = World.world.player_repository.size
    pirc.send_notice command.sender, "*** [ 4PLAYERS ]: There is currently4 #{players} player" + ((players > 1 || players == 0) ? "s" : "") + " online."
  end
end

class TimeCommandListener < CommandListener
  def execute(pirc, command)
    pirc.send_notice command.sender, "*** [ 4TIME ]: The current time is4 #{Time.new}."
  end
end

class PartCommandListener < PrivilegeCommandListener
  def execute_privilege(pirc, command)
    if command.channel != "#eryna" or (command.channel == "#eryna" and command.sender == "Buroa")
      pirc.part_channel command.channel, ".part used by #{command.sender}"
    end
  end
end

class StatsCommandListener < CommandListener
  def execute(pirc, command)
    npcs = World.world.npc_repository.size
    grounds = World.world.items.size
    chunks = World.world.region_manager.chunk_size
    regions = World.world.region_manager.region_size
    pirc.send_notice command.sender, "*** [ 4STATS ]: npcs:4 #{npcs} | objects:4 #{objects} | chunks:4 #{chunks} | regions:4 #{regions}"
  end
end

class UptimeCommandListener < CommandListener
  def execute(pirc, command)
    uptime = World.world.uptime
    usertime = (uptime*15)/60
    pirc.send_notice command.sender, "*** [ 4UPTIME ]: I have been running for4 #{usertime} minute" + ((usertime > 1 || usertime == 0) ? "s" : "") + "."
  end
end

class CombatCommandListener < CommandListener
  def execute(pirc, command)
    if command.command.arguments.length == 1
      name = command.command.arguments[0]
      response = loadPlayer name
      if response.status == 2
        player = response.player
        skills = player.skill_set
        pirc.send_notice command.sender, "*** [ 4COMBAT ]: 4#{name} is level4 #{skills.combat_level} [4Melee] ASDHRPM:4 #{skills.skill(0).maximum_level} #{skills.skill(2).maximum_level} #{skills.skill(1).maximum_level} #{skills.skill(3).maximum_level} #{skills.skill(4).maximum_level} #{skills.skill(5).maximum_level} #{skills.skill(6).maximum_level}"
      elsif response.status == 13
        pirc.send_notice command.sender, "*** [ 4COMBAT ]: 4#{name} is not a valid username."
      end
    else
      pirc.send_notice command.sender, "Syntax: .cmb [user]"
    end
  end
end

class SkillCommandListener < CommandListener
  def execute(pirc, command)
    if command.command.arguments.length == 2
      skill = command.command.arguments[0]
      name = command.command.arguments[1]
      response = loadPlayer name
      if response.status == 2
        player = response.player
        skills = player.skill_set
        sid = skillNameToId skill
        if sid != -1
          pirc.send_notice command.sender, "*** [ 4#{skill.upcase} ]: lvl:4 #{skills.skill(sid).maximum_level} | exp:4 #{skills.skill(sid).experience} (#{(skills.skill(sid).experience/13034431)*100} of 99)"
        end
      elsif response.status == 13
        pirc.send_notice command.sender, "*** [ 4SKILL ]:4 #{name} is not a valid username."
      end
    else
      pirc.send_notice command.sender, "Syntax: .skill [skill] [user]"
    end
  end
end

class YellCommandListener < CommandListener
  def execute(pirc, command)
    if command.command.arguments.length > 0
      user = get_user command.sender, command.channel
      message = extract command.command
      World.world.player_repository.each do |players|
        players.send_message "[#{command.channel}] #{user}: #{message}"
      end
    else
      pirc.send_notice command.sender, "Syntax: .yell [message]"
    end
  end
end

class PriceCommandListener < CommandListener
  def execute(pirc, command)
    args = command.command.arguments
    if args.length == 1
      item = args[0].to_i
      begin
        defi = ItemDefinition.for_id item
        name = defi.name.downcase
        price = defi.value
        pirc.send_notice command.sender, "*** [ 4PRICE ]:4 #{name.capitalize} currently costs4 #{price} coins."
      rescue Exception => e
        pirc.send_notice command.sender, "*** [ 4PRICE ]:4 #{item} is not a valid item id."
      end
    else
      pirc.send_notice command.sender, "Syntax: .price [item]"
    end
  end
end

class HelpCommandListener < CommandListener
  def execute(pirc, command)
    commands = ''
    $dispatcher.commands.each do |key, value|
      commands << "4#{key}, "
    end
    pirc.send_notice command.sender, "*** [ 4HELP ]: The following commands are available: #{commands}"
  end
end

class NewsCommandListener < CommandListener
  def execute(pirc, command)
    pirc.send_notice command.sender, "*** [ 4NEWS ]: #{pirc.news}"
  end
end

class VoiceCommandListener < PrivilegeCommandListener
  def execute_privilege(pirc, command)
    if command.command.arguments.length == 1
      name = command.command.arguments[0]
      pirc.voice command.channel, name
    else
      pirc.send_notice command.sender, "Syntax: .voice [user]"
    end
  end
end

class HalfOpCommandListener < PrivilegeCommandListener
  def execute_privilege(pirc, command)
    if command.command.arguments.length == 1
      name = command.command.arguments[0]
      pirc.set_mode command.channel, "+h #{name}"
    else
      pirc.send_notice command.sender, "Syntax: .hop [user]"
    end
  end
end

class OpCommandListener < PrivilegeCommandListener
  def execute_privilege(pirc, command)
    if command.command.arguments.length == 1
      name = command.command.arguments[0]
      pirc.op command.channel, name
    else
      pirc.send_notice command.sender, "Syntax: .op [user]"
    end
  end
end

class KickCommandListener < PrivilegeCommandListener
  def execute_privilege(pirc, command)
    args = command.command.arguments
    if args.length >= 1
      name = args[0]
      reason = args.length == 2 ? args[1] : ""
      pirc.kick command.channel, name, reason
    else
      pirc.send_notice command.sender, "Syntax: .kick [user] [reason=none]"
    end
  end
end

class TopicCommandListener < PrivilegeCommandListener
  def execute_privilege(pirc, command)
    if command.command.arguments.length > 0
      message = extract command.command
      pirc.set_topic command.channel, message
    else
      pirc.send_notice command.sender, "Syntax: .topic [topic]"
    end
  end
end

class InviteCommandListener < PrivilegeCommandListener
  def execute_privilege(pirc, command)
    if command.command.arguments.length == 1
      name = command.command.arguments[0]
      pirc.send_invite name, command.channel
    else
      pirc.send_notice command.sender, "Syntax: .invite [user]"
    end
  end
end

class CmdCommandListener < StaffCommandListener
  def execute_staff(pirc, command)
    if command.command.arguments.length > 0
      gamecmd = extract command.command
      cmd = DefaultUtil.create_command gamecmd
      DefaultUtil.execute_command cmd
    else
      pirc.send_notice command.sender, "Syntax: .cmd [command]"
    end
  end
end

class MassMessageCommandListener < StaffCommandListener
  def execute_staff(pirc, command)
    if command.command.arguments.length > 0
      message = extract command.command
      pirc.channels.each do |channel|
        pirc.send_message channel, "*** [ 4AMSG ]: #{message}"
      end
    else
      pirc.send_notice command.sender, "Syntax: .mass [message]"
    end
  end
end

class RawCommandListener < StaffCommandListener
  def execute_staff(pirc, command)
    if command.command.arguments.length > 0
      cmd = extract command.command
      pirc.send_raw_line cmd
    else
      pirc.send_notice command.sender, "Syntax: .raw [command]"
    end
  end
end

class DebugCommandListener < StaffCommandListener
  def execute_staff(pirc, command)
    args = command.command.arguments
    if args.length == 1
      debug = args[0]
      if debug == "on"
        $debug = true
      elsif debug == "off"
        $debug = false
      end
    else
      pirc.send_notice command.sender, "Syntax: .debug [on/off]"
    end
  end
end

class SpyCommandListener < StaffCommandListener
  def execute_staff(pirc, command)
    args = command.command.arguments
    if args.length == 1
      chan = args[0]
      pirc.join_channel chan
      pirc.spy_on chan, command.sender
    else
      pirc.send_notice command.sender, "Syntax: .spy [chan]"
    end
  end
end

class EvalCommandListener < StaffCommandListener
  def execute_staff(pirc, command)
    if command.command.arguments.length > 0
      cmd = extract command.command
      result = eval cmd
      pirc.send_message command.channel, "#{result}"
    else
      pirc.send_notice command.sender, "Syntax: .eval [code]"
    end
  end
end

class ReverseDnsCommandListener < StaffCommandListener
  def execute(pirc, command)
    if command.command.arguments.length == 1
      host = command.command.arguments[0]
      is_ip = /(?:[0-9]{1,3}\.){3}[0-9]{1,3}/.match(host) != nil
      if is_ip
        host_result = `host #{host}`.split.join(" ")
        rdns = /pointer (\S+)/.match(host_result).captures[0];
        pirc.send_notice command.sender, "*** [ 4RDNS ]:4 #{host} has the reverse4 #{rdns}"
      else
        host_result = `host #{host}`.split.join(" ")
        dns = /address (\S+)/.match(host_result).captures[0]
        pirc.send_notice command.sender, "*** [ 4RDNS ]:4 #{host} has the address4 #{dns}."
      end
    else
      pirc.send_notice command.sender, "Syntax: .rdns [address]"
    end
  end
end

class PingCommandListener < StaffCommandListener
  def execute(pirc, command)
    if command.command.arguments.length == 1 or command.command.arguments.length == 2
      v6 = command.command.arguments.length == 2 ? (command.command.arguments[1] == "-6" ? "6" : "") : ""
      ping_result = `ping#{v6} #{command.command.arguments[0]} -c 1`.split
      if ping_result.length > 0
        ping_result = ping_result.join(" ")
        from = /from(\s\S+)/.match(ping_result).captures[0]
        latency = /time=(\S+\s\S+)/.match(ping_result).captures[0]
        pirc.send_notice command.sender, "*** [ 4PING ]: traveled to04#{from} in4 #{latency}."
      end
    else
      pirc.send_notice command.sender, "Syntax: .ping [address] [-6=false]"
    end
  end
end

class PluginCommandListener < CommandListener
  def execute(pirc, command)
    plugins = `ls data/plugins/`.split.join(" ").gsub("bootstrap.rb", "").split(" ")
    plugins_ = ''
    plugins.each do |_plugin|
      plugins_ << "4#{_plugin}, "
    end
    pirc.send_notice command.sender, "*** [ 4PLUGINS ]: currently enabled: #{plugins_}"
  end
end

class AuthCommandListener < StaffCommandListener
end