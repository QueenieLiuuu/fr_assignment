package edu.cmu.cs214.Santorini.model;

import java.util.Objects;

/**
 * Cell/Tower, is a 1x1 cell on the board.
 * Worker can stand on a cell, or build a cell to make it height + 1
 */
public class Cell {
    @Override
    public String toString() {
        return "Cell{" +
                "height=" + height +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return height == cell.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height);
    }

    public static class InvalidCellHeightError extends RuntimeException {
        public InvalidCellHeightError(int h) {
            super("invalid height: " + h);
        }
    }

    public static final int MAX_HEIGHT = 4;
    private int height;

    public Cell() {
        this.height = 0;
    }

    public Cell(int height) {
        if (Math.abs(height) > MAX_HEIGHT) {
            throw new InvalidCellHeightError(height);
        }
        this.height = height;
    }

    /**
     * we use negative height to denote a dome.
     * Normally, a dome is -4, but some gods can build a dome on the lower level.
     */
    public boolean isDome() {
        return this.height < 0;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * build on this cell, improve height +1
     * @return height after built, if failed to build, return 0
     */
    public int build() {
        if (isDome()) {
            return 0;
        }
        if (isHighestWithoutDome()) {
            this.height = -(this.height + 1);
            return this.height;
        }
        return ++this.height;
    }

    /**
     * build a dome on this cell, improve height +1 and make it negative
     * @return height after built, if failed to build, return 0
     */
    public int buildDome() {
        if (isDome()) {
            return 0;
        }
        this.height = -(this.height + 1);
        return this.height;
    }

    public Cell copy() {
        return new Cell(height);
    }

    public boolean isHighestWithoutDome() {
        return this.height == MAX_HEIGHT - 1;
    }
}
