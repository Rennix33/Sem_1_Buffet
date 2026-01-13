/*
 *	Author:  
 *  Date: 
*/

import java.util.Scanner;
import java.util.Random;

class starter {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        System.out.println("What is your name?");
        String name = sc.nextLine();

        System.out.println("What is your title. Ex: Slayer of Dragons");
        String title = sc.nextLine();

        System.out.println("Would you like to be a Wizard, Warrior, or Rogue.");
        String choose = sc.nextLine();

        if (choose.equalsIgnoreCase("Wizard")) {
            System.out.print("You've chosen Wizard! Master your Spells!");
        }
        else if (choose.equalsIgnoreCase("Warrior")) {
            System.out.print("Go fight people Warrior");
        }
        else if (choose.equalsIgnoreCase("Rogue")) {
            System.out.print("ROGUE!!!!!");
        }
        else {
            System.out.print("That's not a valid Choice. Choose Wizard, Warrior, or Rogue.");
        }

        System.out.println("");
        System.out.println("You have 20 skill points to spend in the following: Strength, Dexterity, Intelligence, and Charisma. Spend them wisely.");
        System.out.println("");

        int totalPoints = 20;

        System.out.print("Strength (1-10): ");
        int strength = sc.nextInt();
        if (strength > 10) {
            System.out.println("That’s too high! Strength can’t be more than 10.");
        } else if (strength > totalPoints) {
            System.out.println("You don’t have enough points for that!");
        }
        totalPoints -= strength;
        System.out.println("You have " + totalPoints + " points left to spend.\n");

        System.out.print("Dexterity (1-10): ");
        int dexterity = sc.nextInt();
        if (dexterity > 10) {
            System.out.println("That’s too high! Dexterity can’t be more than 10.");
        } else if (dexterity > totalPoints) {
            System.out.println("You don’t have enough points for that!");
        }
        totalPoints -= dexterity;
        System.out.println("You have " + totalPoints + " points left to spend.\n");

        System.out.print("Intelligence (1-10): ");
        int intelligence = sc.nextInt();
        if (intelligence > 10) {
            System.out.println("That’s too high! Intelligence can’t be more than 10.");
        } else if (intelligence > totalPoints) {
            System.out.println("You don’t have enough points for that!");
        }
        totalPoints -= intelligence;
        System.out.println("You have " + totalPoints + " points left to spend.\n");

        System.out.print("Charisma (1-10): ");
        int charisma = sc.nextInt();
        if (charisma > 10) {
            System.out.println("That’s too high! Charisma can’t be more than 10.");
        } else if (charisma > totalPoints) {
            System.out.println("You don’t have enough points for that!");
        }
        totalPoints -= charisma;

        System.out.println("-----------------");
        System.out.println("You are " + name + ", the " + title + " of CVHS.");
        System.out.println("You're a " + choose + " with the following stats!");
        System.out.println("Strength - " + strength);
        System.out.println("Dexterity - " + dexterity);
        System.out.println("Intelligence - " + intelligence);
        System.out.println("Charisma - " + charisma);

        if (totalPoints > 0) {
            System.out.println("You still have " + totalPoints + " points left unspent!");
        }

        System.out.println("");
        System.out.print("Good luck on your quest " + name);
    }
}
