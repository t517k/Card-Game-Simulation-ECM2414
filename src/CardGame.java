import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class CardGame {
    ArrayList<Card> pack;
    ArrayList<Card> decks;
    int playerTotal;
    int deckTotal;
    String packPath;

    public static void main(String[] args) {
        CardGame currentGame = new CardGame();
        Scanner playerNumberin = new Scanner(System.in);
        System.out.println("Game Start!");
        while (true) {
            System.out.println("Enter number of players:");
            int input = playerNumberin.nextInt();
            if (input < 1 || !playerNumberin.hasNextInt()) {
                System.out.println("Invalid input!");
            } else {
                currentGame.playerTotal = input;
                break;
            }
        }
        while (true) {
            System.out.println("Enter path of the chosen pack file:");
            String input = playerNumberin.nextLine();
            try {

            } catch {

            }
        }
    }

    public void loadPack(int playerTotal, String packPath) {
        try {
            File packFile = new File(packPath);
            Scanner fileReader = new Scanner(packFile);
            while (fileReader.hasNextLine()) {
                int cardValue = fileReader.nextInt();
                Card card = new Card(playerTotal, cardValue);
                pack.add(card);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    public Boolean checkPack(int playerTotal, String packPath) {

    }
}
