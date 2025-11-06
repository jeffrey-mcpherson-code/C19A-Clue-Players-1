/**
 * TestBoard Class
 * This class holds the full 21×21 board.
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

public class TestBoard {
    private TestBoardCell[][] grid;
    private Set<TestBoardCell> targets;
    private Set<TestBoardCell> visited;
    public TestBoard() {
        grid = new TestBoardCell[21][21];
        for (int row = 0; row < 21; row++) {
            for (int col = 0; col < 21; col++) {
                grid[row][col] = new TestBoardCell(row, col);
            }
        }
        targets = new HashSet<>();
        calcAdjacencies();
    }

    private void calcAdjacencies() {
    	 for (int row = 0; row < 21; row++) {
             for (int col = 0; col < 21; col++) {
            	 TestBoardCell cell = grid[row][col];
                 
                 //UP
                 if (row > 0) {
                	 cell.adjList.add(grid[row - 1][col]);
                 }
                 //DOWN
                 if (row < 20) {
                	 cell.adjList.add(grid[row + 1][col]);
                 }
                 
                 //LEFT
                 if (col > 0) {
                	 cell.adjList.add(grid[row][col - 1]);
                 }
                 
                 //RIGHT
                 if (col < 20) {
                	 cell.adjList.add(grid[row][col + 1]);
                 }
             }
    	 }
	}

    public void calcTargets(TestBoardCell startCell, int pathLength) {
        targets.clear();
        visited = new HashSet<>();
        visited.add(startCell);
        findAllTargets(startCell, pathLength);
    }

	
    private void findAllTargets(TestBoardCell currentCell, int stepsRemaining) {
        for (TestBoardCell adj : currentCell.getAdjList()) {
            if (visited.contains(adj) || adj.isOccupied()) {
                continue;
            }
            visited.add(adj);

            if (stepsRemaining == 1 || adj.isRoom()) {
                targets.add(adj);
            } else {
                findAllTargets(adj, stepsRemaining - 1);
            }

            visited.remove(adj); 
        }
    }
	
    public TestBoardCell getCell(int row, int col) {
        return grid[row][col];
    }

    public Set<TestBoardCell> getTargets() {
        return targets;  
    }

}
