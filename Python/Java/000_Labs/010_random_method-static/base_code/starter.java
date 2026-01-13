/*
 *	Author:  
 *  Date: 
*/

import java.util.Random;

class starter {
    public static void main(String args[]) {
        Random rand = new Random();

        int a = rand.nextInt(10);
        System.out.println("a) " + a);

        int b = rand.nextInt(100) + 1;
        System.out.println("b) " + b);

        double c = 2.5 + rand.nextDouble();
        System.out.println("c) " + c);

        double d = 14 + (589 - 14) * rand.nextDouble();
        System.out.println("d) " + d);
    }
}
