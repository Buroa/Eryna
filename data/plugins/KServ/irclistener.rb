require 'java'
Dir["*.jar"].each { |jar| require jar }

java_import 'org.jibble.pircbot.IrcException'
java_import 'org.jibble.pircbot.PircBot'

IRC_WELCOME_MESSAGE = "Webclient: http://play.eryna.net | Website: http://www.eryna.net | Forums: http://www.3xgaming.com/forum/eryna/ | Game channel: #eryna"
IRC_INVITE_MESSAGE = "Thanks for the invite4 %sender%. I am4 Eryna, founded by4 http://www.buroa.me/. To view our latest updates, please type4 .news. Our Channel:4 #eryna. Our Website:4 http://www.eryna.net/."

class IrcListener < PircBot
  attr_reader :spy, :news

  def initialize
    @spy = {}
    @news = ""
  end

  def onMessage(channel, sender, login, hostname, message)
    cmd = ChatCommand.new sender, channel, message
    $dispatcher.dispatch cmd

    spyd = @spy[channel.downcase]
    if spyd != nil
      send_notice spyd, "[#{channel}] #{sender}: #{message}"
    end
  end

  def onPrivateMessage(sender, login, hostname, message)
    cmd = ChatCommand.new sender, sender, message
    $dispatcher.dispatch cmd
  end

  def onJoin(channel, sender, login, hostname)
    if channel == "#bots"
      return
    end

    if sender == "Buroa"
      send_message channel, "Welcome 4#{sender}! (web: 4http://www.buroa.me/ | git: 4http://www.github.com/buroa/)"
    end

    if channel == "#eryna"
      if sender != get_nick
        voice channel, sender
      end
    end

    send_notice sender, IRC_WELCOME_MESSAGE
  end

  def onPart(channel, sender, login, hostname)
    if @spy[channel.downcase] != nil
      if sender == get_nick
        @spy[channel.downcase] = nil
      end
    end
  end

  def onInvite(targetNick, sourceNick, sourceLogin, sourceHostname, channel)
    join_channel channel
    send_message channel, IRC_INVITE_MESSAGE.sub("%sender%", sourceNick)
  end

  def onTopic(channel, topic, setBy, date, changed)
    if channel == "#eryna"
      news = "#{Time.at(date/1000)} by #{setBy}: #{topic}"
    end
  end

  def onDisconnect
    _irc_new_instance
    _irc_register_commands
    _irc_load_settings
    schedule 5 do |task|
      _irc_connect
      task.stop
    end
  end

  def spy_on(chan, sender)
    @spy[chan.downcase] = sender
  end
end