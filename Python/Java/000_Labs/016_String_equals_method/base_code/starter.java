/*
 *	Author:  
 *  Date: 
*/

import java.util.Scanner;
import java.util.Random;

class starter {
	public static void main(String args[]) {
		// the string "I love to learn coding remotely." will appear in
		// the command window when you compile and run this program.
		//System.out.print("I love to learn coding remotely."); 
		Scanner sc = new Scanner(System.in);
		System.out.println("Would you like to be a Wizard, Warrior, or Rogue. ");
		String choose = sc.nextLine(); 
		if (choose.equalsIgnoreCase("Wizard")) {
			System.out.print("You've chosen Wizard! Master you Spells!");
		}
		else if (choose.equalsIgnoreCase("Warrior")) {
			System.out.print("Go fight people Warrior");
		}
		else if (choose.equalsIgnoreCase("Rogue")) {
			System.out.print("ROGUE!!!!!");
		}
		else {
			System.out.print("Thats not a valid Choice. Choose Wizard, Warrior, or Rogue.");
		}
		
		
		
		
		
	}
}
