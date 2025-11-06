/**
 * I, Garrison White, certify that I participated equitably in the creation of assignment C14, dated 10/16/2025.
 * I, Jeffrey McPherson, certify that I participated equitably in the creation of this assignment C14, dated 10/16/2025.

 * TestBoardCell Class
 * This class represents one grid cell on the 21×21 test board.
 *
 * @author Garrison White
 * @author Jeffrey McPherson
 *
 * Date: 10/17/2025
 *
 */

package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
	private int row;
	private int col;
	private boolean isRoom;
	private boolean isOccupied;
	Set<TestBoardCell> adjList;

	public TestBoardCell(int row, int col) {
		this.col = col; 
		this.row = row; 
        this.isRoom = false;
        this.isOccupied = false;
		adjList = new HashSet<>();
	}
	
	void addAdjacency(TestBoardCell cell) {
		 adjList.add(cell);
	}

	public Set<TestBoardCell> getAdjList() {
	       return adjList;
	}
	public void setRoom(boolean isRoom) {
        this.isRoom = isRoom;
    }

    public boolean isRoom() {
        return isRoom;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public boolean getOccupied() {
        return isOccupied;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
    
    public boolean isOccupied() {
        return isOccupied;
    }
}
