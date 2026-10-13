import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Player implements  Runnable{
    int playerId;
    static int NumOfPlayers = 0;
    private final AtomicBoolean gameOver;
    ArrayList<Card> playerHand;

    public Player(int NumOfPlayers, AtomicBoolean gameOver){
        this.playerId = +NumOfPlayers;
        this.gameOver = gameOver;
    }

    private boolean hasWon(){
        playerHand firstelement = playerHand.get(0);
        for (int x = 0; i <=3; i++){
            if (x.equals(firstelement)){
                return false;
            }
            else {
                return true;
            }
        }
    }

    private string createPlayerOutput(){
    //method might be used to create playeroutput.txt
}

    private void writeToOutput(){
    //method might be used to write to playeroutput files
}

    private void takeCard(){
        int x = 0; //placeholder
        playerHand.add(4, x);
        //player takes a card from the deck on their left which is their playerId
    }

    private int discardCard() {
        for (int i = 0; i <= 4; i++) {
            if (playerHand[i] == playerId){
                i = i + 1;
            }
            else{
                return i;
            }
            playerOutput.write("player " + playerId + " discards a " + i + " to deck x") //change to reference of deck that player will discard to
            //player discards a card from the deck on their right which is their playerId + 1
            // in the case of the last player e.g player 4, if playerId == numPlayers then they discard to deck 1
        }
        return i;

    }
    @Override
    public void run() {
        String filename = "player" + playerId + "_output.txt";

        try (FileWriter playerOutput = new FileWriter(filename)) {

            playerOutput.write("Player " + playerId + " has started\n");

            hasWon();
            while (!hasWon) {
                // Your player thread's game logic goes here
                takeCard();
                discardCard();
                hasWon(); // Your existing method, if appropriate
            }

            playerOutput.write("Player " + playerId + " has finished\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Player " + playerId + " has won, with hand " + playerHand);
    }