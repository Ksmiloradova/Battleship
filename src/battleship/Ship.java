package battleship;

public abstract class Ship {
    public abstract int getCellsNumber();

    protected int hittedNumber = 0;

    public void shot() {
        ++hittedNumber;
    }

    boolean isSunk() {
        return hittedNumber == getCellsNumber();
    }
}
