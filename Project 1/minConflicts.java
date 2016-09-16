/* Joel Castro
 * CS420 - Project 1
 *
 * Implement both the Steepest Hill Climbing and MIN-CONFLICTS
 * algorithm to solve the 16-queen problem
 */

package nqueenproblem;

import java.util.ArrayList;
import java.util.Random;

public class minConflicts {

    private int size;
    private int stepCost;
    private ChessSquare[][] board;

    public minConflicts(int n) {
        size = n;
        stepCost = 0;
        board = new ChessSquare[n][n];

        //initialze board with no queens on the board
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = new ChessSquare();
            }
        }
    }

    public boolean minConflicts() {
        Random rand = new Random();
        int[] parent = new int[size + 1]; //+1 to store the #OfConflicts of initial
        int[] child = new int[size];
        int[][] possibleChildrensConf = new int[size][size];
        int bestChildFitness;
        int bestChildRow = size;; //Set to fictitious # incase cant find a better child
        boolean solved = false;
        int moves = 0;

        //Randomize the Q's on the board
        for (int i = 0; i < size; i++) {
            int row = rand.nextInt(size);
            parent[i] = row;
            child[i] = row;
            board[row][i].placeQueen();
        }

        //Find # of conflicts for the board
        parent[size] = numOfConflicts(parent);

        //Set current best childs fitness to be parents fitness
        bestChildFitness = parent[size];

        //If no conflicts then its solved
        if (parent[size] == 0) {
            solved = true;
        }

        //Used to pick random columns
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int col = 0; col < size; col++) {
            candidates.add(col);
        }

        while (!solved && moves < size * 10) {
            int randomQueenToBeMoved = candidates.get(rand.nextInt(candidates.size()));

            //Find # of conflicts of the chosen column
            for (int row = 0; row < size; row++) {
                possibleChildrensConf[row][randomQueenToBeMoved] = numOfConflicts(child);
            }

            //Find row with lowest conflicts
            for (int row = 0; row < size; row++) {
                if (possibleChildrensConf[row][randomQueenToBeMoved] < bestChildFitness) {
                    bestChildRow = row;
                    bestChildFitness = possibleChildrensConf[row][randomQueenToBeMoved];
                }
            }

            //If solution found
            if (bestChildFitness == 0) {
                solved = true;
                moves++;
                break;
            }

            //Remove chosen Queens column's (now old) position
            removeQueen(randomQueenToBeMoved);

            //Add chosen Queens new position if a better row was found
            if (bestChildRow != size) {
                board[bestChildRow][randomQueenToBeMoved].placeQueen();
                moves++;
            }
        }

        return solved;
    }

    //Get # of conflicts
    public int numOfConflicts(int[] b) {
        int conf = 0;

        for (int col = 0; col < size; col++) {
            conf += checkEast(b[col], col);
            conf += checkNorthEast(b[col], col);
            conf += checkSouthEast(b[col], col);
        }
        return conf;
    }

    //Check the board horizontally
    public int checkEast(int r, int c) {
        int conf = 0;
        c++;

        for (; c < size; c++) {
            if (board[r][c].getQueenOn()) {
                conf++;
            }
        }
        return conf;
    }

    //Check the board top right direction
    public int checkNorthEast(int r, int c) {
        int conf = 0;
        r--;
        c++;

        for (; c < size && r >= 0; c++, r--) {
            if (board[r][c].getQueenOn()) {
                conf++;
            }
        }
        return conf;
    }

    //Check the board bottom right direction
    public int checkSouthEast(int r, int c) {
        int conf = 0;
        r++;
        c++;

        for (; c < size && r < size; c++, r++) {
            if (board[r][c].getQueenOn()) {
                conf++;
            }
        }
        return conf;
    }

    public void removeQueen(int col) {
        for (int row = 0; row < size; row++) {
            board[row][col].removeQueen();
        }
    }

    public int getstepCost() {
        return stepCost;
    }
}
