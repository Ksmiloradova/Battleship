package battleship;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(System.in);
            int min = -1, N = 1, M = 1, n5 = 0, n4 = 0, n3 = 0, n2 = 0, n1 = 0;
            try {
                tryToGetParametersFromCommandLine(args);

            } catch (Exception e) {
                System.out.print("");
                boolean isStandingSuccessful = false;
                if (getParametersFromConsole(in, min, N, M, n5, n4, n3, n2, n1, isStandingSuccessful)) return;
            }
            Field.drowField();
            while (!Field.isGameOver()) {
                System.out.print("Input the number of row for hit: ");
                int r = in.nextInt();
                System.out.print("Input the number of column for hit: ");
                int c = in.nextInt();
                Field.Shot(r, c);
                Field.drowField();
            }
            in.close();
        } catch (Exception ignored) {
            System.out.print("Something went wrong");
        }
    }

    /**
     * Method gets parameters from console.
     */
    private static boolean getParametersFromConsole(Scanner in, int min, int N, int M, int n5, int n4, int n3, int n2, int n1, boolean isStandingSuccessful) {
        while (min < 0 || M < 1 || N < 1 || !isStandingSuccessful) {
            System.out.println("Would you like to continue (y) or exit (n) ");
            if (Objects.equals(in.next(), "n")) return true;
            try {
                System.out.print("Input a number of rows: ");
                M = in.nextInt();
                System.out.print("Input a number of columns: ");
                N = in.nextInt();
                System.out.print("Input a number of Carriers: ");
                n5 = in.nextInt();
                System.out.print("Input a number of Battleships: ");
                n4 = in.nextInt();
                System.out.print("Input a number of Cruisers: ");
                n3 = in.nextInt();
                System.out.print("Input a number of Destroyer: ");
                n2 = in.nextInt();
                System.out.print("Input a number of Submarines: ");
                n1 = in.nextInt();
            } catch (Exception ignored) {
            }
            min = Math.min(Math.min(Math.min(Math.min(Math.min(Math.min(M, N),
                    n1), n2), n3), n4), n5);
            if (min >= 0 || M > 0 || N > 0) {
                Field.getGameParameters(M, N, n5, n4, n3, n2, n1);
                isStandingSuccessful = Field.randomStandShipsForUser();
                if (!isStandingSuccessful)
                    System.out.println("Computer is not able to " +
                            "arrange the Fleet in the Ocean.\n" +
                            "Please to re-enter these parameters or quit the game");
            }
        }
        return false;
    }

    /**
     * Method gets parameters from command line.
     */
    private static void tryToGetParametersFromCommandLine(String[] args) throws Exception {
        Field.getGameParameters(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                Integer.parseInt(args[2]), Integer.parseInt(args[3]),
                Integer.parseInt(args[4]), Integer.parseInt(args[5]),
                Integer.parseInt(args[6]));
        if (!Field.randomStandShipsForUser()) {
            System.out.println("Computer is not able to arrange the Fleet in the Ocean.\n" +
                    "Please to re-enter these parameters or quit the game");
            throw new Exception();
        }
    }

}
