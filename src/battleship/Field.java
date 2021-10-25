package battleship;

import java.util.Random;

public class Field {
    static char missed = '•';
    static char hitted = 'x';
    static char[][] field;
    static boolean[][] busyPlaces;
    private static boolean[][] notAvailableCells;
    static boolean[][] shotedPlaces;
    static int M;
    static int N;
    static Ship[] fleet;
    private static int shipCounter;
    private static final int ATTEMPTS_NUMBER = 100;
    private static int numberOfBusyCells = 0;
    private static int numberOfSuccessfulHits = 0;
    private static int numberOfHits = 0;


    static public void getGameParameters(int m, int n, int n5, int n4, int n3, int n2, int n1) {
        M = m;
        N = n;
        field = new char[M][N];
        busyPlaces = new boolean[M][N];
        shotedPlaces = new boolean[M][N];
        notAvailableCells = new boolean[M][N];
        shipCounter = n5 + n4 + n3 + n2 + n1;
        fleet = new Ship[shipCounter];
        addShip(n5, Carrier.class);
        addShip(n4, Battleship.class);
        addShip(n3, Cruiser.class);
        addShip(n2, Destroyer.class);
        addShip(n1, Submarine.class);
        numberOfBusyCells = 0;
        for (Ship s : fleet) {
            numberOfBusyCells += s.getCellsNumber();
        }
    }

    private static void addShip(int number, Class<?> cls) {
        try {
            for (int i = 0; i < number; i++) {
                int n = fleet.length - (shipCounter--);
                fleet[n] = (Ship) cls.getConstructor((Class[]) null).newInstance();
            }
        } catch (Exception ignored) {
        }
    }

    static public boolean randomStandShips() {
        Random random = new Random();
        int m, n, counter = 0;
        boolean isGorizontal;
        for (Ship ship : fleet) {
            boolean isOkey = false;
            while (!isOkey && counter++ < ATTEMPTS_NUMBER) {
                int maxM = M, maxN = N;
                isGorizontal = random.nextBoolean();
                if (isGorizontal) maxN -= ship.getCellsNumber() - 1;
                else maxM -= ship.getCellsNumber() - 1;
                if (maxN < 0 || maxM < 0) continue;
                int innerCounter = 0;
                do {
                    m = random.nextInt(maxM);
                    n = random.nextInt(maxN);
                } while (notAvailableCells[m][n] && ++innerCounter < ATTEMPTS_NUMBER);
                /* Checks if the position is possible. */
                isOkey = true;
                if (isGorizontal) {
                    for (int i = n; i < n + ship.getCellsNumber(); i++) {
                        if (notAvailableCells[m][i]) {
                            isOkey = false;
                            break;
                        }
                    }
                    if (isOkey) {
                        for (int i = n; i < n + ship.getCellsNumber(); i++) {
                            busyPlaces[m][i] = true;

                            tryingSurroundingCells(m, i);
                        }
                    }
                } else {
                    for (int i = m; i < m + ship.getCellsNumber(); i++) {
                        if (notAvailableCells[i][n]) {
                            isOkey = false;
                            break;
                        }
                    }
                    if (isOkey) {
                        for (int i = m; i < m + ship.getCellsNumber(); i++) {
                            busyPlaces[i][n] = true;
                            tryingSurroundingCells(i, n);
                        }

                    }
                }

            }
            if (counter >= ATTEMPTS_NUMBER) {
                return false;
            }
        }
        return true;
    }

    private static void tryingSurroundingCells(int m, int i) {
        tryMakeNotAvoidable(m, i);
        tryMakeNotAvoidable(m - 1, i - 1);
        tryMakeNotAvoidable(m - 1, i);
        tryMakeNotAvoidable(m - 1, i + 1);
        tryMakeNotAvoidable(m, i + 1);
        tryMakeNotAvoidable(m + 1, i + 1);
        tryMakeNotAvoidable(m + 1, i);
        tryMakeNotAvoidable(m + 1, i - 1);
        tryMakeNotAvoidable(m, i - 1);
    }

    private static void tryMakeNotAvoidable(int tryingM, int tryingN) {
        if (tryingN > -1 && tryingM > -1 && tryingN < N && tryingM < M) {
            notAvailableCells[tryingM][tryingN] = true;
        }
    }

    public static void drowField() {
        for (int i = 0; i < 2 * field[0].length; i++) {
            System.out.print('–');
        }
        System.out.println('–');
        for (int j = 0; j < field.length; j++) {
            System.out.print('|');
            for (int i = 0; i < field[0].length; i++) {
                if (shotedPlaces[j][i]) {
                    if (busyPlaces[j][i]) System.out.print(hitted);
                    else System.out.print(missed);
                } else {
                    System.out.print(' ');
                }
                System.out.print('|');
            }
            System.out.println();
            for (int i = 0; i < field[0].length; i++) {
                System.out.print("––");
            }
            System.out.println('–');
        }
    }

    public static void Shot(int row, int column) {
        int m = row - 1;
        int n = column - 1;
        if (n > -1 && n < N && m > -1 && m < M && !shotedPlaces[m][n]) {
            shotedPlaces[m][n] = true;
            ++numberOfHits;
            if (busyPlaces[m][n]) {
                ++numberOfSuccessfulHits;
                System.out.println("hit");
            } else {
                System.out.println("miss");
            }
        } else {
            System.out.println("Incorrect shot!");
        }
    }

    public static boolean isGameOver() {
        if (numberOfSuccessfulHits == numberOfBusyCells) {
            System.out.printf("Game is over\nTotal number of the shots: %d\nBest number: %d\n", numberOfHits, numberOfBusyCells);
            return true;
        }
        return false;
    }
}
