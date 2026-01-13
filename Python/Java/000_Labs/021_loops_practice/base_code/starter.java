/*
 *	Author:  
 *  Date: 
*/

import java.util.Scanner;

class starter {
	public static void main(String args[]) {
		// Your code goes below here
		
		
		Scanner sc = new Scanner(System.in);
        System.out.println("Guess the number from 1-1000");

        int numberToGuess = (int)(Math.random() * 1000) + 1;
        int guess = 0; 
        
        
        while (guess != numberToGuess) {
            System.out.print("Enter your guess: ");
            guess = sc.nextInt();
            
            
            if(guess < numberToGuess){
            	System.out.println("Too low");
            	
            	}else if(guess > numberToGuess){
            		System.out.println("Too high");
            		}else {
            			System.out.print("Nice! You got it!");
            		}
            	}
            }
}
		
		
		
		
		
		



		
	

