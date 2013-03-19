# %usrrep% is the regex where the username goes into the url
# ex: http://www.google.com/%usrrep%
# user executed command has the name: Buroa
# would become: http://www.google.com/Buroa
# the id has to be in the data/www/v/script.apollo folder
#
# look at runelocus.apollo for how to structure another site

VOTE_SITES = {}

class VoteSite
  attr_reader :name, :url, :votes

  def initialize(name, url)
    @name = name
    @url = url
    @votes = {}
  end

  def add(name)
    @votes[name] = true
  end

  def remove(name)
    @votes.delete name
  end

  def has_voted(name)
    return @votes[name] != nil
  end
end

def append_vote_site(id, clazz)
  VOTE_SITES[id] = clazz
end

append_vote_site 1, VoteSite.new("Runelocus", "http://www.runelocus.com/toplist/index.php?action=vote&id=21223&id2=%usrrep%")
append_vote_site 2, VoteSite.new("Runeserver", "http://www.rune-server.org/toplist.php?do=vote&sid=6942&incentive=%usrrep%")
append_vote_site 3, VoteSite.new("Runetop", "http://runetoplist.com/?v=963&i=%usrrep%")