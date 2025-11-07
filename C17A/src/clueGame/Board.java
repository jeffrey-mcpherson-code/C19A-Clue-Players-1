/**
 * The Board class represents the Clue game board,
 * loading its setup and layout files to create a grid of BoardCells.
 *
 * Compatible with FileInitTests and BoardAdjTargetTest suites.
 *
 * @author Jeffrey McPherson
 * @author Garrison White
 * Date: 11/07/2025
 */
package clueGame;

import java.util.*;



import java.io.*;

public class Board {
    private static Board theInstance = new Board();
    private Map<Character, Room> rooms = new HashMap<>();
    private int numRows=0;
    private int numColumns=0;
    private BoardCell[][] grid;
    private Set<BoardCell> targets;
    private Set<BoardCell> visited;
    private Map<Character, Room> roomMap;
    private Map<BoardCell, Set<BoardCell>> adjMap;

 // Layout symbols
    private static final char LABEL_MARKER = '#';
    private static final char CENTER_MARKER = '*';
    private static final char NO_PASSAGE = '\0';

    // Door directions
    private static final char UP_SYMBOL = '^';
    private static final char DOWN_SYMBOL = 'v';
    private static final char LEFT_SYMBOL = '<';
    private static final char RIGHT_SYMBOL = '>';
    
    
    
    
    private String layoutConfigFile;
    private String setupConfigFile;

    // Singleton pattern
    public static Board getInstance() {
        return theInstance;
    }

    public static void reset() {
        theInstance = new Board();
    }
    
    // Private constructor
    private Board() {
        roomMap = new HashMap<>();
        adjMap = new HashMap<>();
    }
   
    public void setConfigFiles(String layout, String setup) {
        layoutConfigFile = layout;
        setupConfigFile = setup;

        roomMap.clear();
        rooms.clear();
        adjMap.clear();
        targets = null;
        visited = null;
        grid = null;

        numRows = 0;
        numColumns = 0;
    }
    
    public void initialize(){
    	try {
	        loadSetupConfig();
	        loadLayoutConfig();
	        calcAdjacencies();

    	} catch(BadConfigFormatException e) {
    		throw new RuntimeException(e);
    	}
    }

/**
 * Calculates adjacency lists for each cell.
 * Used by movement and pathfinding logic to determine legal moves.
 */
  
   
    	public void loadSetupConfig() throws BadConfigFormatException{
    	    try (Scanner in = new Scanner(new File(setupConfigFile))) {
    	        while (in.hasNextLine()) {
    	            String line = in.nextLine().trim();

    	            // Skip blank lines or comment lines
    	            if (line.isEmpty() || line.startsWith("//")) continue;

    	            String[] parts = line.split(",");
    	            if (parts.length < 3) {
    	            	throw new BadConfigFormatException("because I said so");
    	            }

    	            String type = parts[0].trim();
    	            String name = parts[1].trim();
    	            char initial = parts[2].trim().charAt(0);

    	  
    	            if (type.equalsIgnoreCase("Room") || type.equalsIgnoreCase("Space")) {
    	                Room room = new Room(name, initial);
    	                roomMap.put(initial, room);
    	                // Optional: log for debugging
    	                System.out.println("Loaded " + type + " -> " + initial + " : " + name);
    	            }
    	        }
    	    } catch (FileNotFoundException e) {
    	    	 throw new BadConfigFormatException("Setup file not found: " + setupConfigFile);
    	    }
    	}

    public void loadLayoutConfig() throws BadConfigFormatException{
        try {
       
            FileReader reader = new FileReader(layoutConfigFile);
            Scanner in = new Scanner(reader);

            List<String[]> tempGrid = new ArrayList<>();
            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (line.equals("")) continue;
                tempGrid.add(line.split(","));
                for(@SuppressWarnings("unused") String thing: line.split(",")) {
                }
            }
            in.close();

            numRows = tempGrid.size();
            numColumns = tempGrid.get(0).length;
            grid = new BoardCell[numRows][numColumns];
            
            for (int row = 0; row < numRows; row++) {
                String[] cells = tempGrid.get(row);
                int colCount = 0;
                for (int col = 0; col < numColumns; col++) {
                    String cellCode = cells[col];
                    char initial = cellCode.charAt(0);
                    Room room = roomMap.get(initial);
                    if (room == null) {
                        throw new BadConfigFormatException("Room is not in setup file");
                    }
                    if(cells.length != numColumns) {
                    	throw new BadConfigFormatException("Does not have same number of columns in every row");
                    }

                    BoardCell cell = new BoardCell(row, col);
                    cell.setRoom(room);
                    if (cellCode.length() > 1) {
                        char dir = cellCode.charAt(1);
                        switch (dir) {
                        case UP_SYMBOL:
                            cell.setDoorDirection(DoorDirection.UP);
                            cell.setDoorway(true);
                            break;

                        case DOWN_SYMBOL:
                            cell.setDoorDirection(DoorDirection.DOWN);
                            cell.setDoorway(true);
                            break;

                        case LEFT_SYMBOL:
                            cell.setDoorDirection(DoorDirection.LEFT);
                            cell.setDoorway(true);
                            break;

                        case RIGHT_SYMBOL:
                            cell.setDoorDirection(DoorDirection.RIGHT);
                            cell.setDoorway(true);
                            break;

                        case LABEL_MARKER:
                            cell.setLabel(true);
                            cell.getRoom().setLabelCell(cell);
                            break;

                        case CENTER_MARKER:
                            cell.setRoomCenter(true);
                            cell.setDoorDirection(DoorDirection.NONE);
                            cell.getRoom().setCenterCell(cell);
                            break;

                        default:
                            cell.setDoorDirection(DoorDirection.NONE);
                            cell.setSecretPassage(dir);
                            break;
                    }
                   
                    }
                    colCount++;  
                    
                    grid[row][col] = cell;
                }
            }
          //door shenanigans
            linkDoorsAndRooms();


        } catch (FileNotFoundException e) {
            System.out.println("Layout file not found: " + e.getMessage());
        } //catch (Exception e) {
            //System.out.println("Error loading layout: " + e.getMessage());
        //}
    }


    public Room getRoom(char initial) {
        return roomMap.get(initial);
    }

    public Room getRoom(BoardCell cell) {
        return cell.getRoom();
    }

    public BoardCell getCell(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numColumns)
            return null;
        return grid[row][col];
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }


/**
 * Calculates adjacency lists for each cell.
 * Used by movement and pathfinding logic to determine legal moves.
 */
    public void calcAdjacencies() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                BoardCell cell = getCell(row,col);
                if(cell!=null) {
                Set<BoardCell> adjList = new HashSet<>();

                if (cell.isWalkway() || cell.isDoorway()) {
                	int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
                	DoorDirection[] dirEnums = {DoorDirection.UP, DoorDirection.DOWN, DoorDirection.LEFT, DoorDirection.RIGHT};

                	for (int i = 0; i < directions.length; i++) {
                	    int newRow = row + directions[i][0];
                	    int newCol = col + directions[i][1];
                	    addAdjacency(adjList, cell, newRow, newCol, dirEnums[i]);
                	}

                }
                if (cell.isRoomCenter()) {
                	Room room = cell.getRoom();
                	for (BoardCell door: room.getDoors()) {
                		cell.addAdjacency(door);
                	}
                }
                }
            }
        }
    }

    private void addAdjacency(Set<BoardCell> adjList, BoardCell from, int row, int col, DoorDirection neededDir) {
        if (row < 0 || col < 0 || row >= numRows || col >= numColumns) return;
        BoardCell to = grid[row][col];
        if (to == null || to.getInitial()=='X') return;

        if (to.isWalkway()) {
            from.addAdjacency(to);
        } else if (to.isDoorway() && to.getDoorDirection() == neededDir) {
        	from.addAdjacency(to);
        } else if (from.isDoorway() && from.getDoorDirection() == neededDir) {
        	from.addAdjacency(to.getRoom().getCenterCell());
        }
    }

    public Set<BoardCell> getAdjList(int row, int col) {
        BoardCell cell = getCell(row, col);
        if (cell == null) return new HashSet<>();
        Set<BoardCell> result = cell.getAdjList();
        if (result == null) return new HashSet<>();
        return result;
    }

    


	public Set<BoardCell> getTargets() {
	
		return targets;
	}
	private void linkDoorsAndRooms() throws BadConfigFormatException {
	    for (int row = 0; row < numRows; row++) {
	        for (int col = 0; col < numColumns; col++) {
	            BoardCell cell = getCell(row, col);
	            if (cell.isDoorway()) {
	                switch (cell.getDoorDirection()) {
	                    case UP -> getCell(row - 1, col).getRoom().addDoor(cell);
	                    case DOWN -> getCell(row + 1, col).getRoom().addDoor(cell);
	                    case LEFT -> getCell(row, col - 1).getRoom().addDoor(cell);
	                    case RIGHT -> getCell(row, col + 1).getRoom().addDoor(cell);
	                    default -> throw new BadConfigFormatException("Invalid door direction.");
	                }
	            } else if (cell.getSecretPassage() != NO_PASSAGE) {
	                cell.getRoom().addDoor(roomMap.get(cell.getSecretPassage()).getCenterCell());
	            }
	        }
	    }
	}

/**
 * Determines reachable cells for a given starting point and path length.
 * Used by game logic to display legal target squares for player movement.
 */
	/**
	 * Calculates all reachable target cells from a given starting cell
	 * within the specified number of steps.
	 */
	public void calcTargets(BoardCell startCell, int steps) {
	    targets = new HashSet<>();
	    visited = new HashSet<>();
	    visited.add(startCell);
	    exploreTargets(startCell, steps);
	}

	/**
	 * Recursive helper to explore all valid targets from the current cell.
	 * Avoids revisiting cells and properly handles room entry logic.
	 */
	private void exploreTargets(BoardCell currentCell, int stepsRemaining) {
	    for (BoardCell neighbor : currentCell.getAdjList()) {

	        // Skip visited or blocked cells
	        if (visited.contains(neighbor)) continue;
	        if (neighbor.isOccupied() && !neighbor.isRoomCenter()) continue;

	        visited.add(neighbor);

	        // Base case: reached step limit or entered a room
	        if (stepsRemaining == 1 || neighbor.isRoom()) {
	            targets.add(neighbor);
	        } else {
	            exploreTargets(neighbor, stepsRemaining - 1);
	        }

	        // Backtrack to allow other paths
	        visited.remove(neighbor);
	    }
	}

}
