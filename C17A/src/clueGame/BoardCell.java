
/**
 * The BoardCell class represents one square on the Clue game board.
 * It stores its grid coordinates, room initial, and whether it contains
 * a doorway — and in which direction that door faces.
 *
 * Stage: Initialization (Clue Paths 3)
 * 
 * @author Jeffrey McPherson
 * @author Garrison White
 * Date: 10/22/2025
 */    
package clueGame;

import java.util.Set;
import java.util.HashSet;

import experiment.TestBoardCell;

public class BoardCell {
    private int row, col;
    private boolean occupied = false;
    private boolean isLabel = false;
    private boolean isRoomCenter = false;
    private boolean doorway = false;   
    private char secretPassage = '\0';
    private char initial;
    private Room room;
    private DoorDirection doorDirection = DoorDirection.NONE;
	Set<BoardCell> adjList;
	
    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.doorway = false;
        this.doorDirection = DoorDirection.NONE;
        this.adjList = new HashSet<BoardCell>();
    }

	void addAdjacency(BoardCell cell) {
		 adjList.add(cell);
	}

	public Set<BoardCell> getAdjList() {
	       return adjList;
	}
	
    public int getRow() { return row; }
    public int getCol() { return col; }

    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean val) { occupied = val; }

    public void setRoom(Room r) { room = r; }
    public Room getRoom() { return room; }

    public boolean isRoom() {
        return room != null && !room.getName().equals("Walkway") && !room.getName().equals("Unused");
    }

    public boolean isDoorway() { return doorway; }
    public void setDoorway(boolean val) { doorway = val; }

    public DoorDirection getDoorDirection() { return doorDirection; }
    public void setDoorDirection(DoorDirection dir) { doorDirection = dir; }

    public boolean isWalkway() {
        return room != null && room.getName().equals("Walkway");
    }

    public boolean isLabel() { return isLabel; }
    public void setLabel(boolean val) { isLabel = val; }

    public boolean isRoomCenter() { return isRoomCenter; }
    public void setRoomCenter(boolean val) { isRoomCenter = val; }

    public char getSecretPassage() { return secretPassage; }
    public void setSecretPassage(char sp) { secretPassage = sp; }

    public char getInitial() { return initial; }
    public void setInitial(char initial) { this.initial = initial; }
    
    public void setDoorway(boolean doorway, DoorDirection dir) {
        this.doorway = doorway;
        this.doorDirection = dir;
    }
    

}