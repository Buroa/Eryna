package org.apollo.game.model;

import org.apollo.game.model.inter.store.Shop;
import org.apollo.game.model.inter.trade.TradeSession;
import org.apollo.game.model.skill.farming.FarmingSet;

/**
 * Gives the player settings.
 * @author Steve
 */
public final class PlayerSettings {

	/**
	 * The farming set.
	 */
	private FarmingSet farmingSet;

	/**
	 * The player's trade request.
	 */
	private Request request;

	/**
	 * The player's trade session.
	 */
	private TradeSession tradeSession;

	/**
	 * A flag indicating if the player is withdrawing items as notes.
	 */
	private boolean withdrawingNotes = false; // TODO find a better place!

	/**
	 * The public chat setting.
	 */
	private int publicChat = 0;

	/**
	 * The private chat value.
	 */
	private int privateChat = 0;

	/**
	 * The trade chat value.
	 */
	private int trade = 0;

	/**
	 * The shop that is currently open.
	 */
	private Shop shop;

	/**
	 * The dialogue id that is currently open.
	 */
	private int dialogueId;

	/**
	 * The hidden flag.
	 */
	private boolean hide;

	/**
	 * The current song.
	 */
	private int currentSong;

	/**
	 * The voting points.
	 */
	private short votingPoints = 0;

	/**
	 * The banned flag.
	 */
	private boolean banned;

	/**
	 * The muted flag.
	 */
	private boolean mute;

	/**
	 * The attackable flag.
	 */
	private boolean attackable = false;

	/**
	 * The membership flag.
	 */
	private boolean members = false;

	/**
	 * A flag indicating if the player has designed their character.
	 */
	private boolean designedCharacter = false;

	/**
	 * Gets the currently opened dialogue id.
	 * @return The currently opened dialogue id
	 */
	public int getDialogueId() {
		return dialogueId;
	}

	/**
	 * Gets the farming set.
	 * @return The farming set.
	 */
	public FarmingSet getFarmingSet() {
		return farmingSet;
	}

	/**
	 * Gets the private chat value.
	 * @return The private chat value.
	 */
	public int getPrivateChat() {
		return privateChat;
	}

	/**
	 * Gets the public chat value.
	 * @return The public chat value.
	 */
	public int getPublicChat() {
		return publicChat;
	}

	/**
	 * Gets the player's trade request.
	 * @return The player's trade request.
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * Gets the shop.
	 * @return the shop.
	 */
	public Shop getShop() {
		return shop;
	}

	/**
	 * Gets the trade value.
	 * @return The trade value.
	 */
	public int getTrade() {
		return trade;
	}

	/**
	 * Gets the current trade session.
	 * @return The trade session.
	 */
	public TradeSession getTradeSession() {
		return tradeSession;
	}

	/**
	 * Gets the voting points.
	 * @return The voting points.
	 */
	public short getVotingPoints() {
		return votingPoints;
	}

	/**
	 * Checks if the player has designed their character.
	 * @return A flag indicating if the player has designed their character.
	 */
	public boolean hasDesignedCharacter() {
		return designedCharacter;
	}

	/**
	 * Gets the player's attackable flag.
	 * @return True if the player is attackable, false if otherwise.
	 */
	public boolean isAttackable() {
		return attackable;
	}

	/**
	 * Gets the banned flag.
	 * @return True if banned, false if otherwise.
	 */
	public boolean isBanned() {
		return banned;
	}

	/**
	 * Gets the farming flag.
	 * @return True if farming, false if otherwise.
	 */
	public boolean isFarming() {
		return farmingSet != null;
	}

	/**
	 * Gets the hidden flag.
	 * @return True if hidden, false if otherwise.
	 */
	public boolean isHidden() {
		return hide;
	}

	/**
	 * Checks if this player account has membership.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isMembers() {
		return members;
	}

	/**
	 * Gets the muted flag.
	 * @return True if muted, false if otherwise.
	 */
	public boolean isMuted() {
		return mute;
	}

	/**
	 * Gets the withdrawing notes flag.
	 * @return The flag.
	 */
	public boolean isWithdrawingNotes() {
		return withdrawingNotes;
	}

	/**
	 * Plays a song.
	 * @param song The song.
	 */
	public void playSong(int song) {
		if (song != currentSong)
			currentSong = song;
	}

	/**
	 * Sets the player's attackable flag.
	 * @param attackable True if attackable, false if otherwise.
	 */
	public void setAttackable(boolean attackable) {
		this.attackable = attackable;
	}

	/**
	 * Sets the banned flag.
	 * @param banned True if banned, false if otherwise.
	 */
	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	/**
	 * Sets the character design flag.
	 * @param designedCharacter A flag indicating if the character has been
	 * designed.
	 */
	public void setDesignedCharacter(boolean designedCharacter) {
		this.designedCharacter = designedCharacter;
	}

	/**
	 * Sets the dialogue id.
	 * @param dialogueId The dialogue id.
	 */
	public void setDialogueId(int dialogueId) {
		this.dialogueId = dialogueId;
	}

	/**
	 * Sets the farming set.
	 * @param farmingSet The farming set.
	 */
	public void setFarmingSet(FarmingSet farmingSet) {
		this.farmingSet = farmingSet;
	}

	/**
	 * Sets the hide boolean flag.
	 * @param hide The hide flag.
	 */
	public void setHide(boolean hide) {
		this.hide = hide;
	}

	/**
	 * Sets the membership flag.
	 * @param members The membership flag.
	 */
	public void setMembers(boolean members) {
		this.members = members;
	}

	/**
	 * Sets the muted flag.
	 * @param mute True if muted, false if otherwise.
	 */
	public void setMute(boolean mute) {
		this.mute = mute;
	}

	/**
	 * Sets the private chat value.
	 * @param privateChat The private chat value.
	 */
	public void setPrivateChat(int privateChat) {
		this.privateChat = privateChat;
	}

	/**
	 * Sets the public chat value.
	 * @param publicChat The public chat value.
	 */
	public void setPublicChat(int publicChat) {
		this.publicChat = publicChat;
	}

	/**
	 * Sets the player's trade request.
	 * @param request The player's trade request.
	 */
	public void setRequest(Request request) {
		this.request = request;
	}

	/**
	 * Sets the shop.
	 * @param shop The shop.
	 */
	public void setShop(Shop shop) {
		this.shop = shop;
	}

	/**
	 * Sets the trade value.
	 * @param trade The trade value.
	 */
	public void setTrade(int trade) {
		this.trade = trade;
	}

	/**
	 * Sets the trade session.
	 * @param tradeSession The trade session.
	 * @return The trade session.
	 */
	public TradeSession setTradeSession(TradeSession tradeSession) {
		this.tradeSession = tradeSession;
		return tradeSession;
	}

	/**
	 * Sets the voting points.
	 * @param votingPoints The voting points to set.
	 */
	public void setVotingPoints(short votingPoints) {
		this.votingPoints = votingPoints;
	}

	/**
	 * Sets the withdrawing notes flag.
	 * @param withdrawingNotes The flag.
	 */
	public void setWithdrawingNotes(boolean withdrawingNotes) {
		this.withdrawingNotes = withdrawingNotes;
	}

}
