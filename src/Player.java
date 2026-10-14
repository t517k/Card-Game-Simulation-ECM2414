import java.io.*;
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

    private boolean hasWon() {
        if (playerHand.size() != 4) {
            return false;
        }

        int firstValue = playerHand.get(0).getValue();

        for (int i = 1; i < playerHand.size(); i++) {
            if (playerHand.get(i).getValue() != firstValue) {
                return false;
            }
        }

        return true;
    }

    private String createPlayerOutput() throws IOException {
        String filename = "player" + playerId "_output.txt";
        playerOutput = new BufferedWriter(new FileWriter(filename));
    }

    private void writeToOutput(String message) throws IOException{
        playerOutput.write(message);
        playerOutput.newLine();
        playerOutput.flush();
}

    private void takeCard() throws IOException {

        Card drawnCard = null;

        if (drawnCard != null) {
            playerHand.add(drawnCard);
            //player takes a card from the deck on their left which is their playerId
            writeToOutput("player " + playerId + " draws a " + drawnCard + " from deck " + playerId);
        }
    }


    private Card discardCard() throws IOException {
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);

            if (card.getValue() != playerId) {
                Card discardedCard = playerHand.remove(i);


                int rightDeck = (playerId % NumOfPlayers) + 1;

                writeToOutput("player " + playerId  + " discards a " + discardedCard.getValue() + " to deck " + rightDeck);
                return discardedCard;
            }
        }

        return null;
    }

    @Override
    public void run() {
            if (hasWon()) {
                System.out.println("Player " + playerId + " wins");

                gameOver.set(true);
                return;
            }
        try () {
            createPlayerOutput();
            writeToOutput("player " + playerId + " has started");
            writeToOutput("player " +  playerId + " initial hand " + playerHand);

            while (!gameOver.get()) {
                takeCard();
                Card discarded = discardCard(); //replace discard once deck logic is finished and stuff
                hasWon();
            }

            writeToOutput("Player " + playerId + " has finished");
            writeToOutput("Player " + playerId + " final hand: " + playerHand);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (playerOutput != null) {
                    playerOutput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }