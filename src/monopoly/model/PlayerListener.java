package monopoly.model;

/**
 * @author Niels Norberg
 */
public interface PlayerListener {
    void playerPositionChanged(Player player);

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
