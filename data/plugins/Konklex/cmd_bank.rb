require 'java'
java_import 'org.apollo.game.model.inter.bank.BankUtils'

on :command, :bank, RIGHTS_DEV do |player, command|
  BankUtils.open_bank player
end
