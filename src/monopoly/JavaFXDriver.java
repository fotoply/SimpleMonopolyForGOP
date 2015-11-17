package monopoly;/**
 * Created 11/17/15
 *
 * @author Niels Norberg
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import monopoly.model.Dice;
import monopoly.model.MonopolyConstants;
import monopoly.model.Player;
import monopoly.model.PlayerListener;
import monopoly.model.fields.*;
import monopoly.view.MainViewController;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class JavaFXDriver extends Application implements PlayerListener {

    private FieldInterface[] board;
    private Dice diceCup;
    private boolean playerHasWon;
    private ObservableList<Player> players = FXCollections.observableArrayList();
    private MainViewController mainViewController;
    private Stage primaryStage;
    private Player currentPlayer;
    private int timesSame = 0;

    public static void main(String[] args) {
        launch(args);
    }

    public Dice getDiceCup() {
        return diceCup;
    }

    public ObservableList<Player> getPlayers() {
        return players;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        loadWindow();
        diceCup = new Dice();
        diceCup.addListener(mainViewController);
        fillFields();

        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                mainViewController.addText(String.valueOf((char) b));
            }
        };
        System.setOut(new PrintStream(out, true));
    }

    public void loadWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainView.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        mainViewController = loader.getController();
        primaryStage.show();
        mainViewController.setDriver(this);
    }

    public void createPlayer(String name) {
        Player player = new Player(name);
        player.addListener(this);
        players.add(player);
    }

    public void removePlayer(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                players.remove(player);
                return;
            }
        }
    }

    public void fillFields() {
        board = new FieldInterface[MonopolyConstants.BOARDSIZE];
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

    public void doPlayerTurn() {
        if (currentPlayer == null) {
            currentPlayer = players.get(0);
        }
        if (diceCup.isPair()) {
            timesSame++;
            if (timesSame == 3) {
                currentPlayer.sendToJail();
                System.out.println(currentPlayer.getName() + " was caught and sent to jail");
                return;
            }
        } else {
            timesSame = 0;
            if (players.indexOf(currentPlayer) == players.size() - 1) {
                this.currentPlayer = players.get(0);
            } else {
                this.currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
            }
        }
        diceCup.throwDice();
        currentPlayer.move(diceCup.getSum());
        mainViewController.updateButtons();
    }

    public void buyCurrentField() {
        if (currentPlayerCanBuy()) {
            currentPlayer.buyField((OwnableField) board[currentPlayer.getPos()]);
        }
    }

    public void upgradeCurrentField() {
        if (currentPlayerCanUpgrade()) {
            ((StreetField) board[currentPlayer.getPos()]).upgrade();
        }
    }

    public boolean currentPlayerCanBuy() {
        return currentPlayer.canBuyField(board[currentPlayer.getPos()]);
    }

    public FieldInterface getCurrentField() {
        return board[currentPlayer.getPos()];
    }

    public boolean currentPlayerCanUpgrade() {
        if (board[currentPlayer.getPos()] instanceof StreetField) {
            if (((StreetField) board[currentPlayer.getPos()]).canUpgrade() && ((StreetField) board[currentPlayer.getPos()]).getOwner() == currentPlayer) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void playerPositionChanged(Player player) {
        board[player.getPos()].fieldEvent(player);
    }

    @Override
    public void playerPassedStart(Player player) {
        if (player.getRounds() > 1000) { // Game should not end after any sensible amount of rounds
            playerHasWon = true;
            System.out.println(player.getName() + " has won!");
            return;
        }
        player.addRound();
        player.reward(MonopolyConstants.PASSING_START);
        System.out.println(player.getName() + " just passed start and got " + MonopolyConstants.PASSING_START);
    }

    @Override
    public int playerMoveStart(Player player, int distance) {
        if (player.isJailed()) {
            if (diceCup.isPair()) {
                player.setJailed(false);
                System.out.println(player.getName() + " got out of jail with a " + diceCup.getSum());
                return diceCup.getSum();
            } else {
                System.out.println(player.getName() + " failed to leave jail");
                return -1;
            }
        }
        return 0;
    }
}
