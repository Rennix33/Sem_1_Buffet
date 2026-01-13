/*
 *	Author:  
 *  Date: 
*/

import java.util.Scanner;
import java.util.Random;

class starter {
    public static void main(String args[]) {
        Random rand = new Random();

       
        int number = rand.nextInt(1000) + 1;
        System.out.println(number);
        
        Scanner sc = new Scanner(System.in);
        int guess = sc.nextInt();
        if (guess == number){ System.out.print("Correct");
        }
        
        if (guess != number){ System.out.print("Wrong");
        
        }
        
        
        
        
        
        
        
        
    }
}
