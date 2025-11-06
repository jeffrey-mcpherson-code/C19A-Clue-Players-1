/**
 * BoardTestsExp Class
 * JUnit test class
 *
 * @author Garrison White
 * @author Jeffrey McPherson
 *
 * Date: 10/17/2025
 *
 */


package tests;

import experiment.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class BoardTestsExp {
    private TestBoard board;

    @BeforeEach
    public void setUp() {
        board = new TestBoard();
    }

    @Test
    public void testAdjacencyTopLeftCorner() {
        TestBoardCell cell = board.getCell(0, 0);
        Set<TestBoardCell> testList = cell.getAdjList();
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(0, 1)));
        assertTrue(testList.contains(board.getCell(1, 0)));
    }

    @Test
    public void testAdjacencyBottomRightCorner() {
        TestBoardCell cell = board.getCell(20, 20);
        Set<TestBoardCell> testList = cell.getAdjList();
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(20, 19)));
        assertTrue(testList.contains(board.getCell(19, 20)));
    }

    @Test
    public void testTargetsOneStep() {
        TestBoardCell startCell = board.getCell(2, 2);
        board.calcTargets(startCell, 1);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(4, targets.size()); // should fail until implemented
    }

    @Test
    public void testTargetsTwoSteps() {
        TestBoardCell startCell = board.getCell(2, 2);
        board.calcTargets(startCell, 2);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(8, targets.size()); // fails now, will pass later
    }

    @Test
    public void testTargetsWithRoomAndOccupied() {
        TestBoardCell startCell = board.getCell(1, 1);
        board.getCell(2, 2).setRoom(true);
        board.getCell(1, 2).setOccupied(true);
        board.calcTargets(startCell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        assertFalse(targets.contains(board.getCell(1, 2))); // occupied
        assertTrue(targets.contains(board.getCell(2, 2))); // room
    }
}
