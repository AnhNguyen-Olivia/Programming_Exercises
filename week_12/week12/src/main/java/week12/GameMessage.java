package week12;

/**
 * Data class representing a game message for network communication.
 * 
 * Example request (client → server):
 * {"boardState":"000000000","move":"5"}
 * 
 * Example response (server → client):
 * {"boardState":"200010000","status":"Computer turns"}
 * 
 * Possible status values:
 * - "Computer turns"      game continues
 * - "You wins!"           human won
 * - "Computer wins!"      computer won
 * - "Draw!"               draw
 * - "Cell is occupied!"   invalid move, board unchanged
 */

public class GameMessage {
    String boardState;
    String move;
    String status;
}
