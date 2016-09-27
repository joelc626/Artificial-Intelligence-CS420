/* Joel Castro
 * CS420 - Project 1
 * Driver: NQueenProblem.java
 *
 * Implement both the Steepest Hill Climbing and MIN-CONFLICTS
 * algorithm to solve the 16-queen problem
 */

package nqueenproblem;

public class ChessSquare {
    private boolean queenOn;
    
    //Constructor
    public ChessSquare() {
        queenOn = false;
    }

    //Place queen on square
    public void placeQueen() {
        queenOn = true;
    }

    //Remove queen from this square
    public void removeQueen() {
        queenOn = false;
    }

    //Return if queen is on this square or not
    public boolean getQueenOn() {
        return queenOn;
    }
}