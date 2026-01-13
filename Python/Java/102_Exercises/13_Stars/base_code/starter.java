/*
 *	Author:
 *  Date:
 *	Collaborator(s): 
*/

import java.util.Scanner;

class starter {
    public static void main(String args[]) {
        String star = "*";

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter an integer:");
        int num = sc.nextInt(); 

        int count = 1; 
        while (count <= num) {
            System.out.println(star);
            star = star + "*"; 
            count++;
        }

        sc.close();
    }
}

