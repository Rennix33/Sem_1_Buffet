class starter {
    public static void main(String[] args) {
        // ANSI escape code for red background
        String red = "\u001B[41m";
        String reset = "\u001B[0m";

        // Print a block of red
        for (int i = 0; i < 30; i++) {
            System.out.println(red + " ".repeat(80) + reset);
        }
    }
}
