/*
 *	Author:  
 *  Date: 
*/

import java.util.Scanner;

class starter {
	public static void main(String args[]) {
		// Your code goes below here
		
		
		Scanner sc = new Scanner(System.in);
		
		
		System.out.println("Please enter your name:");
		String name = sc.nextLine();
		System.out.println("How many times do you want your name to be printed");
		int num = sc.nextInt();
		
		int x = 0;
		while(true){
			if (x == num){
				break;
			}
			System.out.println(name);
			x++;
		}
		
		
		



		
	}
}
