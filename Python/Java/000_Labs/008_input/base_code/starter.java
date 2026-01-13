/*
 *	Author:  
 *  Date: 
*/

import java.util.Scanner;

class starter {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);

	
		System.out.println("Please enter your first name");
		String firstName = sc.nextLine();
		
		System.out.println("Please enter your age");
		int age = sc.nextInt();
		sc.nextLine(); 
		
		System.out.println("Please enter your birth month");
		String month = sc.nextLine();
		
		System.out.println("Please enter your birth day");
		String day = sc.nextLine();
		
		System.out.println("Please enter your birth year");
		String year = sc.nextLine();
		
		System.out.println("How much is a buck fifty");
		String money = sc.nextLine();

		
		System.out.println("Your name is " + firstName + ", you are " + age + " years old, your birthday is on " + month + " " + day + ", " + year + ", and a buck fifty is " + money + ".");
	}
}
