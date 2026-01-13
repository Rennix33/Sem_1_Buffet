/*
 *	Author:
 *  Date:
 * 	Collaborator(s): 
*/

import java.util.Scanner;
import java.util.Random;

class starter {
    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();

    int money = 100;
    String play = "y";

    System.out.println("Slot Machine Rules:");
    System.out.println("1. Each player starts with $100.");
    System.out.println("2. Input a wager less than your total amount of money.");
    System.out.println("3. The slot machine will roll 3 numbers from 1 to 10.");
    System.out.println("   a. If two numbers match, you double your money.");
    System.out.println("   b. If three numbers match, you triple your money.");
    System.out.println("   c. If none match, you lose your money.");
    System.out.println("--------------------------------------------------");

    while (play.equalsIgnoreCase("y")) {
    if (money <= 0) {
    System.out.println("You're out of money! Game over.");
    break;
    }

    System.out.print("Would you like to play the slots? (Y/N): ");
    play = sc.next();

    if (!play.equalsIgnoreCase("y")) {
    break;
    }

    System.out.println("You have $" + money + ". How much would you like to wager?");
    int wager = sc.nextInt();

    while (true) {
    if (wager <= 0) {
    System.out.println("Invalid wager. Enter a number greater than 0:");
    wager = sc.nextInt();
    } else {
    if (wager > money) {
    System.out.println("You don't have that much! Enter a smaller number:");
    wager = sc.nextInt();
    } else {
    break;
    }
    }
    }

    System.out.println("Great! Let's play!!!");

    int a = rand.nextInt(10) + 1;
    int b = rand.nextInt(10) + 1;
    int c = rand.nextInt(10) + 1;

    System.out.println("_______________________");
    System.out.println(" | " + a + " | " + b + " | " + c + " | ");
    System.out.println("_______________________");

    if (a == b) {
    if (b == c) {
    money = money + wager * 2;
    System.out.println("JACKPOT!!! All three match — you tripled your wager!");
    } else {
    money = money + wager;
    System.out.println("You won! Two numbers match!");
    }
    } else {
    if (b == c) {
    money = money + wager;
    System.out.println("You won! Two numbers match!");
    } else {
    if (a == c) {
    money = money + wager;
    System.out.println("You won! Two numbers match!");
    } else {
    money = money - wager;
    System.out.println("No matches. You lost your wager!");
    }
    }
    }

    System.out.println("You now have $" + money + ".");
    System.out.println("--------------------------------------------------");
    }

    System.out.println("Thanks for playing! You ended with $" + money + ".");
    sc.close();
    }
}
