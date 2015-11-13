package monopoly;

import monopoly.model.Dice;
import monopoly.model.MonopolyConstants;
import monopoly.model.Player;
import monopoly.model.PlayerListener;
import monopoly.model.fields.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * The driver class which is in charge of most of the game logic itself, such as initialising the game and handling turns.
 *
 * @author Niels Norberg
 */
public class TextBasedDriver implements PlayerListener {
    private FieldInterface[] board;
    private Dice diceCup;
    private boolean playerHasWon;

    public TextBasedDriver() {
        board = new FieldInterface[MonopolyConstants.BOARDSIZE];
        diceCup = new Dice();
        populateFields();
    }

    public static void main(String[] args) {
        TextBasedDriver textBasedDriver = new TextBasedDriver();
        textBasedDriver.play();
    }

    public void populateFields() {
        int groupId = 0;
        int subVal = 0;
        for (int i = 0; i < MonopolyConstants.BOARDSIZE; i++) { // Initialize the board with different fields
            if (subVal == 3) {
                groupId++;
                subVal = 0;
            }
            subVal++;
            switch (i + 1) {
                case 1:    // Andre felter
                case 3:
                case 5:
                case 8:
                case 11:
                case 18:
                case 21:
                case 23:
                case 34:
                case 37:
                case 39:
                    board[i] = new OtherField(MonopolyConstants.FIELD_NAMES[i], i + 1);
                    break;
                case 31:    // Gå i spjældet:
                    board[i] = new GoToJail(MonopolyConstants.FIELD_NAMES[i], i + 1);
                    break;
                case 6:    // Rederier:
                case 16:
                case 26:
                case 36:
                    board[i] = new ShippingCompany(MonopolyConstants.FIELD_NAMES[i], i + 1, 50, 200);
                    break;
                case 13:
                case 29:    //Bryggerier:
                    // Her er diceCup en parameter, da betaling afhænger af antal øjne der er slået
                    board[i] = new Brewery(MonopolyConstants.FIELD_NAMES[i], i + 1, diceCup, 150);
                    break;
                default:    // Gader:
                    board[i] = new StreetField(MonopolyConstants.FIELD_NAMES[i], i + 1, i * 3, i * 10, groupId);
            }
        }
    }

    /**
     * Starts the game. Function terminates when game ends
     */
    public void play() {
        Logger.getGlobal().info("Game starting");
        Player[] players; // Initialize the players
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nPlease enter how many players: ");
        players = new Player[scanner.nextInt()];
        if (players.length <= 0) {
            System.out.println("No players, game ending"); // Game cannot be started if there is no or negative players
            return;
        }
        if (players.length > MonopolyConstants.PLAYER_NAMES.length) {
            System.out.println("Too many players, max is " + MonopolyConstants.PLAYER_NAMES.length);
            return;
        }

        System.out.print("\nDo you wanna enter names yourself? (Y/N)");
        scanner.nextLine(); // To handle a floating enter error
        boolean hasAnswered = false;
        while (!hasAnswered) {
            switch (scanner.nextLine().toLowerCase()) { // Get the next line of input frm the user
                case "y":
                    for (int i = 0; i < players.length; i++) {
                        System.out.println("Please enter player " + (i + 1) + "'s name: ");
                        players[i] = new Player(scanner.nextLine()); // Name the player what the user input
                    }
                    hasAnswered = true;
                    System.out.println("Players has been named.");
                    break;

                case "n":
                    ArrayList<String> names = new ArrayList<>();
                    Collections.addAll(names, MonopolyConstants.PLAYER_NAMES);
                    Collections.shuffle(names); // Shuffle the list of names
                    for (int i = 0; i < players.length; i++) { // Pick names from the shuffled list, starting from the start to avoid collisions in names
                        players[i] = new Player(names.get(i));
                    }
                    System.out.println("Players has been named.");
                    hasAnswered = true;
                    break;

                default:
                    System.out.println("Invalid answer, try again");
            }
        }

        for (Player player : players) {
            player.addListener(this);
        }

        playerHasWon = false; // Boolean describing whether any player in the game has won yet and therefor if the game loop should be stopped.
        while (!playerHasWon) {
            for (Player currentPlayer : players) {
                doPlayerTurn(diceCup, currentPlayer);
                if (playerHasWon) {
                    break;
                }
            }
        }
    }

    public void doPlayerTurn(Dice dices, Player currentPlayer) {
        int timesSame = 0;
        do { // Roll the die until a player does not roll two of the same die
            dices.throwDice();
            System.out.println(currentPlayer.getName() + " rolled " + dices.getSum());
            if (dices.isPair()) {
                timesSame++;
            }
            if (timesSame == 2) {
                currentPlayer.sendToJail();
                System.out.println(currentPlayer.getName() + " was caught and sent to jail");
                return;
            }
            currentPlayer.move(dices.getSum());
            if (dices.isPair() && !currentPlayer.isJailed()) {
                System.out.println(currentPlayer.getName() + " throwing again.");
            }
        } while (dices.isPair() && !currentPlayer.isJailed());
    }

    @Override
    public void playerPositionChanged(Player player) {
        board[player.getPos()].fieldEvent(player);
    }

    @Override
    public void playerPassedStart(Player player) {
        if (player.getRounds() > 3) {
            playerHasWon = true;
            System.out.println(player.getName() + " has won!");
            return;
        }
        player.addRound();
        player.pay(-MonopolyConstants.PASSING_START);
        System.out.println(player.getName() + " just passed start and got " + MonopolyConstants.PASSING_START);
    }

    @Override
    public int playerMoveStart(Player player, int distance) {
        if (player.isJailed()) {
            if (diceCup.isPair()) {
                player.setJailed(false);
                return 0;
            } else {
                for (int i = 0; i < 2; i++) {
                    diceCup.throwDice();
                    if (diceCup.isPair()) {
                        player.setJailed(false);
                        System.out.println(player.getName() + " rolled a " + diceCup.getSum() + " and got out of jail");
                        return diceCup.getSum();
                    }
                    System.out.println(player.getName() + " rolled a " + diceCup.getSum());
                }
                System.out.println(player.getName() + " failed to leave jail");
                return -1;
            }
        }
        return 0;
    }
}
