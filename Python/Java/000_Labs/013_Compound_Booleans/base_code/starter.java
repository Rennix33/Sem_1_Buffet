/*
 *	Author:  
 *  Date: 
*/

import java.util.Scanner;

class starter {
	public static void main(String args[]) {
		// the string "I love to learn coding remotely." will appear in
		// the command window when you compile and run this program.
		//System.out.print("I love to learn coding remotely.");
		
        Scanner sc = new Scanner(System.in);

        System.out.println("Please give one integer: ");
        int French = sc.nextInt();

        System.out.println("Please give another integer: ");
        int frinch = sc.nextInt();

        System.out.println("Please give 1 last integer: ");
        int froonch = sc.nextInt();

        if (French > frinch) { 
            if (French > froonch) { 
               System.out.println("");
                System.out.print(French);
            }
        }

        if (frinch > French) { 
            if (frinch > froonch) { 
               System.out.println("");
                System.out.print(frinch);
            }
        }
        
        if (froonch > French) { 
            if (froonch > frinch) { 
               System.out.println("");
                System.out.print(froonch);
            }
        }
        
        
        
        
    }
}
