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


    private void takeCard(){
        //player takes a card from the deck on their left which is their playerId
    }

    private void discardCard(){
        //player discards a card from the deck on their right which is their playerId + 1
        // in the case of the last player e.g player 4, if playerId == numPlayers then they discard to deck 1
    }

    @Override
    public void run() {
        String filename = "player" + playerId + "_output.txt";

        try (FileWriter playerOutput = new FileWriter(filename)) {

            playerOutput.write("Player " + playerId + " has started\n");

            while (!hasWon) {
                // Your player thread's game logic goes here

                hasWon(); // Your existing method, if appropriate
            }

            playerOutput.write("Player " + playerId + " has finished\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Player " + playerId + " has won, with hand " + playerHand);
    }