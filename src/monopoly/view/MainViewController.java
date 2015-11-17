package monopoly.view;

/**
 * Created 11/17/15
 *
 * @author Niels Norberg
 */

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import monopoly.JavaFXDriver;
import monopoly.model.Dice;
import monopoly.model.DiceRolledListener;
import monopoly.model.Player;
import monopoly.model.fields.StreetField;

public class MainViewController implements DiceRolledListener {

    private JavaFXDriver driver;

    @FXML
    private TextArea textPane;

    @FXML
    private TableView<Player> playerView;

    @FXML
    private TableColumn<Player, String> playerNameColumn;

    @FXML
    private TableColumn<Player, Integer> playerMoneyColumn;

    @FXML
    private TableColumn<Player, Integer> currentFieldColumn;

    @FXML
    private Button addPlayerButton;

    @FXML
    private Button removePlayerButton;

    @FXML
    private Button die1;

    @FXML
    private Button die2;
    @FXML
    private TextField playerNameField;
    @FXML
    private Button buyFieldButton;
    @FXML
    private Button upgradeFieldButton;
    @FXML
    private Text currentFieldText;
    @FXML
    private Text currentUpgradesText;

    @Override
    public void diceRolled(Dice dice) {
        die1.setText(dice.getDice().get(0).getResult() + "");
        die2.setText(dice.getDice().get(1).getResult() + "");
    }

    @FXML
    private void initialize() {
        playerNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        playerMoneyColumn.setCellValueFactory(param -> param.getValue().moneyProperty().asObject());
        currentFieldColumn.setCellValueFactory(param -> param.getValue().posProperty().asObject());
    }

    @FXML
    void addPlayerClicked(ActionEvent event) {
        if (!playerNameField.getText().equals("")) {
            driver.createPlayer(playerNameField.getText());
            playerNameField.setText("");
        }
    }

    public void addText(String text) {
        Platform.runLater(() -> textPane.appendText(text));
    }

    @FXML
    void removePlayerClicked(ActionEvent event) {
        if (!playerNameField.getText().equals("")) {
            driver.removePlayer(playerNameField.getText());
            playerNameField.setText("");
        }
    }

    public void setDriver(JavaFXDriver driver) {
        this.driver = driver;
        playerView.setItems(driver.getPlayers());
    }

    @FXML
    void startButtonClicked(ActionEvent event) {
        driver.doPlayerTurn();
    }

    @FXML
    void upgradeFieldClicked(ActionEvent event) {
        driver.upgradeCurrentField();
    }

    @FXML
    void buyFieldClicked(ActionEvent event) {
        driver.buyCurrentField();
    }

    public void updateButtons() {
        if (driver.currentPlayerCanBuy()) {
            buyFieldButton.setDisable(false);
        } else {
            buyFieldButton.setDisable(true);
        }

        if (driver.currentPlayerCanUpgrade()) {
            upgradeFieldButton.setDisable(false);
        } else {
            upgradeFieldButton.setDisable(true);
        }

        currentFieldText.setText(driver.getCurrentField().getName());
        if (driver.getCurrentField() instanceof StreetField) {
            currentUpgradesText.setText(((StreetField) driver.getCurrentField()).getUpgrades() + "");
        } else {
            currentUpgradesText.setText("Not upgradeable");
        }
    }
}
