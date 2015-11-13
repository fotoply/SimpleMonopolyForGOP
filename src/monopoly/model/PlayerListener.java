package monopoly.model;

/**
 * @author Niels Norberg
 */
public interface PlayerListener {

    /**
     * Is called when any player that is being listened to has their position changed.
     * Is called under any circumstance, even when the player is not moving because of a dice roll.
     * The players current position when this method is called is the new position.
     *
     * @param player the player who's position has changed
     */
    void playerPositionChanged(Player player);

    /**
     * Is called when any player that is being listened to moves across the start tile.
     * Is not called if that player is moving there as a result of being sent to prison.
     *
     * @param player the player who just passed start
     */
    void playerPassedStart(Player player);

    /**
     * Triggered when the player starts his movement
     *
     * @param player    the player who is about to move
     * @param oldAmount the old amount that the player would move
     * @return negative to cancel movement, positive to adjust movement and 0 to ignore the listener
     */
    int playerMoveStart(Player player, int oldAmount);
}
