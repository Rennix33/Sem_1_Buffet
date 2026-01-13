import java.awt.AWTException;
import java.awt.Robot;

class starter {

    	public static void main(String args[]) {
        try {
            // Create a Robot object
            Robot robot = new Robot();

            // Define the target coordinates
            int targetX = 500; // X-coordinate
            int targetY = 300; // Y-coordinate

            // Move the mouse to the specified coordinates
            robot.mouseMove(targetX, targetY);

            System.out.println("Mouse moved to (" + targetX + ", " + targetY + ")");

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}