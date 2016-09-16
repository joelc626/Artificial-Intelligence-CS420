/* Joel Castro
 * CS420 - Project 1
 *
 * Implement both the Steepest Hill Climbing and MIN-CONFLICTS
 * algorithm to solve the 16-queen problem
 */

package nqueenproblem;

import java.util.Random;

public class steepHillClimb {

    private int size;
    private int stepCost;
    private ChessSquare[][] board;
    
    public steepHillClimb(int n) {
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

    public boolean steepHillClimb() {
        Random rand = new Random();
        int[] parent = new int[size + 1]; //+1 to store the #OfConflicts of initial
        int[] child = new int[size];
        int[][] possibleChildrensConf = new int[size][size];
        int bestChildFitness;
        int bestChildIndex = size; //Set to fictitious # incase cant find a better child
        int bestChildRow = size; //Set to fictitious # incase cant find a better child
        boolean solved = false;
        boolean stuck = false; //If steepest gets struck

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
            stuck = true;
            solved = true;
        }

        while (!stuck) {
            stuck = true; //If no better child is found then no solution

            //Move one Q, one position along its column
            //and find fitness for that board
            for (int col = 0; col < size; col++) {
                for (int row = 0; row < size; row++) {
                    //Find and remove currrent Q on the column
                    removeQueen(col);

                    //Place new possible Q position
                    child[col] = row;
                    board[row][col].placeQueen();
                    possibleChildrensConf[row][col] = numOfConflicts(child);

                    //Find next move
                    if (possibleChildrensConf[row][col] < bestChildFitness) {
                        bestChildFitness = possibleChildrensConf[row][col];
                        bestChildIndex = col;
                        bestChildRow = row;
                        stuck = false; //better board was found
                        stepCost++;
                    }
                }
                //Return child to current state
                removeQueen(col); //Remove last Q's position which should be at the last row
                child[col] = parent[col]; //Return it to original position
                board[parent[col]][col].placeQueen(); //Place it
            }

            //If solution found
            if (bestChildFitness == 0) {
                solved = true;
            }

            //Change parent to best child found
            if (!stuck) {
                removeQueen(bestChildIndex);
                parent[bestChildIndex] = bestChildRow;
                board[bestChildRow][bestChildIndex].placeQueen();
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