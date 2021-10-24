package battleship;

import java.util.Random;

public class Field {
    static char[][] field;
    static boolean[][] busyPlaces;
    static int M;
    static int N;
    static Ship[] fleet;
    private static int shipCounter;
    static int N5;
    static int N4;
    static int N3;
    static int N2;
    static int N1;

    static public void getGameParameters(int m, int n, int n5, int n4, int n3, int n2, int n1) {
        M = m;
        N = n;
        field = new char[M][N];
        busyPlaces = new boolean[M][N];
        shipCounter = n5 + n4 + n3 + n2 + n1;
        fleet = new Ship[shipCounter];
        addShip(n5, Carrier.class);
        addShip(n4, Battleship.class);
        addShip(n3, Cruiser.class);
        addShip(n2, Destroyer.class);
        addShip(n1, Submarine.class);
    }

    private static void addShip(int number, Class<?> cls) {
        try {
            fleet[fleet.length - (shipCounter--)] = (Ship) cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static private void randomStandShips(int min, int max) {
        Random random = new Random();
        for (Ship ship :
                fleet) {
            boolean isOkey = false;
            while (!isOkey) {
                int maxM = M, maxN = N;
                boolean isGorizontal = random.nextBoolean();
                if (isGorizontal) maxM -= ship.getCellsNumber();
                else maxN -= ship.getCellsNumber();
                if (maxN < 0 || maxM < 0) continue;
                int m = random.nextInt(maxM) + 1;
                int n = random.nextInt(maxN) + 1;
                /** Checks if the position is possible. */
                isOkey = true;
                for (int i = m; m < m + ship.getCellsNumber(); m++) {
                    for (int j = n; n < n + ship.getCellsNumber(); n++) {
                        if (busyPlaces[i][j]) {
                            isOkey = false;
                            break;
                        }
                    }
                    if (!isOkey) break;
                }
            }
        }

    }
}
