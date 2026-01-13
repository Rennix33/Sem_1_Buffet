import java.util.*;

/**
 * ClashRoyaleMini.java
 *
 * A simplified, console-based Clash Royale-like demo.
 * Single-file for quick compilation: javac ClashRoyaleMini.java && java ClashRoyaleMini
 *
 * Game concepts included:
 * - Cards (TroopCard, SpellCard)
 * - Elixir system (gain per turn)
 * - Towers with HP
 * - Simple troop movement and auto-attack resolution
 * - Turn-based console input (two players: Human vs AI)
 *
 * This is intentionally simplified for teaching / extension.
 */

public class starter {
    public static void main(String[] args) {
        Game game = new Game();
        game.setup();
        game.run();
    }
}

/* ---------- Core game classes ---------- */

class Game {
    final int MAX_TURNS = 200;
    Player p1, p2;
    Arena arena;
    Scanner scanner = new Scanner(System.in);

    void setup() {
        // create players
        p1 = new Player("You", false);      // human
        p2 = new Player("CPU", true);       // AI
        // create arena
        arena = new Arena(p1, p2);
        // build simple decks
        p1.buildStarterDeck();
        p2.buildStarterDeck();
        System.out.println("=== Welcome to ClashRoyaleMini ===");
        System.out.println("Simple demo: each turn you gain 1 elixir (max 10). Play cards by index.");
        System.out.println("First to destroy opponent's King Tower wins.");
    }

    void run() {
        int turn = 1;
        while (turn <= MAX_TURNS) {
            System.out.println("\n--- Turn " + turn + " ---");
            // both players gain elixir
            p1.gainElixir();
            p2.gainElixir();
            System.out.println(p1.status());
            System.out.println(p2.status());

            // player 1 (human) action
            if (!p1.isAI) {
                playerAction(p1);
            } else {
                aiAction(p1);
            }

            // player 2 (CPU)
            if (!p2.isAI) {
                playerAction(p2);
            } else {
                aiAction(p2);
            }

            // advance arena: troops move and fight
            arena.resolveTurn();

            // check win condition
            if (p1.kingTower.isDestroyed() || p2.kingTower.isDestroyed()) break;

            turn++;
        }

        // result
        if (p1.kingTower.isDestroyed() && p2.kingTower.isDestroyed()) {
            System.out.println("Draw: both King Towers destroyed!");
        } else if (p1.kingTower.isDestroyed()) {
            System.out.println("You lose! Your King Tower was destroyed.");
        } else if (p2.kingTower.isDestroyed()) {
            System.out.println("You win! Enemy King Tower destroyed.");
        } else {
            System.out.println("Max turns reached. Draw.");
        }
    }

    void playerAction(Player player) {
        System.out.println("\n" + player.name + " — choose a card to play (or 'pass'):");
        player.printHand();
        System.out.print("> ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("pass") || input.equals("")) {
            System.out.println(player.name + " passed.");
            return;
        }
        try {
            int idx = Integer.parseInt(input);
            if (idx < 0 || idx >= player.hand.size()) {
                System.out.println("Invalid index. Passing.");
                return;
            }
            Card c = player.hand.get(idx);
            if (player.elixir < c.cost) {
                System.out.println("Not enough elixir. Passing.");
                return;
            }
            // deploy: for troops, place on player's side. Spells target opponent tower.
            player.elixir -= c.cost;
            player.hand.remove(idx);
            Card played = c.copy(); // ensure unique instance
            if (played instanceof TroopCard) {
                Troop troop = new Troop((TroopCard)played, player);
                arena.spawnTroop(troop);
                System.out.println(player.name + " deployed troop: " + troop.card.name);
            } else if (played instanceof SpellCard) {
                SpellCard spell = (SpellCard)played;
                System.out.println(player.name + " cast spell: " + spell.name);
                spell.apply(player, player.opponent(), arena);
            }
            // draw a card
            player.drawFromDeck();
        } catch (NumberFormatException ex) {
            System.out.println("Bad input. Passing.");
        }
    }

    void aiAction(Player ai) {
        // Very simple AI: play the first playable card, otherwise pass
        boolean played = false;
        for (int i = 0; i < ai.hand.size(); i++) {
            Card c = ai.hand.get(i);
            if (c.cost <= ai.elixir) {
                ai.elixir -= c.cost;
                Card playedCard = c.copy();
                ai.hand.remove(i);
                if (playedCard instanceof TroopCard) {
                    Troop troop = new Troop((TroopCard)playedCard, ai);
                    arena.spawnTroop(troop);
                    System.out.println(ai.name + " deployed troop: " + troop.card.name);
                } else if (playedCard instanceof SpellCard) {
                    SpellCard spell = (SpellCard)playedCard;
                    System.out.println(ai.name + " cast spell: " + spell.name);
                    spell.apply(ai, ai.opponent(), arena);
                }
                ai.drawFromDeck();
                played = true;
                break;
            }
        }
        if (!played) {
            System.out.println(ai.name + " passed.");
        }
    }
}

class Arena {
    Player p1, p2;
    List<Troop> troops = new ArrayList<>();
    // simple linear lane model: positions 0..100; p1's king tower at 0, p2's at 100.
    int width = 101;

    Arena(Player a, Player b) {
        this.p1 = a; this.p2 = b;
        a.setOpponent(b); b.setOpponent(a);
        // initial towers
        a.kingTower = new Tower(a.name + " King Tower", 1000, 0);
        b.kingTower = new Tower(b.name + " King Tower", 1000, 100);
        a.leftTower = new Tower(a.name + " Left Tower", 600, 10);
        a.rightTower = new Tower(a.name + " Right Tower", 600, 20);
        b.leftTower = new Tower(b.name + " Left Tower", 600, 90);
        b.rightTower = new Tower(b.name + " Right Tower", 600, 80);
    }

    void spawnTroop(Troop t) {
        // spawn positions: p1 troops spawn at 10, p2 troops at 90
        t.position = t.owner == p1 ? 10 : 90;
        // direction: p1 -> +, p2 -> -
        t.direction = t.owner == p1 ? +1 : -1;
        troops.add(t);
    }

    void resolveTurn() {
        // troops move
        Iterator<Troop> it = troops.iterator();
        while (it.hasNext()) {
            Troop t = it.next();
            if (t.card.speed > 0) {
                t.position += t.direction * t.card.speed; // speed is int steps per turn
            }
            // clamp
            if (t.position < 0) t.position = 0;
            if (t.position > 100) t.position = 100;
        }

        // troop vs troop combat: nearest opposite troop adjacent (pos difference <= 1)
        for (int i = 0; i < troops.size(); i++) {
            Troop a = troops.get(i);
            for (int j = i+1; j < troops.size(); j++) {
                Troop b = troops.get(j);
                if (a.owner != b.owner && Math.abs(a.position - b.position) <= 1) {
                    // they attack each other
                    a.hp -= b.card.damage;
                    b.hp -= a.card.damage;
                }
            }
        }

        // troops attack towers if within range (position equals tower pos or close)
        for (Troop t : new ArrayList<>(troops)) {
            Player enemy = t.owner.opponent();
            Tower target = null;
            // determine closest tower from enemy side (king tower prioritized if reached)
            // simple rule: if reached >= target pos then attack that tower
            if (t.direction > 0) {
                // moving right: check enemy left(90), right(80), king(100)
                if (t.position >= enemy.leftTower.pos) target = enemy.leftTower;
                if (t.position >= enemy.rightTower.pos) target = enemy.rightTower;
                if (t.position >= enemy.kingTower.pos) target = enemy.kingTower;
            } else {
                // moving left: check enemy right(80), left(90), king(0)
                if (t.position <= enemy.rightTower.pos) target = enemy.rightTower;
                if (t.position <= enemy.leftTower.pos) target = enemy.leftTower;
                if (t.position <= enemy.kingTower.pos) target = enemy.kingTower;
            }
            if (target != null && !target.isDestroyed()) {
                target.hp -= t.card.damage;
                // optional: troops take return damage from tower (defense)
                t.hp -= target.getReturnDamage();
            }
        }

        // remove dead troops; show events
        Iterator<Troop> iter = troops.iterator();
        while (iter.hasNext()) {
            Troop t = iter.next();
            if (t.hp <= 0) {
                System.out.println(t.owner.name + "'s " + t.card.name + " died at pos " + t.position);
                iter.remove();
            }
        }

        // show tower dmg
        for (Player p : new Player[]{p1, p2}) {
            for (Tower tw : new Tower[]{p.leftTower, p.rightTower, p.kingTower}) {
                if (tw.hp <= 0 && !tw.destroyedFlag) {
                    System.out.println(tw.name + " was destroyed!");
                    tw.destroyedFlag = true;
                }
            }
        }

        // summary: troops count
        System.out.println("Troops on field: " + troops.size());
    }
}

/* ---------- Players, Decks, Hands ---------- */

class Player {
    String name;
    boolean isAI;
    int elixir = 3;
    final int MAX_ELIXIR = 10;
    List<Card> deck = new ArrayList<>();
    List<Card> hand = new ArrayList<>();
    Tower leftTower, rightTower, kingTower;
    Player opponent;

    Player(String name, boolean isAI) {
        this.name = name; this.isAI = isAI;
    }

    void setOpponent(Player o) { this.opponent = o; }
    Player opponent() { return opponent; }

    void gainElixir() {
        elixir = Math.min(MAX_ELIXIR, elixir + 1);
    }

    void buildStarterDeck() {
        // Simple deck: a few troops and one spell
        deck.clear();
        deck.add(new TroopCard("Goblin", 2, 1, 100));   // name, cost, speed, damage/hp combined: we'll treat damage from 'damage' field
        deck.add(new TroopCard("Goblin", 2, 1, 100));
        deck.add(new TroopCard("Knight", 3, 1, 250));
        deck.add(new TroopCard("Giant", 5, 1, 500)); // heavy hp & damage
        deck.add(new TroopCard("Archer", 3, 2, 80));
        deck.add(new SpellCard("Fireball", 4, 150)); // deals 150 to tower / troops in area
        // duplicate to create deck size ~10
        deck.add(new TroopCard("Archer", 3, 2, 80));
        deck.add(new TroopCard("Knight", 3, 1, 250));
        deck.add(new TroopCard("Goblin", 2, 1, 100));
        deck.add(new SpellCard("Fireball", 4, 150));
        Collections.shuffle(deck);
        // draw starting hand 4
        hand.clear();
        for (int i = 0; i < 4; i++) drawFromDeck();
    }

    void drawFromDeck() {
        if (deck.isEmpty()) return;
        hand.add(deck.remove(0));
    }

    void printHand() {
        System.out.print("Hand: ");
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            System.out.print("[" + i + ":" + c.name + " cost:" + c.cost + "] ");
        }
        System.out.println();
    }

    String status() {
        return String.format("%s - Elixir: %d | King HP: %d | Left: %d Right: %d",
                name, elixir,
                kingTower == null ? 0 : kingTower.hp,
                leftTower == null ? 0 : leftTower.hp,
                rightTower == null ? 0 : rightTower.hp);
    }
}

/* ---------- Towers ---------- */

class Tower {
    String name;
    int hp;
    int pos; // position on lane
    boolean destroyedFlag = false;

    Tower(String name, int hp, int pos) {
        this.name = name; this.hp = hp; this.pos = pos;
    }

    boolean isDestroyed() { return hp <= 0; }

    // when troops attack tower, tower "returns" some damage to troop
    int getReturnDamage() {
        return 10; // simple fixed melee return
    }
}

/* ---------- Cards ---------- */

abstract class Card {
    String name;
    int cost;

    Card(String name, int cost) { this.name = name; this.cost = cost; }
    abstract Card copy();
}

class TroopCard extends Card {
    int speed;   // steps per turn
    int damage;  // damage per attack
    int hp;      // troop hp

    TroopCard(String name, int cost, int speed, int damageAndHp) {
        super(name, cost);
        this.speed = speed;
        // For simplicity we split the damage/hp parameter:
        this.damage = damageAndHp / 2;
        this.hp = damageAndHp;
    }

    @Override
    Card copy() {
        TroopCard t = new TroopCard(this.name, this.cost, this.speed, this.hp * 1); // damage/hp param reused
        t.damage = this.damage;
        t.hp = this.hp;
        return t;
    }
}

class SpellCard extends Card {
    int power; // area/tower damage

    SpellCard(String name, int cost, int power) {
        super(name, cost);
        this.power = power;
    }

    @Override
    Card copy() {
        return new SpellCard(this.name, this.cost, this.power);
    }

    void apply(Player caster, Player targetPlayer, Arena arena) {
        // Simple: damage enemy king tower and any troops near it (pos >= 85 if caster is p1, <=15 if caster is p2)
        if (caster == arena.p1) {
            System.out.println("Fireball hits enemy King Tower for " + power + " damage.");
            targetPlayer.kingTower.hp -= power;
            // damage nearby troops
            for (Troop t : new ArrayList<>(arena.troops)) {
                if (t.owner == targetPlayer && t.position >= 80) {
                    t.hp -= power/2;
                }
            }
        } else {
            System.out.println("Fireball hits enemy King Tower for " + power + " damage.");
            targetPlayer.kingTower.hp -= power;
            for (Troop t : new ArrayList<>(arena.troops)) {
                if (t.owner == targetPlayer && t.position <= 20) {
                    t.hp -= power/2;
                }
            }
        }
    }
}

/* ---------- Troop Instances ---------- */

class Troop {
    TroopCard card;
    Player owner;
    int hp;
    int position;
    int direction; // +1 or -1
    int dir; // alias

    Troop(TroopCard card, Player owner) {
        this.card = card;
        this.owner = owner;
        this.hp = card.hp;
    }
}
