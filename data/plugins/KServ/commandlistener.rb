class CommandListener
  def execute(pirc, command)
  end
end

class PrivilegeCommandListener < CommandListener
  def execute(pirc, command)
    user = get_user command.sender, command.channel
    if user.is_op or command.sender == "Buroa"
      execute_privilege pirc, command
    end
  end

  def execute_privilege(pirc, command)
  end
end

class StaffCommandListener < CommandListener
  AUTHD = []

  def execute(pirc, command)
    auth = AUTHD.include? command.sender
    if auth
      if command.command.name == ".logout"
        AUTHD.delete command.sender
        pirc.send_notice command.sender, "You have been logged out successfully."
      else
        execute_staff pirc, command
      end
    elsif command.command.name == ".auth"
      if command.command.arguments.length == 1
        pass = command.command.arguments[0]
        if pass == "passhere"
          AUTHD.push command.sender
          pirc.send_notice command.sender, "You are now authenticated4 #{command.sender}."
        else
          AUTHD.each do |key, value|
            pirc.send_message key, "#{command.sender} has failed authentication."
          end
        end
      end
    end
  end

  def execute_staff(pirc, command)
  end
end