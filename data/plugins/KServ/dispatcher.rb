require 'java'
java_import 'org.apollo.game.command.Command'

class ChatDispatcher
  attr_reader :bot, :commands

  def initialize(bot)
    @bot = bot
    @commands = {}
  end

  def register(command, clazz)
    commands[command] = clazz
  end

  def dispatch(chat)
    clazz = commands[chat.command.name]
    if clazz != nil and bot != nil
      clazz.execute bot, chat
    end
  end
end