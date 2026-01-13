import java.util.*;
import java.util.stream.Collectors;

/**
 * Poker.java
 * Simple console Texas Hold'em implementation.
 *
 * Save as Poker.java
 *
 * Note: This is a single-file, self-contained program meant for local use on a machine
 * with a graphical/display-capable Java environment (console).
 *
 * Controls: follow printed prompts. Use numbers/letters as requested.
 */
public class Poker {
    public static void main(String[] args) {
        GameConfig cfg = new GameConfig();
        PokerGame game = new PokerGame(cfg);
        game.start();
    }
}

/* ===== Configuration ===== */
class GameConfig {
    int startingChips = 1000;
    int smallBlind = 10;
    int bigBlind = 20;
    int numAIs = 3; // number of AI opponents (change if you want)
    int maxPlayers = 6;
    boolean verboseAI = false;
}

/* ===== Card, Deck ===== */
enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }

class Card {
    final int rank; // 2..14 (11=J,12=Q,13=K,14=A)
    final Suit suit;
    Card(int rank, Suit suit) { this.rank = rank; this.suit = suit; }
    @Override public String toString() {
        String r = switch(rank) {
            case 14 -> "A";
            case 13 -> "K";
            case 12 -> "Q";
            case 11 -> "J";
            default -> Integer.toString(rank);
        };
        char s = switch(suit) {
            case CLUBS -> '♣';
            case DIAMONDS -> '♦';
            case HEARTS -> '♥';
            case SPADES -> '♠';
        };
        return r + s;
    }
}

class Deck {
    private final List<Card> cards = new ArrayList<>();
    private final Random rand = new Random();
    Deck() {
        for (Suit s : Suit.values()) {
            for (int r = 2; r <= 14; r++) cards.add(new Card(r, s));
        }
    }
    void shuffle() { Collections.shuffle(cards, rand); }
    Card deal() {
        if (cards.isEmpty()) throw new IllegalStateException("Deck empty");
        return cards.remove(cards.size()-1);
    }
}

/* ===== Player ===== */
class Player {
    final String name;
    boolean isHuman;
    int chips;
    List<Card> hole = new ArrayList<>();
    boolean folded = false;
    boolean allIn = false;
    int currentBet = 0;
    Player(String name, boolean isHuman, int chips) {
        this.name = name; this.isHuman = isHuman; this.chips = chips;
    }
    void resetForRound() {
        hole.clear();
        folded = false;
        allIn = false;
        currentBet = 0;
    }
    @Override public String toString() {
        return name + (isHuman ? " (you)" : "") + " ["+chips+"]";
    }
}

/* ===== Hand evaluator =====
   Produce a numeric score where higher = better.
   Score layout: category * 1e10 + tiebreaker components packed.
   Categories 9..0: 9=Royal/straight flush (we'll treat straight flush as 9), 8=fourKind, 7=fullHouse, 6=flush, 5=straight, 4=threeKind,
   3=twoPair, 2=onePair, 1=highCard
*/
class HandEvaluator {
    // Evaluate best 5-card hand from up to 7 cards
    static HandValue evaluate(List<Card> cards) {
        if (cards.size() < 5) throw new IllegalArgumentException("Need >=5 cards to evaluate");
        long bestScore = Long.MIN_VALUE;
        List<Card> bestHand = null;
        int n = cards.size();
        int[] idx = new int[5];
        for (idx[0]=0; idx[0]<n-4; idx[0]++)
         for (idx[1]=idx[0]+1; idx[1]<n-3; idx[1]++)
          for (idx[2]=idx[1]+1; idx[2]<n-2; idx[2]++)
           for (idx[3]=idx[2]+1; idx[3]<n-1; idx[3]++)
            for (idx[4]=idx[3]+1; idx[4]<n; idx[4]++) {
                List<Card> five = List.of(cards.get(idx[0]), cards.get(idx[1]), cards.get(idx[2]), cards.get(idx[3]), cards.get(idx[4]));
                long score = scoreFive(five);
                if (score > bestScore) { bestScore = score; bestHand = five; }
            }
        return new HandValue(bestScore, bestHand);
    }

    // Score a 5-card hand
    private static long scoreFive(List<Card> five) {
        // sort by rank desc
        List<Card> c = new ArrayList<>(five);
        c.sort((a,b)->b.rank - a.rank);
        boolean isFlush = c.stream().map(card->card.suit).distinct().count() == 1;
        // handle straight (A as high or A as low)
        List<Integer> ranks = c.stream().map(card->card.rank).distinct().collect(Collectors.toList());
        // for straight detection, produce sequence with Ace as 14 and also as 1 for wheel
        List<Integer> rForStraight = new ArrayList<>(ranks);
        if (rForStraight.contains(14)) {
            rForStraight.add(0, 1); // Ace low considered as 1 for wheel (we'll treat accordingly)
        }
        boolean isStraight = false;
        int straightHigh = 0;
        // check consecutive
        for (int i=0;i<rForStraight.size();i++) {
            int start = rForStraight.get(i);
            int consec = 1;
            int last = start;
            for (int j=i+1;j<rForStraight.size();j++) {
                if (rForStraight.get(j) == last-1) { consec++; last = rForStraight.get(j); }
                else if (rForStraight.get(j) == last) continue;
                else break;
            }
            if (consec >=5) { isStraight = true; straightHigh = start; break; }
        }
        // counts
        Map<Integer,Integer> cnt = new HashMap<>();
        for (Card card : c) cnt.put(card.rank, cnt.getOrDefault(card.rank,0)+1);
        List<Integer> byCount = cnt.keySet().stream().sorted((a,b)->{
            int ca = cnt.get(a), cb = cnt.get(b);
            if (ca != cb) return cb - ca; // higher count first
            return b - a; // higher rank first
        }).collect(Collectors.toList());

        // Determine category and tiebreakers
        // Four of a kind
        if (byCount.size()>=1 && cnt.get(byCount.get(0))==4) {
            int four = byCount.get(0);
            int kicker = byCount.size()>1 ? byCount.get(1) : 0;
            return packScore(8, four, kicker, 0,0,0);
        }
        // Full house (3 + 2)
        if (cnt.containsValue(3) && cnt.containsValue(2)) {
            int three = byCount.stream().filter(k->cnt.get(k)==3).findFirst().orElse(byCount.get(0));
            int pair = byCount.stream().filter(k->cnt.get(k)==2).findFirst().orElse(byCount.stream().filter(k->cnt.get(k)!=3).findFirst().orElse(0));
            return packScore(7, three, pair, 0,0,0);
        }
        // Flush
        if (isFlush && !isStraight) {
            // tiebreakers: ranks descending
            int a1 = c.get(0).rank, a2 = c.get(1).rank, a3 = c.get(2).rank, a4 = c.get(3).rank, a5 = c.get(4).rank;
            return packScore(6, a1, a2, a3, a4, a5);
        }
        // Straight or straight flush (if isFlush and isStraight then straight flush)
        if (isStraight) {
            if (isFlush) {
                return packScore(9, straightHigh, 0,0,0,0); // straight flush
            } else {
                return packScore(5, straightHigh, 0,0,0,0);
            }
        }
        // Three of a kind
        if (cnt.containsValue(3)) {
            int three = byCount.stream().filter(k->cnt.get(k)==3).findFirst().orElse(byCount.get(0));
            List<Integer> kickers = byCount.stream().filter(k->cnt.get(k)==1).collect(Collectors.toList());
            int k1 = kickers.size()>0 ? kickers.get(0) : 0;
            int k2 = kickers.size()>1 ? kickers.get(1) : 0;
            return packScore(4, three, k1, k2,0,0);
        }
        // Two pair
        long pairsCount = cnt.values().stream().filter(v->v==2).count();
        if (pairsCount >= 2) {
            List<Integer> pairs = byCount.stream().filter(k->cnt.get(k)==2).collect(Collectors.toList());
            int p1 = pairs.get(0), p2 = pairs.get(1);
            int kicker = byCount.stream().filter(k->cnt.get(k)==1).findFirst().orElse(0);
            return packScore(3, p1, p2, kicker,0,0);
        }
        // One pair
        if (pairsCount == 1) {
            int pair = byCount.stream().filter(k->cnt.get(k)==2).findFirst().orElse(byCount.get(0));
            List<Integer> kickers = byCount.stream().filter(k->cnt.get(k)==1).collect(Collectors.toList());
            int k1 = kickers.size()>0 ? kickers.get(0) : 0;
            int k2 = kickers.size()>1 ? kickers.get(1) : 0;
            int k3 = kickers.size()>2 ? kickers.get(2) : 0;
            return packScore(2, pair, k1, k2, k3, 0);
        }
        // High card
        int h1 = c.get(0).rank, h2 = c.get(1).rank, h3 = c.get(2).rank, h4 = c.get(3).rank, h5 = c.get(4).rank;
        return packScore(1, h1, h2, h3, h4, h5);
    }

    // pack category and five tiebreaker ints into a long
    private static long packScore(int category, int a, int b, int c, int d, int e) {
        // ensure each field fits in 8 bits (0..255) - ranks <= 14 so fine
        long score = category;
        score = (score << 8) | (a & 0xFF);
        score = (score << 8) | (b & 0xFF);
        score = (score << 8) | (c & 0xFF);
        score = (score << 8) | (d & 0xFF);
        score = (score << 8) | (e & 0xFF);
        return score;
    }
}

/* HandValue holds numeric score and the 5-card winning combination */
class HandValue {
    final long score;
    final List<Card> bestFive;
    HandValue(long score, List<Card> bestFive) { this.score = score; this.bestFive = bestFive; }
    String pretty() {
        int category = (int)(score >> 40);
        String cat = switch(category) {
            case 9 -> "Straight Flush";
            case 8 -> "Four of a Kind";
            case 7 -> "Full House";
            case 6 -> "Flush";
            case 5 -> "Straight";
            case 4 -> "Three of a Kind";
            case 3 -> "Two Pair";
            case 2 -> "One Pair";
            case 1 -> "High Card";
            default -> "Unknown";
        };
        String cards = bestFive.stream().map(Object::toString).collect(Collectors.joining(" "));
        return cat + " (" + cards + ")";
    }
}

/* ===== PokerGame ===== */
class PokerGame {
    GameConfig cfg;
    List<Player> players = new ArrayList<>();
    Deck deck;
    List<Card> board = new ArrayList<>();
    int dealerIndex = 0;
    int pot = 0;
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();

    PokerGame(GameConfig cfg) { this.cfg = cfg; }

    void start() {
        setupPlayers();
        System.out.println("\nStarting Texas Hold'em. Type 'q' anytime to quit.\n");
        boolean continueGame = true;
        while (continueGame && activePlayersCount() > 1) {
            playHand();
            // remove busted players
            players.removeIf(p->p.chips <= 0 && !p.isHuman); // auto remove busted AI
            // if human busted, end
            Player human = getHuman();
            if (human == null || human.chips <= 0) {
                System.out.println("\nYou're out of chips. Game over.");
                break;
            }
            System.out.print("\nPlay another hand? (y/n): ");
            String ans = sc.nextLine().trim().toLowerCase();
            if (ans.equals("q") || ans.equals("n")) continueGame = false;
            else {
                // advance dealer
                dealerIndex = (dealerIndex + 1) % players.size();
            }
        }
        System.out.println("Thanks for playing!");
    }

    void setupPlayers() {
        System.out.println("Welcome to Console Poker!");
        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = "You";
        Player human = new Player(name, true, cfg.startingChips);
        players.add(human);
        for (int i=1; i<=cfg.numAIs; i++) {
            players.add(new Player("AI_"+i, false, cfg.startingChips));
        }
        // cap players
        if (players.size() > cfg.maxPlayers) players = players.subList(0, cfg.maxPlayers);
        System.out.println("Players:");
        for (Player p : players) System.out.println(" - " + p);
    }

    int activePlayersCount() {
        int c=0;
        for (Player p: players) if (p.chips > 0) c++;
        return c;
    }
    Player getHuman() {
        for (Player p: players) if (p.isHuman) return p;
        return null;
    }

    void playHand() {
        // reset
        deck = new Deck(); deck.shuffle();
        board.clear();
        pot = 0;
        for (Player p : players) p.resetForRound();
        // remove players with 0 chips from sitting out
        players = players.stream().filter(p->p.chips > 0).collect(Collectors.toList());
        if (players.size()<2) { System.out.println("Not enough players to continue."); return; }

        // post blinds
        int sbIndex = (dealerIndex + 1) % players.size();
        int bbIndex = (dealerIndex + 2) % players.size();
        Player sb = players.get(sbIndex);
        Player bb = players.get(bbIndex);
        int sbAmt = Math.min(cfg.smallBlind, sb.chips);
        int bbAmt = Math.min(cfg.bigBlind, bb.chips);
        sb.chips -= sbAmt; sb.currentBet = sbAmt; pot += sbAmt;
        bb.chips -= bbAmt; bb.currentBet = bbAmt; pot += bbAmt;
        System.out.println("\n--- New hand ---");
        System.out.println("Dealer: " + players.get(dealerIndex).name + " | Small blind: " + sb.name + " ("+sbAmt+") | Big blind: " + bb.name + " ("+bbAmt+")");

        // deal hole cards
        for (int i=0;i<2;i++) {
            for (Player p : players) {
                p.hole.add(deck.deal());
            }
        }

        // show human hole for convenience
        Player human = getHuman();
        System.out.println("\nYour cards: " + human.hole.get(0) + " " + human.hole.get(1));

        // betting rounds: pre-flop, flop, turn, river
        int toCall = bb.currentBet; // initial highest bet
        // Pre-flop betting starting from player after big blind
        runBettingRound((bbIndex+1)%players.size());

        if (moreThanOneActive()) {
            // Flop
            burn(); board.add(deck.deal()); board.add(deck.deal()); board.add(deck.deal());
            System.out.println("\n*** FLOP: " + boardToString());
            resetCurrentBets();
            runBettingRound((dealerIndex+1)%players.size());
        }
        if (moreThanOneActive()) {
            // Turn
            burn(); board.add(deck.deal());
            System.out.println("\n*** TURN: " + boardToString());
            resetCurrentBets();
            runBettingRound((dealerIndex+1)%players.size());
        }
        if (moreThanOneActive()) {
            // River
            burn(); board.add(deck.deal());
            System.out.println("\n*** RIVER: " + boardToString());
            resetCurrentBets();
            runBettingRound((dealerIndex+1)%players.size());
        }

        // Showdown / award pot
        List<Player> contenders = players.stream().filter(p->!p.folded).collect(Collectors.toList());
        if (contenders.size() == 1) {
            Player winner = contenders.get(0);
            winner.chips += pot;
            System.out.println("\nAll others folded. " + winner.name + " wins the pot of " + pot + " chips.");
        } else {
            // evaluate hands
            System.out.println("\n--- Showdown ---");
            long bestScore = Long.MIN_VALUE;
            List<Player> winners = new ArrayList<>();
            for (Player p : contenders) {
                List<Card> seven = new ArrayList<>(board);
                seven.addAll(p.hole);
                HandValue hv = HandEvaluator.evaluate(seven);
                System.out.println(p.name + (p.isHuman ? " (you): " : ": ") + p.hole.get(0) + " " + p.hole.get(1) + " => " + hv.pretty());
                if (hv.score > bestScore) { bestScore = hv.score; winners.clear(); winners.add(p); }
                else if (hv.score == bestScore) winners.add(p);
            }
            int share = pot / winners.size();
            for (Player w : winners) w.chips += share;
            System.out.print("Winner(s): ");
            System.out.println(winners.stream().map(p->p.name).collect(Collectors.joining(", ")) + " split pot ("+share+" each).");
        }
        // show stacks
        System.out.println("\nStacks:");
        for (Player p : players) System.out.println(" - " + p);
    }

    boolean moreThanOneActive() {
        return players.stream().filter(p->!p.folded && p.chips>0).count() > 1;
    }

    void runBettingRound(int startingIndex) {
        // Simple betting loop: each player gets a chance until everyone has called / folded
        int numPlayers = players.size();
        int lastToAct = (startingIndex + numPlayers -1) % numPlayers;
        int idx = startingIndex;
        boolean bettingOngoing = true;
        int currentMaxBet = players.stream().mapToInt(p->p.currentBet).max().orElse(0);

        // If everyone has same currentBet and we've looped a full round, finish
        int checksInRow = 0;
        Set<Player> acted = new HashSet<>();
        int activeCount = (int)players.stream().filter(p->p.chips>0 && !p.folded).count();

        while (bettingOngoing) {
            Player p = players.get(idx);
            if (!p.folded && p.chips>0) {
                if (p.isHuman) {
                    humanAction(p, currentMaxBet);
                } else {
                    aiAction(p, currentMaxBet);
                }
                currentMaxBet = players.stream().mapToInt(pl->pl.currentBet).max().orElse(0);
            }
            // detect if betting round over: all players either folded or have currentBet == currentMaxBet or are all-in
            boolean allSettled = true;
            int activeStill = 0;
            for (Player pl : players) {
                if (pl.folded) continue;
                if (pl.chips == 0) continue; // all-in - considered settled
                activeStill++;
                if (pl.currentBet != currentMaxBet) { allSettled = false; break; }
            }
            if (allSettled) break;
            idx = (idx+1) % numPlayers;
            // if idx cycles enough, keep going until all settled
        }
        // collect bets to pot and reset currentBet
        for (Player p : players) {
            pot += p.currentBet;
            p.currentBet = 0;
        }
        System.out.println("Pot is now: " + pot);
    }

    void humanAction(Player p, int currentMaxBet) {
        if (p.folded || p.chips <= 0) return;
        System.out.println("\nYour turn. Board: " + boardToString());
        System.out.println("Your cards: " + p.hole.get(0) + " " + p.hole.get(1));
        System.out.println("Pot: " + pot + " | Your stack: " + p.chips + " | To call: " + (currentMaxBet - p.currentBet));
        boolean canCheck = currentMaxBet == p.currentBet;
        System.out.print("Choose action - ");
        if (canCheck) System.out.print("[c]heck ");
        else System.out.print("[c]all ");
        System.out.print("[r]aise [f]old: ");
        String in = sc.nextLine().trim().toLowerCase();
        if (in.equals("q")) { System.exit(0); }
        if (in.equals("f")) {
            p.folded = true;
            System.out.println(p.name + " folds.");
            return;
        }
        if (in.equals("r")) {
            System.out.print("Enter raise amount (min " + Math.max(cfg.bigBlind, currentMaxBet*2) + " or type 'all' to shove): ");
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("all")) {
                int amt = p.chips;
                p.currentBet += amt;
                p.chips -= amt;
                if (p.chips == 0) p.allIn = true;
                System.out.println(p.name + " goes all-in with " + amt);
                return;
            }
            try {
                int raiseAmt = Integer.parseInt(s);
                // interpret as total bet or as raise? Simpler: treat input as amount to put in additional to currentBet
                if (raiseAmt <= 0) { System.out.println("Invalid amount. Treating as call."); callHuman(p, currentMaxBet); return; }
                if (raiseAmt >= p.chips) {
                    int put = p.chips;
                    p.currentBet += put;
                    p.chips -= put;
                    if (p.chips == 0) p.allIn = true;
                    System.out.println(p.name + " goes all-in with " + put);
                    return;
                } else {
                    p.currentBet += raiseAmt;
                    p.chips -= raiseAmt;
                    System.out.println(p.name + " raises by " + raiseAmt + " (current bet: " + p.currentBet + ")");
                    return;
                }
            } catch (NumberFormatException ex) { System.out.println("Invalid input. Treating as call."); callHuman(p, currentMaxBet); return; }
        }
        // default to call or check
        callHuman(p, currentMaxBet);
    }

    void callHuman(Player p, int currentMaxBet) {
        int need = currentMaxBet - p.currentBet;
        if (need <= 0) {
            System.out.println(p.name + " checks.");
            return;
        }
        int put = Math.min(need, p.chips);
        p.currentBet += put;
        p.chips -= put;
        if (p.chips == 0) p.allIn = true;
        System.out.println(p.name + " calls " + put + (p.allIn ? " (all-in)" : ""));
    }

    void aiAction(Player p, int currentMaxBet) {
        if (p.folded || p.chips <= 0) return;
        // Basic AI heuristics: compute rough hand strength from hole + board
        double strength = estimateHandStrength(p);
        int needToCall = currentMaxBet - p.currentBet;
        boolean canCheck = needToCall <= 0;
        if (cfg.verboseAI) System.out.println(p.name + " strength=" + String.format("%.2f", strength));
        // Aggressive if strong, fold if weak and need to call, randomize a bit
        double r = rand.nextDouble();
        if (canCheck) {
            if (strength > 0.6 && r < 0.7 && p.chips > cfg.bigBlind) {
                // raise
                int raiseBy = Math.min(p.chips, cfg.bigBlind + rand.nextInt(cfg.bigBlind*3));
                p.currentBet += raiseBy; p.chips -= raiseBy;
                System.out.println(p.name + " raises " + raiseBy + ".");
            } else {
                System.out.println(p.name + " checks.");
            }
        } else {
            if (strength > 0.7 && r < 0.9) {
                // reraise or call
                int raiseBy = Math.min(p.chips - needToCall, Math.max(cfg.bigBlind, (int)(needToCall*2)));
                if (raiseBy > 0 && rand.nextDouble() < 0.6) {
                    // raise
                    int totalPut = needToCall + raiseBy;
                    if (totalPut >= p.chips) { totalPut = p.chips; p.allIn = true; }
                    p.currentBet += totalPut; p.chips -= totalPut;
                    System.out.println(p.name + " calls and raises to " + p.currentBet + ".");
                } else {
                    // call
                    int put = Math.min(needToCall, p.chips);
                    p.currentBet += put; p.chips -= put;
                    if (p.chips==0) p.allIn=true;
                    System.out.println(p.name + " calls " + put + ".");
                }
            } else if (strength > 0.35 && rand.nextDouble() < 0.8) {
                // call moderate
                int put = Math.min(needToCall, p.chips);
                p.currentBet += put; p.chips -= put;
                if (p.chips==0) p.allIn=true;
                System.out.println(p.name + " calls " + put + ".");
            } else {
                // fold if can't afford or unlikely
                if (needToCall >= p.chips) {
                    // maybe call all-in occasionally
                    if (r < 0.15) {
                        int put = p.chips; p.currentBet += put; p.chips = 0; p.allIn = true;
                        System.out.println(p.name + " calls all-in " + put + ".");
                    } else {
                        p.folded = true; System.out.println(p.name + " folds.");
                    }
                } else {
                    p.folded = true; System.out.println(p.name + " folds.");
                }
            }
        }
    }

    // very rough strength estimator: evaluate best 5-card hand using available cards (if <5, approximate)
    double estimateHandStrength(Player p) {
        List<Card> available = new ArrayList<>(board);
        available.addAll(p.hole);
        if (available.size() < 2) return 0.5;
        if (available.size() < 5) {
            // quick heuristic: pair in hole, high cards, suited
            int a = p.hole.get(0).rank, b = p.hole.get(1).rank;
            double score = 0;
            if (a==b) score += 0.5;
            if (a>=11) score += 0.2;
            if (b>=11) score += 0.2;
            if (p.hole.get(0).suit == p.hole.get(1).suit) score += 0.15;
            return Math.min(1.0, score);
        } else {
            HandValue hv = HandEvaluator.evaluate(available);
            // map category to approx strength
            int category = (int)(hv.score >> 40);
            double base = switch(category) {
                case 9 -> 0.99;
                case 8 -> 0.95;
                case 7 -> 0.9;
                case 6 -> 0.85;
                case 5 -> 0.75;
                case 4 -> 0.6;
                case 3 -> 0.5;
                case 2 -> 0.4;
                case 1 -> 0.2;
                default -> 0.5;
            };
            // tweak by high card inside five
            int top = (int)((hv.score >> 32) & 0xFF);
            base += (top - 2) / 200.0;
            return Math.min(0.999, base);
        }
    }

    void resetCurrentBets() {
        for (Player p : players) p.currentBet = 0;
    }

    void burn() { // burn card for completeness
        deck.deal();
    }

    String boardToString() {
        if (board.isEmpty()) return "(empty)";
        return board.stream().map(Object::toString).collect(Collectors.joining(" "));
    }
}
