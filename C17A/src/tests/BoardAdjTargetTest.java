package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("C17A/data/ClueLayout.csv", "C17A/data/ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are light green on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different rooms.
		// First, the Wine Cellar that only has two doors and a secret room
		Set<BoardCell> testList = board.getAdjList(2, 2);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(3, 4)));
		assertTrue(testList.contains(board.getCell(4, 4)));
		assertTrue(testList.contains(board.getCell(2, 19)));

		// Tests that the Pantry has four doors
		testList = board.getAdjList(18, 1);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(15, 1)));
		assertTrue(testList.contains(board.getCell(17, 5)));
		assertTrue(testList.contains(board.getCell(18, 5)));
		assertTrue(testList.contains(board.getCell(19, 5)));
		
		// Tests that the Greenhouse has two doors
		testList = board.getAdjList(8, 6);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(5, 6)));
		assertTrue(testList.contains(board.getCell(11, 6)));
	}

	
	// Ensure door locations include their rooms and also additional walkways
	// These cells are light green on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		Set<BoardCell> testList = board.getAdjList(15, 1);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(18, 1)));
		assertTrue(testList.contains(board.getCell(15, 0)));
		assertTrue(testList.contains(board.getCell(15, 2)));

		testList = board.getAdjList(18, 7);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(17, 10)));
		assertTrue(testList.contains(board.getCell(18, 6)));
		assertTrue(testList.contains(board.getCell(19, 7)));

		testList = board.getAdjList(4, 19);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(4, 18)));
		assertTrue(testList.contains(board.getCell(2, 19)));
	}
	
	// Test a variety of walkway scenarios
	// These tests are light blue on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on bottom edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(20, 7);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(19, 7)));
		
		// Test a walkway surrounded by other walkways
		testList = board.getAdjList(9, 14);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(9, 13)));
		assertTrue(testList.contains(board.getCell(9, 15)));
		assertTrue(testList.contains(board.getCell(8, 14)));
		assertTrue(testList.contains(board.getCell(10, 14)));

		// Test walkway next to building
		testList = board.getAdjList(1, 16);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(1, 15)));
		assertTrue(testList.contains(board.getCell(2, 16)));

		// Test next to center
		testList = board.getAdjList(11,12);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(10, 12)));
		assertTrue(testList.contains(board.getCell(12, 12)));
		assertTrue(testList.contains(board.getCell(11, 13)));
	
	}
	
	
	// Tests out of room center, 1, 3 and 4
	// These are purple on the planning spreadsheet
	@Test
	public void testTargetsInMasterBedroom() {
		// Tests a roll of 1
		board.calcTargets(board.getCell(9, 18), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(1, 10)));
		assertTrue(targets.contains(board.getCell(12, 18)));	
		
		// Tests a roll of 3
		board.calcTargets(board.getCell(9, 18), 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(12, 16)));
		assertTrue(targets.contains(board.getCell(12, 20)));		
		
		// test a roll of 4
		board.calcTargets(board.getCell(9, 18), 4);
		targets= board.getTargets();
		assertEquals(7, targets.size());	
	}
	
	@Test
	public void testTargetsInCigarBar() {
		// test a roll of 1
		board.calcTargets(board.getCell(2, 19), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());	
		
		// test a roll of 3
		board.calcTargets(board.getCell(2, 19), 3);
		targets= board.getTargets();
		assertEquals(8, targets.size());	
		
		// test a roll of 4
		board.calcTargets(board.getCell(2, 19), 4);
		targets= board.getTargets();
		assertEquals(8, targets.size());	
	}

	// Tests out of room center, 1, 3 and 4
	// These are purple on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(12, 18), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		
		// test a roll of 3
		board.calcTargets(board.getCell(12, 18), 3);
		targets= board.getTargets();
		assertEquals(7, targets.size());	
		
		// test a roll of 4
		board.calcTargets(board.getCell(12, 18), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());	
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(14, 14), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		
		// test a roll of 3
		board.calcTargets(board.getCell(14, 14), 3);
		targets= board.getTargets();
		assertEquals(13, targets.size());
		
		// test a roll of 4
		board.calcTargets(board.getCell(14, 14), 4);
		targets= board.getTargets();
		assertEquals(18, targets.size());
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(5, 9), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		
		// test a roll of 3
		board.calcTargets(board.getCell(5, 9), 3);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		
		// test a roll of 4
		board.calcTargets(board.getCell(5, 9), 4);
		targets= board.getTargets();
		assertEquals(16, targets.size());
	}

	@Test
	// Tests that occupied locations work properly
	public void testTargetsOccupied() {
		// Tests a roll of 3 blocked 2 down
		board.getCell(13, 6).setOccupied(true);
		board.calcTargets(board.getCell(11, 6), 3);
		board.getCell(13, 6).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(10, targets.size());
		for(BoardCell d: targets) {
			System.out.println(d.getRow() + " " + d.getCol());
		}
		assertTrue(targets.contains(board.getCell(12, 8)));
		assertTrue(targets.contains(board.getCell(12, 4)));
		assertTrue(targets.contains(board.getCell(13, 5)));	
		assertFalse(targets.contains(board.getCell(13, 6)));
		assertFalse(targets.contains(board.getCell(14, 6)));
	
		// Test that room can be reached even if occupied
		board.getCell(12, 20).setOccupied(true);
		board.getCell(8, 18).setOccupied(true);
		board.calcTargets(board.getCell(8, 17), 1);
		board.getCell(12, 20).setOccupied(false);
		board.getCell(8, 18).setOccupied(false);
		targets= board.getTargets();
		assertEquals(0, targets.size());

		// Tests attempting to enter a room with a blocked doorway
		board.getCell(12, 18).setOccupied(true);
		board.calcTargets(board.getCell(12, 20), 3);
		board.getCell(12, 18).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertFalse(targets.contains(board.getCell(9, 18)));
		assertTrue(targets.contains(board.getCell(12, 19)));	
		assertTrue(targets.contains(board.getCell(13, 18)));

	}
}
