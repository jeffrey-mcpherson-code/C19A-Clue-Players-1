package clueGame;
/**
 * Enum representing the possible directions a door can face in the ClueGame.
 * It is used by BoardCell to determine valid movement between rooms and walkways.
 *
 * @author Jeffrey McPherson
 * @author Garrison White
 * Date: 11/07/2025
 */
public enum DoorDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    NONE;  // When not a doorway
  
}
