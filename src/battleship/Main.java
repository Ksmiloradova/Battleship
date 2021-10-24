package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length == 7) {
            try {
                Field.getGameParameters(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]),
                        Integer.parseInt(args[5]), Integer.parseInt(args[6]));

            } catch (NumberFormatException e) {
                System.out.print("");
                Scanner in = new Scanner(System.in);
                int min = 0;
                while (min >= 0) {
                    System.out.print("Input a number of rows: ");
                    int M = in.nextInt();
                    System.out.print("Input a number of columns: ");
                    int N = in.nextInt();
                    System.out.print("Input a number of Carriers: ");
                    int n5 = in.nextInt();
                    System.out.print("Input a number of Battleships: ");
                    int n4 = in.nextInt();
                    System.out.print("Input a number of Cruisers: ");
                    int n3 = in.nextInt();
                    System.out.print("Input a number of Destroyer: ");
                    int n2 = in.nextInt();
                    System.out.print("Input a number of Submarines: ");
                    int n1 = in.nextInt();
                    min = Math.min(Math.min(Math.min(Math.min(Math.min(Math.min(M, N), n1), n2), n3), n4), n5);
                }
                in.close();
                if (min < 0) return;
            }
        }

    }
}
