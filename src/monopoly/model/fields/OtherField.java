package monopoly.model.fields;

import monopoly.model.Player;

import java.util.Random;

/**
 * @author Niels Norberg
 */
public class OtherField implements FieldInterface {

    private String name;
    private int number;

    public OtherField(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public void fieldEvent(Player player) {
        if (getName() != "Fængsel" || player.isJailed() == false) {
            System.out.println(player.getName() + " landed on " + getName());
        }
        if (getName() == "?") {
            Random rnd = new Random();
            switch (rnd.nextInt(10)) {
                case 0:
                case 6:
                    int moneyWon = (rnd.nextInt(5) + 1) * 50;
                    System.out.println(player.getName() + " drew a lucky card and won " + moneyWon + " in the lottery");
                    player.pay(-moneyWon);
                    break;

                case 1:
                    player.sendToJail();
                    System.out.println(player.getName() + " was sent to jail for driving with a body in the trunk");
                    break;

                case 2:
                case 7:
                    int moneyLost = (rnd.nextInt(5) + 1) * 20;
                    System.out.println(player.getName() + " was caught speeding and paid a fine of " + moneyLost);
                    player.pay(moneyLost);
                    break;

                case 3:
                case 8:
                    moneyLost = (rnd.nextInt(5) + 1) * 10;
                    System.out.println(player.getName() + " spent " + moneyLost + " on a fine night in a hotel");
                    player.pay(moneyLost);
                    break;

                case 4:
                case 5:
                case 9:
                    moneyWon = (rnd.nextInt(5) + 1) * 35;
                    System.out.println(player.getName() + " worked hard and got a bonus of " + moneyWon);
                    player.pay(-moneyWon);
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "OtherField{" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}
