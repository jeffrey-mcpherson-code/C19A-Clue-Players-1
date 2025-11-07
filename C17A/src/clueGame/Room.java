/**
 * Represents a single room or space in the ClueGame board.
 * Loaded from the legend file (.txt) to map initials to full names.
 *
 * @author Jeffrey McPherson
 * @author Garrison White
 * Date: 11/07/2025
 */
package clueGame;

import java.util.Set;
import java.util.HashSet;

public class Room {
    private String name;
    private char initial;
    private BoardCell centerCell;
    private BoardCell labelCell;
    private Set<BoardCell> doorList;

    public Room(String name, char initial) {
        this.name = name;
        this.initial = initial;
        this.doorList = new HashSet<BoardCell>();
    }

    public String getName() {
        return name;
    }

    public char getInitial() {
        return initial;
    }

    public BoardCell getCenterCell() {
        return centerCell;
    }

    public BoardCell getLabelCell() {
        return labelCell;
    }

    public void setCenterCell(BoardCell cell) {
        this.centerCell = cell;
    }

    public void setLabelCell(BoardCell cell) {
        this.labelCell = cell;
    }
    
    public void addDoor(BoardCell cell) {
    	this.doorList.add(cell);
    }
    
    public Set<BoardCell> getDoors() {
    	return doorList;
    }
}
