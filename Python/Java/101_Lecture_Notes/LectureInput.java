/*
    Lecture note example - Input!!
*/
 import java.util.Scanner;
class LectureInput{
    public static void main(String args[]) {
        // Your Code Goes here!
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Rennon's dinner. What would you like to order?");
        System.out.println("1. Chiken Teriyaki - $11.99");
        System.out.println("2. Cheese Burger - $7.99");
        System.out.println("3. California roll - $8.99");
        
        System.out.println("Can I get a name for the order?");
        String name = sc.nextLine();
        System.out.println("How many Chiken Teriyakis do you want?");
        int item1 = sc.nextInt();
        System.out.println("How many Cheese Burgers do you want?");
        int item2 = sc.nextInt();
        System.out.println("How many California rolls do you want?");
        int item3 = sc.nextInt();
        double price1 = item1 * 11.99;
        double price2 = item2 * 7.99;
        double price3 = item3 * 8.99;
        
        System.out.println("How much would you like to tip?");
        double tip = sc.nextDouble();
        double total = price1 + price2 + price3 + tip;
        System.out.println();
        System.out.println("---------------------------");
        System.out.println(name + "s Receipt");
        System.out.println(item1 + " X Chicken Teriyaki = $" + price1);
        System.out.println(item2 + " X Cheese Burgers = $" + price2);
        System.out.println(item3 + " X California roll = $" + price3);
        System.out.println("---------------------------");
        System.out.println("The grand total is - $" + total);
	}
}
