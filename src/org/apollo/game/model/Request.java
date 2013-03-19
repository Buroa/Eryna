package org.apollo.game.model;

/**
 * Represents a request which a player has to another player.
 * @author Jimmy Frix
 */
public final class Request {

	/**
	 * @author Steve
	 */
	public enum State {
		/**
		 * 
		 */
		PENDING,
		/**
		 * 
		 */
		CONFIRMED,
		/**
		 * 
		 */
		TIMED_OUT;
	}

	/**
	 * A type of {@link Request}.
	 * @author Jimmy Frix
	 */
	public enum Type {
		/**
		 * Represents a type of request intended for a player trade.
		 */
		TRADE_REQUEST;
	}

	/**
	 * @param player
	 * @param acquaintance
	 * @return The trade request.
	 */
	public static Request newPlayerTradeRequest(Player player, Player acquaintance) {
		return new Request(player, acquaintance, Type.TRADE_REQUEST, State.PENDING);
	}

	/**
	 * The player making the request.
	 */
	private final Player player;

	/**
	 * The recipient of the request.
	 */
	private final Player recipient;

	/**
	 * The type of request.
	 */
	private final Type type;

	/**
	 * The time in which the request was created.
	 */
	private final long timeCreated;

	/**
	 * The current state of the request.
	 */
	private State state;

	/**
	 * Creates a new instance of Request.
	 * @param player The player making the request.
	 * @param recipient The recipient of the request.
	 * @param type The type of request.
	 * @param state The initial state of the request.
	 */
	public Request(Player player, Player recipient, Type type, State state) {
		this.player = player;
		this.recipient = recipient;
		this.type = type;
		timeCreated = System.currentTimeMillis();
		this.state = state;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals( java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other.getClass() != getClass())
			return false;
		final Request r = (Request) other;
		if (type != r.type)
			return false;
		return player == r.player && recipient == r.recipient && timeCreated == r.timeCreated;
	}

	/**
	 * Gets the player which made the request.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the recipient.
	 * @return The recipient.
	 */
	public Player getRecipient() {
		return recipient;
	}

	/**
	 * Sets the state of the request.
	 * @return The trade session.
	 */
	public State getState() {
		return state;
	}

	/**
	 * Gets the time in which the request was created.
	 * @return The time.
	 */
	public long getTimeCreated() {
		return timeCreated;
	}

	/**
	 * Gets the type of request.
	 * @return The type.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Gets whether or not the request has timed out based on the the amount of
	 * time specified.
	 * @param timeoutLength The amount of time the request has lived before it's
	 * considered to be timed out.
	 * @return <code>true</code> if the request has timed out,
	 * </code>false</code> if not.
	 */
	public boolean hasTimedOut(int timeoutLength) {
		if (state != State.TIMED_OUT) {
			final boolean timedOut = System.currentTimeMillis() - timeCreated >= timeoutLength;
			if (timedOut) {
				setState(State.TIMED_OUT);
				return true;
			}
		}
		return state == State.TIMED_OUT;
	}

	/**
	 * @param state
	 */
	public void setState(State state) {
		if (state == null)
			throw new IllegalStateException("null request state");
		this.state = state;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Request[Type: " + type + " Player: " + player + " Recipent: " + recipient + "Time sent: " + timeCreated
				+ " State: " + state + "]";
	}
}