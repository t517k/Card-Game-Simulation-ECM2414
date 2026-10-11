import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        //check if player has won; will call method during run
    }

    @Override
    public void run(){
        while gameOver
        //write what will happen with each player thread once the thread is run
        System.out.println("Player " + playerId + " has started");

        //player game logic here

        System.out.println("Player " + playerId + "has finished");
    }


}
