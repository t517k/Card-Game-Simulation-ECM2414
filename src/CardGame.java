import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {
    ArrayList<Card> pack;
    ArrayList<Card> decks;
    int playerTotal;
    int deckTotal;
    String packPath;

    static void main(String[] args) {
        CardGame currentGame = new CardGame();
        Scanner playerNumberin = new Scanner(System.in);
        System.out.println("Game Start!");
        while (true) {
            try {
                System.out.print("Enter number of players:");
                int input = Integer.parseInt(playerNumberin.nextLine());
                if (input > 1) {
                    currentGame.playerTotal = input;
                    playerNumberin.close();
                    break;
                } else {
                    System.out.println("Invalid input! Please enter a valid number of players greater than 1!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a value of the integer type!");
            }
        }
        do while (true) {
            try {
                System.out.println("Enter path of the chosen pack file:");
                String input = playerNumberin.nextLine();
                File packFile = new File(input);
                Scanner test = new Scanner(packFile);
                test.close();
                currentGame.packPath = input;
                break;
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
            System.out.println("File open error!");
        }
    }

    private boolean checkPack(int playerTotal, String packPath) throws IOException {
        int lengthValue = playerTotal * 8;
        int noOfLines = 0;
        BufferedReader reader = new BufferedReader(new FileReader(packPath));
        reader.mark(0);
        while (reader.readLine() != null) {
            noOfLines++;
        }
        reader.reset();
        if (lengthValue != noOfLines) {
            System.out.println("Invalid file! File length does not meet required value of No. of players * 8!");
            return false;
        }
        try {
            while (reader.readLine() != null) {
                int test = Integer.parseInt(reader.readLine());
                if (test < 1) {
                    System.out.println("Invalid file! line contains a negative value!");
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid file! Line value is not of correct integer type!");
            return false;
        }
        return true;
    }
}
