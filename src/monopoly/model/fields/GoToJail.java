package monopoly.model.fields;

import monopoly.model.Player;

/**
 * @author Niels Norberg
 */
public class GoToJail implements FieldInterface {
    private String name;
    private int number;

    public GoToJail(String name, int number) {
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
        System.out.println(player.getName() + " was sent to jail");
        player.sendToJail();
    }

    @Override
    public String toString() {
        return "GoToJail{" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}