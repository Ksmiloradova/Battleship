package battleship;

import java.util.Random;

public class Field {
    /**
     * The representation of an Ocean cell for “fired-miss”.
     */
    private static final char MISSED = '•';
    /**
     * The representation of an Ocean cell for “fired-hit”.
     */
    private static final char HITTED = 'x';/**
     * The representation of an Ocean cell for “fired-sunk”.
     */
    private static final char SUNKED = '█';
    /**
     * The representation of an Ocean cell for “not-fired”.
     */
    private static final char NOT_FIRED = ' ';
    private static Ship[][] field;
    private static boolean[][] busyPlaces;
    private static boolean[][] notAvailableCells;
    private static boolean[][] shotedPlaces;
    private static int M;
    private static int N;
    private static Ship[] fleet;
    private static int shipCounter;
    private static final int ATTEMPTS_NUMBER = 100;
    private static int numberOfBusyCells = 0;
    private static int numberOfSuccessfulHits = 0;
    private static int numberOfHits = 0;

    /**
     * Method initialize user input game parametrs.
     */
    static public void getGameParameters(int m, int n, int n5, int n4, int n3, int n2, int n1) {
        M = m;
        N = n;
        field = new Ship[M][N];
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

    /**
     * Method adds one ship to array fleet.
     */
    private static void addShip(int number, Class<?> cls) {
        try {
            for (int i = 0; i < number; i++) {
                int n = fleet.length - (shipCounter--);
                fleet[n] = (Ship) cls.getConstructor((Class[]) null).newInstance();
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Method provides user opportunity to install randomly all fleet according to the rules.
     */
    static public boolean randomStandShipsForUser() {
        Random random = new Random();
        int counter = 0;
        boolean isGorizontal;
        for (Ship ship : fleet) {
            boolean isOkey = false;
            while (!isOkey && counter++ < ATTEMPTS_NUMBER) {
                isOkey = helpToStandShips(random, ship);
            }
            if (counter >= ATTEMPTS_NUMBER) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method randomly installs all fleet to the Ocean according to the rules.
     */
    private static boolean helpToStandShips(Random random, Ship ship) {
        boolean isGorizontal;
        int n;
        int m;
        int maxM = M, maxN = N;
        isGorizontal = random.nextBoolean();
        if (isGorizontal) maxN -= ship.getCellsNumber() - 1;
        else maxM -= ship.getCellsNumber() - 1;
        if (maxN < 0 || maxM < 0) return false;
        int innerCounter = 0;
        do {
            m = random.nextInt(maxM);
            n = random.nextInt(maxN);
        } while (notAvailableCells[m][n] && ++innerCounter < ATTEMPTS_NUMBER);
        /* Checks if the position is possible. */
        boolean isOkey = true;
        if (isGorizontal) {
            isOkey = standGorizontal(m, n, ship, isOkey);
        } else {
            isOkey = standVertical(m, n, ship, isOkey);
        }
        return isOkey;
    }

    private static boolean standVertical(int m, int n, Ship ship, boolean isOkey) {
        for (int i = m; i < m + ship.getCellsNumber(); i++) {
            if (notAvailableCells[i][n]) {
                isOkey = false;
                break;
            }
        }
        if (isOkey) {
            for (int i = m; i < m + ship.getCellsNumber(); i++) {
                busyPlaces[i][n] = true;
                field[i][n] = ship;
                tryingSurroundingCells(i, n);
            }

        }
        return isOkey;
    }

    private static boolean standGorizontal(int m, int n, Ship ship, boolean isOkey) {
        for (int i = n; i < n + ship.getCellsNumber(); i++) {
            if (notAvailableCells[m][i]) {
                isOkey = false;
                break;
            }
        }
        if (isOkey) {
            for (int i = n; i < n + ship.getCellsNumber(); i++) {
                busyPlaces[m][i] = true;
                field[m][i] = ship;
                tryingSurroundingCells(m, i);
            }
        }
        return isOkey;
    }

    /**
     * Method makes all surrounding cells not avoidable for other ships.
     */
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

    /**
     * Method trys to make one cell not avoidable for another ships.
     */
    private static void tryMakeNotAvoidable(int tryingM, int tryingN) {
        if (tryingN > -1 && tryingM > -1 && tryingN < N && tryingM < M) {
            notAvailableCells[tryingM][tryingN] = true;
        }
    }

    /**
     * Method paints the Ocean with the ships.
     */
    public static void drowField() {
        for (int i = 0; i < 2 * field[0].length; i++) {
            System.out.print('–');
        }
        System.out.println('–');
        for (int j = 0; j < field.length; j++) {
            System.out.print('|');
            for (int i = 0; i < field[0].length; i++) {
                if (shotedPlaces[j][i]) {
                    if (busyPlaces[j][i]) {
                        if (field[j][i].isSunk()) System.out.print(SUNKED);
                        else System.out.print(HITTED);
                    }
                    else System.out.print(MISSED);
                } else {
                    System.out.print(NOT_FIRED);
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

    /**
     * Method provides user opportunity to shot for user.
     */
    public static void Shot(int row, int column) {
        int m = row - 1;
        int n = column - 1;
        if (n > -1 && n < N && m > -1 && m < M && !shotedPlaces[m][n]) {
            System.out.println("\n++++++++++++++++++++++++++++++\n");
            shotedPlaces[m][n] = true;
            ++numberOfHits;
            if (busyPlaces[m][n]) {
                ++numberOfSuccessfulHits;
                field[m][n].shot();
                if (field[m][n].isSunk())
                    System.out.printf("You just have sunk a %s.\n",
                            field[m][n].getClass().getSimpleName());
                else System.out.println("hit");
            } else {
                System.out.println("miss");
            }
        } else {
            System.out.println("Incorrect shot!");
        }
    }

    /**
     * Method checks if all ships is sunked.
     */
    public static boolean isGameOver() {
        if (numberOfSuccessfulHits == numberOfBusyCells) {
            System.out.printf("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\nGame is over\n" +
                            "Total number of the shots: %d\nBest number: %d\n",
                    numberOfHits, numberOfBusyCells);
            return true;
        }
        return false;
    }
}
