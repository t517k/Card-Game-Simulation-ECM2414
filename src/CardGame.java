import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

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
            try {
                System.out.print("Enter number of players:");
                int input = Integer.parseInt(playerNumberin.nextLine());
                if (input > 1) {
                    currentGame.playerTotal = input;
                    break;
                } else {
                    System.out.println("Invalid input! Please enter a valid number of players greater than 1!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a value of the integer type!");
            }
        }
        while (true) {
            try {
                System.out.println("Enter path of the chosen pack file:");
                String input = playerNumberin.nextLine();
                File packFile = new File(input);
            } catch  (FileNotFoundException e) {
                System.out.println("Invalid path! Please enter a valid path!");
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

    public Boolean checkPack(int playerTotal, String packPath) throws IOException {
        int lengthValue = playerTotal * 8;
        int noOfLines = 0;
        BufferedReader reader = new BufferedReader(new FileReader(packPath));
        reader.mark(0);
        while (reader.readLine() != null) {
            noOfLines++;
        }
        reader.reset();
        if (lengthValue != noOfLines) {
            return false;
        } else {
            continue;
        }


    }
}
