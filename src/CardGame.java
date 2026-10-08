import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {
    private ArrayList<Card> pack;
    private ArrayList<Card> decks;
    private int playerTotal;
    private int deckTotal;
    private String packPath;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Game Start!");
        System.out.println("Enter number of players:");
        int playerTotal = in.nextInt();
        System.out.println("Enter path of pack to load:");
        String packPath = in.next();

    }
}
