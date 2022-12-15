package edu.cmu.cs214.Santorini.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {
    Board board;
    Player player1;
    Player player2;
    Worker worker1;
    Worker worker2;

    @Before
    public void setUp() {
        board = new Board();
        player1 = new Player(1);
        player2 = new Player(2);
        worker1 = new Worker(1, player1);
        worker2 = new Worker(2, player1);
    }

    public static void setBoard(Board board, int[] heights) {
        assert heights.length == Board.BOARD_SIZE * Board.BOARD_SIZE;
        for (int i = 0; i < heights.length; i++) {
            int x = i / Board.BOARD_SIZE;
            int y = i % Board.BOARD_SIZE;
            board.setCell(x, y, heights[i]);
        }
    }

    private static int countZeroHeights(Board board) {
        int count = 0;
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (board.getCell(i, j).getHeight() == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    @Test
    public void testConstructor() {
        assertEquals(board.getBoardSize(), Board.BOARD_SIZE);
    }

    @Test
    public void testPositionInBoard() {
        assertTrue(board.positionInBoard(0, 0));
        assertTrue(board.positionInBoard(0, Board.BOARD_SIZE - 1));
        assertTrue(board.positionInBoard(2, Board.BOARD_SIZE - 1));
        assertTrue(board.positionInBoard(Board.BOARD_SIZE - 1, Board.BOARD_SIZE - 1));
        assertFalse(board.positionInBoard(-1, 1));
        assertFalse(board.positionInBoard(1, -1));
        assertFalse(board.positionInBoard(0, Board.BOARD_SIZE));
        assertFalse(board.positionInBoard(Board.BOARD_SIZE, 2));
    }

    @Test
    public void testHasWorker() {
        board.addWorker(worker1);
        board.addWorker(worker2);
        worker1.setP(new Point(1, 1));
        worker2.setP(new Point(3, 2));
        assertTrue(board.hasWorker(1, 1));
        assertTrue(board.hasWorker(3, 2));
        assertFalse(board.hasWorker(2, 2));
        assertFalse(board.hasWorker(3, 3));
    }

    @Test
    public void testAddDuplicateWorker() {
        assertEquals(0, board.getWorkers().size());
        board.addWorker(worker1);
        assertEquals(1, board.getWorkers().size());
        board.addWorker(worker1);
        assertEquals(1, board.getWorkers().size());
        board.addWorker(worker2);
        assertEquals(2, board.getWorkers().size());
    }

    @Test
    public void testGetAndSetCell() {
        assertEquals(board.getCell(0, 0).getHeight(), 0);
        board.setCell(0, 0, 4);
        assertEquals(board.getCell(0, 0).getHeight(), 4);
        assertEquals(board.getCell(1, 1).getHeight(), 0);
    }

    @Test(expected = Board.OutOfBoardError.class)
    public void testGetCellOOB() {
        board.getCell(-1, 0);
    }

    @Test(expected = Board.OutOfBoardError.class)
    public void testSetCellOOB() {
        board.setCell(-1, 0, 1);
    }

    @Test(expected = Board.OutOfBoardError.class)
    public void testBuildOOB() {
        board.build(-1, 0);
    }

    @Test(expected = Board.ConflictWorkersError.class)
    public void testBuildConflictWorker() {
        board.addWorker(worker1);
        worker1.setP(new Point(1, 0));
        board.build(1, 0);
    }

    @Test(expected = Cell.InvalidCellHeightError.class)
    public void testSetCellInvalidHeight() {
        board.setCell(0, 0, Board.BOARD_SIZE + 1);
    }

    @Test
    public void testBoardSize() {
        assertEquals(board.getBoardSize(), Board.BOARD_SIZE);
    }

    @Test
    public void testReset() {
        BoardTest.setBoard(board, new int[]{
                1, 2, 1, 0, 0,
                -4, 1, 3, 0, 0,
                0, 1, -4, 3, -4,
                0, 0, 1, 3, 2,
                0, 0, 4, 0, 0
        });
        board.addWorker(worker1);
        board.addWorker(worker2);
        assertNotEquals((long) board.getBoardSize() * board.getBoardSize(), countZeroHeights(board));
        assertNotEquals(0, board.getWorkers().size());

        board.reset();
        assertEquals((long) board.getBoardSize() * board.getBoardSize(), countZeroHeights(board));
        assertEquals(0, board.getWorkers().size());
    }

    @Test
    public void testBuild() {
        BoardTest.setBoard(board, new int[]{
                1, 2, 1, 0, 0,
                -4, 1, 3, 0, 0,
                0, 1, -4, 3, -4,
                0, 0, 1, 3, 2,
                0, 0, -4, 0, 0
        });
        assertEquals(2, board.build(0, 0));
        assertEquals(2, board.getCell(0, 0).getHeight());

        assertEquals(0, board.build(1, 0));
        assertEquals(-4, board.getCell(1, 0).getHeight());
    }

    @Test
    public void testGetDirections() {
        BoardTest.setBoard(board, new int[]{
                1, 2, 1, 0, 0,
                -4, 1, 3, 0, 0,
                0, 1, -4, 3, -4,
                0, 0, 1, 3, 2,
                0, 0, -4, 0, 0
        });
        board.addWorker(worker1);
        board.addWorker(worker2);
        worker1.setP(new Point(0, 1));
        worker2.setP(new Point(4, 4));
        Worker worker = new Worker(3, player2);
        worker.setP(new Point(0, 0));
        List<Point> expectedMovablePoints;
        List<Point> expectedBuildablePoints;
        expectedMovablePoints = new ArrayList<>(List.of(
                new Point(1, 1)
        ));
        expectedBuildablePoints = expectedMovablePoints;
        assertEquals(expectedMovablePoints, board.getPoints(worker, true));
        assertEquals(expectedBuildablePoints, board.getPoints(worker, false));

        worker.setP(new Point(1, 1));
        expectedMovablePoints = new ArrayList<>(Arrays.asList(
                new Point(0, 0),
                new Point(0, 2),
                new Point(2, 1),
                new Point(2, 0)
        ));
        expectedBuildablePoints = new ArrayList<>(Arrays.asList(
                new Point(0, 0),
                new Point(0, 2),
                new Point(1, 2),
                new Point(2, 1),
                new Point(2, 0)
        ));
        assertEquals(expectedMovablePoints, board.getPoints(worker, true));
        assertEquals(expectedBuildablePoints, board.getPoints(worker, false));

        worker.setP(new Point(3, 3));
        expectedMovablePoints = new ArrayList<>(Arrays.asList(
                new Point(2, 3),
                new Point(3, 4),
                new Point(4, 3),
                new Point(3, 2)
        ));
        expectedBuildablePoints = expectedMovablePoints;
        assertEquals(expectedMovablePoints, board.getPoints(worker, true));
        assertEquals(expectedBuildablePoints, board.getPoints(worker, false));
        board.reset();
    }

    @Test
    public void testTo2DHeightList() {
        int[] the2DArray = new int[]{
                1, 2, 1, 0, 0,
                -4, 1, 3, 0, 0,
                0, 1, -4, 3, -4,
                0, 0, 1, 3, 2,
                0, 0, -4, 0, 0
        };
        BoardTest.setBoard(board, the2DArray);
        List<List<Integer>> list = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            list.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0)));
        }
        for (int i = 0; i < the2DArray.length; i++) {
            int x = i / Board.BOARD_SIZE;
            int y = i % Board.BOARD_SIZE;
            list.get(x).set(y, the2DArray[i]);
        }
        var listFromBoard = board.to2DHeightList();
        for (int i = 0; i < 5; i++) {
            assertEquals(list.get(i), listFromBoard.get(i));
        }
    }

    @Test
    public void testMoveOnOpponentWorker() {
        Worker worker3 = new Worker(3, this.player2);
        board.addWorker(worker1);
        board.addWorker(worker2);
        board.addWorker(worker3);

        worker1.setP(new Point(0, 0));
        worker2.setP(new Point(1, 1));
        worker3.setP(new Point(0, 1));

        List<Point> worker1NormalMovablePoints = board.getPoints(worker1, true, false);
        // cannot step on other workers
        assertFalse(worker1NormalMovablePoints.contains(new Point(1, 1)));
        assertFalse(worker1NormalMovablePoints.contains(new Point(0, 1)));

        List<Point> worker1StepOpponentMovablePoints = board.getPoints(worker1, true, true);
        // can step on opponent workers
        assertTrue(worker1StepOpponentMovablePoints.contains(new Point(0, 1)));
        // still cannot step on self workers
        assertFalse(worker1StepOpponentMovablePoints.contains(new Point(1, 1)));
    }

    @Test
    public void testCopy() {
        board.setCell(1, 1, 1);
        worker1.setP(new Point(1, 2));
        board.addWorker(worker1);
        board.addWorker(worker2);
        Board copyBoard = board.copy(new ArrayList<>(List.of(worker1, worker2)));
        assertEquals(1, copyBoard.getCell(1, 1).getHeight());
        board.getCell(1, 1).setHeight(2);
        assertEquals(2, board.getCell(1, 1).getHeight());
        assertEquals(1, copyBoard.getCell(1, 1).getHeight());

        assertEquals(copyBoard.getWorkers().get(0).getP(), board.getWorkers().get(0).getP());
        board.getWorkers().get(0).setP(new Point(0, 0));
        assertEquals(new Point(0, 0), copyBoard.getWorkers().get(0).getP());
    }

    @Test
    public void testDashToPosition() {
        Point dasher = new Point(0, 0);
        Point victim = new Point(0, 1);
        assertEquals(new Point(0, 2), board.dashToPosition(dasher, victim));
        worker1.setP(new Point(0, 2));
        board.addWorker(worker1);
        assertNull(board.dashToPosition(dasher, victim));
        dasher = new Point(1, 1);
        assertNull(board.dashToPosition(dasher, victim));
    }
}
