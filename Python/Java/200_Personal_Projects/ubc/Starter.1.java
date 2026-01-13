   

import pkg.*;
import java.util.Scanner;
import java.util.Random;

class starter {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        // Face cards array
        String[] faceCards = {"Jack", "Queen", "King", "Ace"};

        // Welcome messages (shown once)
        typeWriter("Welcome to Blackjack!", 100);
        typeWriter("Here are the rules of the game:", 50);
        typeWriter("Press h to hit and s to stand.", 50);
        typeWriter("Closest to 21 wins.", 75);
        typeWriter("Let's get started.", 75);

        boolean playAgain = true;

        while (playAgain) {
            typeWriter("Shuffling the cards...", 50);

            // --- PLAYER TURN ---
            String playerCard1 = drawCard(rand, faceCards);
            String playerCard2 = drawCard(rand, faceCards);
            int playerTotal = cardValue(playerCard1) + cardValue(playerCard2);

            typeWriter("Your cards: " + playerCard1 + ", " + playerCard2, 75);
            typeWriter("Your total: " + playerTotal, 75);

            boolean playerBusted = false;
            while (true) {
                if (playerTotal >= 21) break; // stop if 21 or bust
                typeWriter("Do you want to hit or stand? (h/s)", 75);
                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("h")) {
                    String newCard = drawCard(rand, faceCards);
                    int val = cardValue(newCard);
                    playerTotal += val;
                    typeWriter("You drew: " + newCard + " (+" + val + ")", 75);
                    typeWriter("Your total: " + playerTotal, 75);
                    if (playerTotal > 21) {
                        playerBusted = true;
                        typeWriter("You busted!", 75);
                        break;
                    }
                } else {
                    typeWriter("You chose to stand at " + playerTotal, 75);
                    break;
                }
            }

            // --- DEALER TURN ---
            int dealerTotal = 0;
            if (!playerBusted) {
                String dealerCard1 = drawCard(rand, faceCards);
                String dealerCard2 = drawCard(rand, faceCards);
                dealerTotal = cardValue(dealerCard1) + cardValue(dealerCard2);
                typeWriter("Dealer's cards: " + dealerCard1 + ", " + dealerCard2, 75);
                typeWriter("Dealer's total: " + dealerTotal, 75);

                // Dealer hits until 17 or more
                while (dealerTotal < 17) {
                    String newCard = drawCard(rand, faceCards);
                    int val = cardValue(newCard);
                    dealerTotal += val;
                    typeWriter("Dealer draws: " + newCard + " (+" + val + ")", 75);
                    typeWriter("Dealer's total: " + dealerTotal, 75);
                }

                if (dealerTotal > 21) {
                    typeWriter("Dealer busted! You win!", 75);
                }
            }

            // --- WINNER CHECK ---
            if (playerBusted) {
                typeWriter("Dealer wins by default.", 75);
            } else if (dealerTotal > 21) {
                typeWriter("Dealer busted. You win!", 75);
            } else if (playerTotal > dealerTotal) {
                typeWriter("You win! " + playerTotal + " vs " + dealerTotal, 75);
            } else if (dealerTotal > playerTotal) {
                typeWriter("Dealer wins! " + dealerTotal + " vs " + playerTotal, 75);
            } else {
                typeWriter("It's a tie! Both at " + playerTotal, 75);
            }

            // --- PLAY AGAIN? ---
            typeWriter("Would you like to play again? (y/n)", 75);
            String again = sc.nextLine();
            if (!again.equalsIgnoreCase("y")) {
                playAgain = false;
                typeWriter("Thanks for playing Blackjack!", 75);
            }
        }
    }

    // Typewriter method
    public static void typeWriter(String text, int delay) {
        for (int i = 0; i < text.length(); i++) {
            System.out.print(text.charAt(i));
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    // Method to draw a random card
    public static String drawCard(Random rand, String[] faceCards) {
        boolean pickNumber = rand.nextBoolean();
        if (pickNumber) {
            int number = rand.nextInt(9) + 2; // 2–10 inclusive
            return Integer.toString(number);
        } else {
            int faceIndex = rand.nextInt(faceCards.length); // 0–3
            return faceCards[faceIndex];
        }
    }

    // Method to get card value
    public static int cardValue(String card) {
        if (card.equals("Jack") || card.equals("Queen") || card.equals("King")) {
            return 10;
        } else if (card.equals("Ace")) {
            return 11; // simplified Ace = 11
        } else {
            return Integer.parseInt(card);
        }
    }
}
