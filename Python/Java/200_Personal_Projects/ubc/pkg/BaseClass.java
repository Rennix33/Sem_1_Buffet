import java.util.*;

// Card suit enum
enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }

// Card class
class Card {
    int rank;
    Suit suit;
    Card(int rank, Suit suit) { this.rank = rank; this.suit = suit; }
    @Override
    public String toString() {
        String r = switch(rank) {
            case 14 -> "A"; case 13 -> "K"; case 12 -> "Q"; case 11 -> "J";
            default -> Integer.toString(rank);
        };
        char s = switch(suit) {
            case CLUBS -> '♣'; case DIAMONDS -> '♦';
            case HEARTS -> '♥'; case SPADES -> '♠';
        };
        return r + s;
    }
}

// Deck class
class Deck {
    List<Card> cards = new ArrayList<>();
    Random rand = new Random();
    Deck() {
        for (Suit s : Suit.values())
            for (int r = 2; r <= 14; r++)
                cards.add(new Card(r, s));
    }
    void shuffle() { Collections.shuffle(cards, rand); }
    Card deal() { return cards.remove(cards.size() - 1); }
}

// Player class
class Player {
    String name;
    List<Card> hand = new ArrayList<>();
    Player(String name) { this.name = name; }
}

// Main public class
public class BaseClass {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Create players
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        Player human = new Player(name);
        Player ai = new Player("AI");

        // Create and shuffle deck
        Deck deck = new Deck();
        deck.shuffle();

        // Deal two cards each
        human.hand.add(deck.deal());
        human.hand.add(deck.deal());
        ai.hand.add(deck.deal());
        ai.hand.add(deck.deal());

        // Show hands
        System.out.println("\nYour hand: " + human.hand.get(0) + " " + human.hand.get(1));
        System.out.println("AI hand: " + ai.hand.get(0) + " " + ai.hand.get(1));

        // Deal 5 community cards
        List<Card> board = new ArrayList<>();
        for (int i = 0; i < 5; i++) board.add(deck.deal());
        System.out.println("Board: " + board);

        // Determine winner (highest single card)
        int humanBest = Math.max(human.hand.get(0).rank, human.hand.get(1).rank);
        int aiBest = Math.max(ai.hand.get(0).rank, ai.hand.get(1).rank);

        if (humanBest > aiBest) System.out.println("You win!");
        else if (aiBest > humanBest) System.out.println("AI wins!");
        else System.out.println("It's a tie!");
    }
}
