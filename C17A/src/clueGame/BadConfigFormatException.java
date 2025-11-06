package clueGame;

/**
 * Custom exception thrown when the configuration files for the board
 * (layout or legend) are improperly formatted or missing expected values.
 *
 * For example:
 * - Unknown room initials in the layout
 * - Inconsistent column counts in rows
 * - Invalid door direction symbols
 *
 * @author Jeffrey McPherson
 * @author Garrison White
 * Date: 10/22/2025
 */

public class BadConfigFormatException extends Exception {

    public BadConfigFormatException() {
        super("Invalid board format");
    }


    public BadConfigFormatException(String message) {
        super(message);
    }

}
