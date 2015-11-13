package monopoly.model;

/**
 * @author Niels Norberg
 */
public interface MonopolyConstants {
    int START_MONEY = 1500;
    int JAIL_POS = 10;
    int PASSING_START = 200;
    int INCOMETAX = 200;
    int STATETAX = 100;
    int BOARDSIZE = 40;

    /**
     * Names for the different fields on the board. Contains 40 names per default
     */
    String[] FIELD_NAMES = {
            "Start", "Rødovrevej", "?", "Hvidovrevej", "Indkomstskat", "Øresund A/S",
            "Roskildevej", "?", "Valby Langgade", "Allégade", "Fængsel", "Frederiksberg Allé", "Tuborg",
            "Büllowsvej", "Gl. Kongevej", "D.F.D.S A/S", "Bernstorffsvej", "?", "Hellerupvej", "Strandvejen",
            "Parkering", "Trianglen", "?", "Østerbrogade", "Grønningen", "Ø.K. A/S", "Bredgade",
            "Kgs Nytorv", "Carlsberg", "Østergade", "Gå i Fængsel!", "Amagertorv", "Vimmelskaftet", "?", "Nygade",
            "D/S Bornholm 1866", "?", "Frederiksberg Allé", "Statsskat", "Rådhuspladsen"
    };

    /**
     * Fictional player names for quickly generating names
     */
    String[] PLAYER_NAMES = {
            "Karl", "Bob", "Sally", "Vigo", "Michael", "Muhammad", "Andreas", "Mads", "Erik", "Shahab", "Tif", "David",
            "John", "Paul", "Mark", "James", "Andrew", "Scott", "Steven", "Robert", "Stephen", "William", "Craig",
            "Michael", "Stuart", "Christopher", "Alan", "Colin", "Brian", "Kevin", "Gary", "Richard", "Derek", "Martin",
            "Thomas", "Neil", "Barry", "Ian", "Jason", "Iain", "Gordon", "Alexander", "Graeme", "Peter", "Darren",
            "Graham", "George", "Kenneth", "Allan", "Simon", "Douglas", "Keith", "Lee", "Anthony", "Grant", "Ross",
            "Jonathan", "Gavi1n", "Nicholas"
    };

    enum Colors {RED, BLUE, GREEN, YELLOW, BLACK, PURPLE, ORANGE, GREY, PINK, LIGHTBLUE, LIGHTGREEN}
}