MUSIC_STATIONS = {}

on :command, :music do |player, command|
  args = command.arguments
  if args.length > 0
    station = MUSIC_STATIONS[args[0]]
    if station != nil
      player.send_message "#{station}:music:"
    end
  else
    player.send_message "Syntax: ::music [station]"
  end
end

def append_music_station(id, url)
  MUSIC_STATIONS[id] = url
end

append_music_station "977music", "http://216.155.151.119/977_HITS_SC"
append_music_station "stop", ""