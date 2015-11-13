package monopoly.model;

import monopoly.model.MonopolyConstants.Colors;
import monopoly.model.fields.OwnableField;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a player in the board game.
 *
 * @author Niels Norberg
 */
public class Player {
    private String name; // The player's name
    private int pos; // The player's position on the board
    private Colors color; // The color of the player
    private int rounds; // How many rounds the player has completed
    private boolean playerPassedStart; // If a player passed start in the current turn
    private int money = MonopolyConstants.START_MONEY;
    private ArrayList<OwnableField> ownedFields = new ArrayList<>();
    private ArrayList<PlayerListener> listeners = new ArrayList<>();
    private boolean jailed = false;

    /**
     * Initialises the player
     *
     * @param name  name of the player to be displayed. Does not have to be unique
     * @param pos   position of the board of the player. Pos 0 = Field1
     * @param color color of the player
     * @param money starting money of the player
     */
    public Player(String name, int pos, Colors color, int money) {
        this.name = name;
        this.pos = pos;
        this.color = color;
        this.rounds = 0;
        this.money = money;
    }

    /**
     * Initialises the player
     *
     * @param name  name of the player to be displayed. Does not have to be unique
     * @param pos   position of the board of the player. Pos 0 = Field1
     * @param color color of the player
     */
    public Player(String name, int pos, Colors color) {
        this(name, pos, color, MonopolyConstants.START_MONEY);
    }

    /**
     * Initialises the player, can be overloaded with position and color
     *
     * @param name The name of the player to be displayed. Does not have to be unique
     */
    public Player(String name) {
        this(name, 0, Colors.values()[(int) (Math.random() * Colors.values().length)]);
    }

    public void addListener(PlayerListener listener) {
        listeners.add(listener);
    }

    /**
     * Moves the player distance. Will wrap around when the player reaches the 40'th field.
     *
     * @param distance The distance to move the player
     */
    public void move(int distance) {
        for (int i = 0; i < listeners.size(); i++) {
            int newDistance = listeners.get(i).playerMoveStart(this, distance); // Check if any of the listeners have cancelled the movement
            if (newDistance == 0) {
                continue;
            } else if (newDistance < 0) {
                return;
            } else {
                distance = newDistance;
            }
        }
        playerPassedStart = false;
        int oldPos = pos;
        setPos((pos + distance) % MonopolyConstants.BOARDSIZE);
        if (oldPos > pos && jailed != true) {
            playerPassedStart = true;
            for (int i = 0; i < listeners.size() && listeners.get(i) != null; i++) {
                listeners.get(i).playerPassedStart(this);
            }
        }
    }

    public void sendToJail() {
        jailed = true;
        setPos(MonopolyConstants.JAIL_POS);
    }

    /**
     * Attempts to buy the field for player. Will allow an already owned field to be bought (transfered)
     *
     * @param field which field to buy
     * @return true if the field was bought, otherwise false
     */
    public boolean buyField(OwnableField field) {
        if (canPay(field.getPrice())) {
            pay(field.getPrice());
            if (field.getOwner() != null) {
                field.getOwner().getOwnedFields().remove(field);
                field.getOwner().pay(-field.getPrice());
            }
            field.setOwner(this);
            getOwnedFields().add(field);
            System.out.println(getName() + " bought " + field.getName() + " for " + field.getPrice());
            return true;
        } else {
            System.out.println(getName() + " tried to buy " + field.getName() + " but was out of money..");
        }
        return false;
    }

    /**
     * Subtracts amount of money form the player, if possible. Will not make player money go to negative
     *
     * @param amount how much money to subtract from the player. Can be negative
     * @return true if the operation succeeded otherwise false
     */
    public boolean pay(int amount) {
        if (canPay(amount)) {
            money -= amount;
            return true;
        }
        return false;
    }

    /**
     * Transfers money from the player it is called on to the recieving player
     *
     * @param amount          how much money to transfer. Can be negative
     * @param receivingPlayer the player to transfer the money to
     * @return true if the operation succeeded otherwise false
     */
    public boolean pay(int amount, Player receivingPlayer) {
        if (canPay(amount)) {
            pay(amount);
            receivingPlayer.pay(-amount);
            return true;
        }
        return false;
    }

    /**
     * Check if a player can pay a specific amount
     *
     * @param amount check if the player can pay this much
     * @return true if the player can pay that much otherwise false
     */
    public boolean canPay(int amount) {
        if (this.money >= amount) {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
        for (int i = 0; i < listeners.size() && listeners.get(i) != null; i++) {
            listeners.get(i).playerPositionChanged(this);
        }
    }

    public Colors getColor() {
        return color;
    }

    public int getRounds() {
        return rounds;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<OwnableField> getOwnedFields() {
        return ownedFields;
    }

    public void addRound() {
        rounds++;
    }

    /**
     * @return whether the player has just passed start in their current turn
     */
    public boolean hasPlayerPassedStart() {
        return playerPassedStart;
    }

    public boolean isJailed() {
        return jailed;
    }

    public void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", pos=" + pos +
                ", color=" + color +
                ", rounds=" + rounds +
                ", playerPassedStart=" + playerPassedStart +
                ", money=" + money +
                ", ownedFields=" + Arrays.toString(ownedFields.toArray()) +
                ", listeners=" + listeners +
                ", jailed=" + jailed +
                '}';
    }
}
