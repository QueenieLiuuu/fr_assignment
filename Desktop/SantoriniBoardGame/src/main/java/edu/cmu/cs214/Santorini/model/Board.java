package edu.cmu.cs214.Santorini.model;

import org.springframework.boot.web.embedded.undertow.UndertowWebServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Board, the 5x5 chess board for the game
 * contains cells (towers) with various heights
 */
public class Board {
    public List<List<Integer>> to2DHeightList() {
        List<List<Integer>> boardHeights = new ArrayList<>();
        for (var row : this.innerBoard) {
            ArrayList<Integer> rowHeights = new ArrayList<>();
            for (Cell cell : row) {
                rowHeights.add(cell.getHeight());
            }
            boardHeights.add(rowHeights);
        }
        return boardHeights;
    }

    public static class OutOfBoardError extends RuntimeException {
        public OutOfBoardError(int x, int y) {
            super(String.format("position (%1s,%2s) is out of board", x, y));
        }
    }

    public static class ConflictWorkersError extends RuntimeException {
        public ConflictWorkersError(int x, int y) {
            super(String.format("cannot interact with (%1s,%2s) because a worker is on it", x, y));
        }
    }

    public static final int BOARD_SIZE = 5;

    private final List<List<Cell>> innerBoard;
    private List<Worker> workers;

    public Board(List<List<Cell>> innerBoard, List<Worker> workers) {
        this.innerBoard = innerBoard;
        this.workers = workers;
    }

    public Board() {
        innerBoard = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j < BOARD_SIZE; j++) {
                row.add(new Cell());
            }
            innerBoard.add(row);
        }
        workers = new ArrayList<>();
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    /**
     * add worker to the board
     *
     * @param worker new worker to add
     * @return if add successfully
     */
    public boolean addWorker(Worker worker) {
        if (!workers.contains(worker)) {
            workers.add(worker);
            return true;
        }
        return false;
    }

    /**
     * Given a position (x, y), check if this position is in board range
     *
     * @param x, x
     * @param y, y
     * @return boolean
     */
    public boolean positionInBoard(int x, int y) {
        return x >= 0 && y >= 0 && x < getBoardSize() && y < getBoardSize();
    }

    public boolean positionInBoard(Point p) {
        return positionInBoard(p.x(), p.y());
    }

    /**
     * @return board size, default: 5
     */
    public int getBoardSize() {
        return this.innerBoard.size();
    }

    /**
     * @return the cell on board(x,y)
     * @throws OutOfBoardError if (x,y) is out of board
     */
    public Cell getCell(int x, int y) throws OutOfBoardError {
        if (!positionInBoard(x, y)) {
            throw new OutOfBoardError(x, y);
        }
        return innerBoard.get(x).get(y);
    }

    public Cell getCell(Point p) throws OutOfBoardError {
        return getCell(p.x(), p.y());
    }

    /**
     * @throws Cell.InvalidCellHeightError if (x,y) is out of board or height is invalid
     */
    public void setCell(int x, int y, int height) throws Cell.InvalidCellHeightError {
        if (!positionInBoard(x, y)) {
            throw new OutOfBoardError(x, y);
        }
        if (Math.abs(height) > Cell.MAX_HEIGHT) {
            throw new Cell.InvalidCellHeightError(height);
        }
        getCell(x, y).setHeight(height);
    }

    /**
     * @return true if there is a worker on position x,y, otherwise false
     */
    public boolean hasWorker(int x, int y) {
        return getWorker(x, y) != null;
    }

    public boolean hasWorker(Point p) {
        return this.hasWorker(p.x(), p.y());
    }

    /**
     * @return get worker if there is a worker on position x,y, otherwise null
     */
    public Worker getWorker(int x, int y) {
        for (Worker worker : workers) {
            if (worker.getP().x() == x && worker.getP().y() == y) {
                return worker;
            }
        }
        return null;
    }

    public Worker getWorker(Point p) {
        return getWorker(p.x(), p.y());
    }


    /**
     * build on cell at board(x, y), improve height +1
     *
     * @return height after built, if failed to build, return -1
     */
    public int build(int x, int y, boolean buildDome) throws RuntimeException {
        if (!positionInBoard(x, y)) {
            throw new OutOfBoardError(x, y);
        }
        if (hasWorker(x, y)) {
            throw new ConflictWorkersError(x, y);
        }
        if (buildDome) {
            return this.getCell(x, y).buildDome();
        } else {
            return this.getCell(x, y).build();
        }
    }

    public int build(int x, int y) throws RuntimeException {
        return this.build(x, y, false);
    }

    public int build(Point p, boolean buildDome) throws RuntimeException {
        return this.build(p.x(), p.y(), buildDome);
    }

    public int build(Point p) throws RuntimeException {
        return this.build(p.x(), p.y());
    }

    /**
     * search two boards, return the first point that with different height
     *
     * @param b1 board 1
     * @param b2 board 2
     * @return diff position
     */
    public static Point findBoardDiff(Board b1, Board b2) {
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (b1.getCell(i, j).getHeight() != b2.getCell(i, j).getHeight()) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    /**
     * dasher dash to victim, tries to force them backward.
     * However, they cannot force when there is wall/another worker at the back
     *
     * @param dasher worker who dashes
     * @param victim worker who suffers from the attack
     * @return victim's new position if succeeded, else null
     */
    public Point dashToPosition(Point dasher, Point victim) {
        int dx = victim.x() - dasher.x();
        int dy = victim.y() - dasher.y();
        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {  // too far
            return null;
        }
        int newX = victim.x() + dx;
        int newY = victim.y() + dy;
        if (!this.positionInBoard(newX, newY) || this.hasWorker(newX, newY)
                || this.getCell(newX, newY).isDome()) {
            return null;
        }
        return new Point(newX, newY);
    }

    /**
     * reset the board with heights=0
     */
    public void reset() {
        this.workers.clear();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                setCell(i, j, 0);
            }
        }
    }

    /**
     * Given a worker, return all possible points they can move to/build on
     * a worker can move downstairs, one level upstairs but not a dome, or horizontally.
     * a worker can build all surrounding cells that is without a dome.
     *
     * @param worker     a worker going to move/build
     * @param movable    set true if the player gonna move, set false if they gonna build
     * @param onOpponent set true if the player can move/build on opponent's worker
     * @return a list of points available to move/build
     */
    public List<Point> getPoints(Worker worker, boolean movable, boolean onOpponent) {
        List<Direction> directions = new ArrayList<>();
        Point p = worker.getP();
        int x = p.x();
        int y = p.y();
        int currentHeight = getCell(x, y).getHeight();
        for (Direction d : Direction.class.getEnumConstants()) {  // for each direction
            int newX = x + d.getValue()[0];
            int newY = y + d.getValue()[1];
            if (!positionInBoard(newX, newY)) {
                continue;
            }
            Worker workerOnP = getWorker(newX, newY);
            if (workerOnP != null) {
                if (!onOpponent) {
                    continue;
                } else if (workerOnP.getPlayer().equals(worker.getPlayer())) {
                    continue;
                }
            }
            Cell cell = getCell(newX, newY);
            if (cell.isDome()) {  // cannot stand/build on a dome
                continue;
            }
            int height = cell.getHeight();
            if (movable && (height - currentHeight > 1)) {  // cannot jump higher than one level
                continue;
            }
            directions.add(d);
        }
        List<Point> points = new ArrayList<>();
        for (var d : directions) {
            points.add(new Point(x + d.getValue()[0], y + d.getValue()[1]));
        }
        return points;
    }

    public List<Point> getPoints(Worker worker, boolean movable) {
        return this.getPoints(worker, movable, false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Board{\n");
        for (var row : this.innerBoard) {
            for (Cell cell : row) {
                sb.append(String.format("%d,", cell.getHeight()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Copy the board's all cells (but do not copy workers) and return the copied for immutable
     * context.
     * Because the players maintain their workers, if board copies too, there will be
     * inconsistent workers.
     *
     * @param workers workers (probably copied by players)
     * @return copied Board
     */
    public Board copy(List<Worker> workers) {
        List<List<Cell>> copyBoard = new ArrayList<>();
        for (var row : innerBoard) {
            List<Cell> copyRow = new ArrayList<>();
            for (var cell : row) {
                copyRow.add(cell.copy());
            }
            copyBoard.add(copyRow);
        }
        return new Board(copyBoard, workers);
    }
}
