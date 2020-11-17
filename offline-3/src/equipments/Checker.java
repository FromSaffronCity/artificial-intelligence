package equipments;

public class Checker {
    private boolean isBlack;  // false: white checker; true: black checker;
    private int checker;
    private int cellX;  // (cellX, cellY): (-1, -1) means checker is captured
    private int cellY;

    public Checker(boolean isBlack, int checker, int cellX, int cellY) {
        this.isBlack = isBlack;
        this.checker = checker;
        this.cellX = cellX;
        this.cellY = cellY;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public int getChecker() {
        return checker;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public int getCellX() {
        return cellX;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    public int getCellY() {
        return cellY;
    }

    @Override
    public String toString() {
        return "isBlack: "+isBlack+"; (x, y): ("+cellX+", "+cellY+");";
    }
}
