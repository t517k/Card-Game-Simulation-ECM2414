import java.util.ArrayList;

public class Player extends Threads{
    int playerId;
    static int NumOfPlayers = 0;
    ArrayList<Card> playerHand;

    public Player(int NumOfPlayers){
        this.playerId = +NumOfPlayers;
    }

    public void run(){
        //write what will happen with each player thread once the thread is run
    }


}
