require 'java'
java_import 'org.apollo.game.event.impl.SetInterfaceTextEvent'

QUESTS = [7332, 7333, 7334, 7336, 7383, 7339, 7338, 7340, 7346, 7341, 7342, 7337, 7343, 7335, 7344, 7345, 7347, 7348]
QUEST_TEXTS = {}

# This will remove the top questbook
on :login do |player|
  QUESTS.each do |id|
    quest_text = QUEST_TEXTS[id]
    if quest_test != nil
      player.send SetInterfaceTextEvent.new(id, quest_text)
    end
  end
end

def append_quest_text(id, text)
  QUEST_TEXTS[id] = text
end

append_quest_text 7332, "hi"