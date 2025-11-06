package tests;

/*
 * This program tests that config files are loaded properly.
 */

// Doing a static import allows me to write assertEquals rather than
// Assert.assertEquals
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

public class FileInitTests {
	// Board constraints for dimensions 
	public static final int LEGEND_SIZE = 12;
	public static final int NUM_ROWS = 21;
	public static final int NUM_COLUMNS = 21;

	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// Set the file names to use my ClueLayout.csv and ClueSetup.txt
		board.setConfigFiles("C17A/data/ClueLayout.csv", "C17A/data/ClueSetup.txt");
		// Initialize will loads all config files
		board.initialize();
	}

	@Test
	public void testRoomLabels() {
		// Tests that the correct abbreviation is used for the given rooms
		assertEquals("Wine Cellar", board.getRoom('C').getName() );
		assertEquals("Cigar Bar", board.getRoom('B').getName() );
		assertEquals("Theater", board.getRoom('T').getName() );
		assertEquals("Spa", board.getRoom('S').getName() );
		assertEquals("Walkway", board.getRoom('W').getName() );
	}

	@Test
	public void testBoardDimensions() {
		// Tests for the same number of rows and columns as our layout
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	@Test
	public void FourDoorDirections() {
		// Tests a leftward facing doorway
		BoardCell cell = board.getCell(4, 4);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		// Tests a upward facing doorway
		cell = board.getCell(3, 10);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		// Tests a rightward facing doorway
		cell = board.getCell(2, 17);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		// Tests a downward facing doorway
		cell = board.getCell(14, 16);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Test a walkway that isn't a door
		cell = board.getCell(13, 7);
		assertFalse(cell.isDoorway());
	}
	

	// Tests for the correct number of doors on the total board
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(21, numDoors);
	}

	// Test a few room cells to ensure the room initial is correct.
	@Test
	public void testRooms() {
		// Tests that the given cell is a standard cell of Wine Cellar
		BoardCell cell = board.getCell(3, 2);
		Room room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals(room.getName(), "Wine Cellar" ) ;
		assertFalse( cell.isLabel() );
		assertFalse( cell.isRoomCenter() ) ;
		assertFalse( cell.isDoorway()) ;

		// Tests that the given cell is the label cell for Theater
		cell = board.getCell(16, 10);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Theater" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		
		// Tests that the given cell is the center cell for Art Gallery
		cell = board.getCell(17, 18);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Art Gallery" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );
		
		// Tests that the given cell is a secret passage from Wine Cellar to Cigar Bar
		cell = board.getCell(3, 0);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Wine Cellar" ) ;
		assertTrue( cell.getSecretPassage() == 'B' );
		
		// Tests that a given cell is a Walkway
		cell = board.getCell(6, 2);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Walkway" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		
		// Tests that a given cell is Unused
		cell = board.getCell(0, 20);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Unused" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		
	}

}
