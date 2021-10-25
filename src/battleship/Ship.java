package battleship;

public abstract class Ship {
    protected int hittedNumber = 0;

    /**
     * Method gets number of cells for current ship type.
     */
    public abstract int getCellsNumber();

    /**
     * Method processes ship hits.
     */
    public void shot() {
        ++hittedNumber;
    }

    /**
     * Method checks if the ship is sunked.
     */
    boolean isSunk() {
        return hittedNumber == getCellsNumber();
    }
}
