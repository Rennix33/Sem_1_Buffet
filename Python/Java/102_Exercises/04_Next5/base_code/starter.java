/*
 *	Author:
 *  Date:
 *	Collaborator(s): 
*/
import java.util.Scanner;

class starter {
	public static void main(String args[]) {

Scanner hi = new Scanner(System.in);
System.out.print("Please enter a number: ");
double number = hi.nextInt();
String name = hi.nextLine();
System.out.println("Here are the next 5 numbers!");
System.out.print(number);
System.out.print(", ");
System.out.print(number + 1);
System.out.print(", ");
System.out.print(number + 2);
System.out.print(", ");
System.out.print(number + 3);
System.out.print(", ");
System.out.print(number + 4);
System.out.print(", ");
System.out.println(number + 5);
System.out.print("Here are the next 5 multiples of ");
System.out.print(number);
System.out.println("! ");
System.out.print(number);
System.out.print(", ");
System.out.print(number*2);
System.out.print(", ");
System.out.print(number*3);
System.out.print(", ");
System.out.print(number*4);
System.out.print(", ");
System.out.println(number*5);
System.out.print("here is ");
System.out.print(number);
System.out.println(" divided by 100!");
System.out.println(number/100);
System.out.print("Here is ");
System.out.print(number);
System.out.println(" divided by 10!");
System.out.print(number/10);



	}
}
