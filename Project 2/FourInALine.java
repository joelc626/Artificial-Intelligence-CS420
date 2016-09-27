/* Joel Castro
 * CS420 - Assignment 2
 *
 * 4-in-a-line. 8x8 board. First player to get 4 in a line (either a row,
 * or a column; diagonals are NOT counted) wins!
 */

import java.util.Random;
import java.util.Scanner;

public class FourInALine {

    private static int gridSize = 8;
    private static char[][] grid = new char[gridSize][gridSize];
    private static char player = 'O';
    private static char computer = 'X';
    private static String initialMove = "";
    private static int highestBeta = -1;
    private static long startTime = 0;
    private static int timeToRun = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Who goes first? X or O? Note:X=computer, O=player");
        int turn = 1;
        String whoFirst = sc.nextLine();
        System.out.println("How many second for the computer to move? example:5");
        timeToRun = sc.nextInt() * 10;
        createGrid();
        if (whoFirst.toUpperCase().charAt(0) == 'X') {
            computerWentFirst();
        }

        System.out.println(boardScore(grid));
        printGrid();

        boolean gameOver = false;

        sc.nextLine(); //catch stupid line
        while (!gameOver) {
            String loc = "";
            if (turn % 2 == 0) {
                System.out.println("Computer's turn in progress...");
            } else {
                System.out.println("Player's turn.");
            }

            if (turn % 2 == 0) {
                placeComputer(computer); // make this placeComputer(computer) when AI implemented
            } else {
                do {
                    do {
                        System.out.println("Please enter location:");
                        loc = sc.nextLine();
                    } while (loc.length() != 2); // input must be 2 characters long
                } while (!checkValidMove(loc));
                placeCharacter(loc.toUpperCase(), player);
            }
            startTime = System.nanoTime();
            printGrid();
            gameOver = checkGameOver();
            turn++;
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~COMPUTER AI HERE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static void computerWentFirst() {	// just pick one of middle slots as they have the best chance
        Random rand = new Random();
        int randRow = rand.nextInt(2);
        int randCol = rand.nextInt(2);
        grid[(gridSize / 2) - 1 + randRow][(gridSize / 2) - 1 + randCol] = 'X';
        System.out.println("Computer moved:" + (char) ((gridSize / 2) - 1 + randRow + 65) + (char) ((gridSize / 2) - 1 + randCol + 49));
    }

    //computer character is passed so they can face each other
    public static void placeComputer(char computer) {
        alphaBeta(grid, 24, -Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        grid[Character.getNumericValue(initialMove.charAt(0))][Character.getNumericValue(initialMove.charAt(1))] = 'X';
        System.out.println("Computer moved:" + (char) (initialMove.charAt(0) + 17) + (char) (initialMove.charAt(1) + 1));
        highestBeta = -1;
    }

    public static int alphaBeta(char[][] node, int depth, int alpha, int beta, boolean maximizingPlayer) {
        long elapsedTime = (System.nanoTime() - startTime) / 100000000;
        if (depth == 0 || depth == 25 || elapsedTime > timeToRun) { 	//width
            return boardScore(node);
        }
        if (maximizingPlayer) { 	// Max, ie alpha
            String s = isAdjacent();
            for (int i = 0; i < s.length() / 2 + 2; i += 2) {
                char[][] child = new char[gridSize][gridSize];

                for (int copyRow = 0; copyRow < gridSize; copyRow++) {  	// copy array
                    for (int copyCol = 0; copyCol < gridSize; copyCol++) {
                        child[copyRow][copyCol] = grid[copyRow][copyCol];
                    }
                }
                child[Character.getNumericValue(s.charAt(i))][Character.getNumericValue(s.charAt(i + 1))] = 'X';

                alpha = Math.max(alpha, alphaBeta(child, depth - 1, alpha, beta, false));
                if (beta <= alpha || boardScore(child) > highestBeta) {
                    if ((beta > highestBeta || boardScore(child) > highestBeta) && beta != Integer.MAX_VALUE) {
                        initialMove = "";
                        initialMove += Character.getNumericValue(s.charAt(i));
                        initialMove += Character.getNumericValue(s.charAt(i + 1));
                        highestBeta = highestBeta = boardScore(child);
                    }
                    break;
                }
            }
            return alpha;
        } else { 	// Min, ie beta
            String s = isAdjacent();
            for (int i = 0; i < s.length(); i += 2) {
                char[][] child = new char[gridSize][gridSize];

                for (int copyRow = 0; copyRow < gridSize; copyRow++) {
                    for (int copyCol = 0; copyCol < gridSize; copyCol++) {
                        child[copyRow][copyCol] = grid[copyRow][copyCol];
                    }
                }

                child[Character.getNumericValue(s.charAt(i))][Character.getNumericValue(s.charAt(i + 1))] = 'X';

                beta = Math.min(beta, alphaBeta(child, depth - 1, alpha, beta, true));
                if (beta <= alpha || boardScore(child) > highestBeta) {
                    if ((beta > highestBeta || boardScore(child) > highestBeta) && beta != Integer.MAX_VALUE) {
                        initialMove = "";
                        initialMove += Character.getNumericValue(s.charAt(i));
                        initialMove += Character.getNumericValue(s.charAt(i + 1));
                        highestBeta = boardScore(child);
                    }
                    break;
                }
            }
            return beta;
        }
    }

    // go through array, for current location check right to it and below it, if not empty, note location at current loc as not empty
    public static String isAdjacent() { // spots that are empty for checking and are near X or O
        String locations = "";
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == '-') { // then check adjacent
                    if (j < gridSize - 1) { // check right
                        if (grid[i][j + 1] != '-') { // then check adjacent
                            locations += i;   // add row to string of locations
                            locations += j;   // add col to string of locations    	 
                            continue;
                        }
                    }
                    if (i < gridSize - 1) { // check down
                        if (grid[i + 1][j] != '-') { // then check adjacent
                            locations += i;   // add row to string of locations
                            locations += j;   // add col to string of locations
                            continue;
                        }
                    }
                    if (j > 0) { // check left
                        if (grid[i][j - 1] != '-') { // then check adjacent
                            locations += i;   // add row to string of locations
                            locations += j;   // add col to string of locations
                            continue;
                        }
                    }
                    if (i > 0) { // check up
                        if (grid[i - 1][j] != '-') { // then check adjacent
                            locations += i;   // add row to string of locations
                            locations += j;   // add col to string of locations
                            continue;
                        }
                    }
                }

            }
        }
        return locations;
    }

    //readable form for debugging
    public static String isAdjacentDebug(String s) {
        String newStr = "";
        for (int i = 0; i < s.length(); i++) {
            if (i % 2 == 0) {
                newStr += (char) (s.charAt(i) + 17);
            } else {
                newStr += (char) (s.charAt(i) + 1);
                newStr += " ";
            }
        }
        return newStr;
    }

    // finds the score of the board if moved to a location for a-b pruning
    public static int boardScore(char[][] grid) {
        int score = 0;
        
        // Check Horizontal
        int horizontalScore;
        char character = '-';
        for (int i = 0; i < gridSize; i++) {
            horizontalScore = 0;
            character = '-';
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] != '-') {
                    if (character == grid[i][j]) {
                        horizontalScore++;
                    } else {
                        if (character != '-' && horizontalScore > 1) {
                            score += (horizontalScore) * (horizontalScore) * (horizontalScore) * (horizontalScore);
                        }
                        horizontalScore = 1;
                    }
                    character = grid[i][j];
                } else if (character != '-' && horizontalScore > 1) {
                    score += (horizontalScore) * (horizontalScore) * (horizontalScore) * (horizontalScore);
                    horizontalScore = 0;
                    character = grid[i][j];
                } else {
                    horizontalScore = 0;
                }
                if (horizontalScore >= 4) {
                    if (character != '-') {
                        score += (horizontalScore) * (horizontalScore) * (horizontalScore) * (horizontalScore);
                    }
                    horizontalScore = 0;
                }
            }
        }

        // Check Vertical
        int verticalScore;
        character = '-';
        for (int i = 0; i < gridSize; i++) {
            verticalScore = 0;
            character = '-';
            for (int j = 0; j < gridSize; j++) {
                if (grid[j][i] != '-') {
                    if (character == grid[j][i]) {
                        verticalScore++;
                    } else {
                        if (character != '-' && verticalScore > 1) {
                            score += (verticalScore) * (verticalScore) * (verticalScore) * (verticalScore);
                        }
                        verticalScore = 1;
                    }
                    character = grid[j][i];
                } else if (character != '-' && verticalScore > 1) {
                    score += (verticalScore) * (verticalScore) * (verticalScore) * (verticalScore);
                    verticalScore = 0;
                    character = grid[j][i];
                } else {
                    verticalScore = 0;
                }
                if (verticalScore >= 4) {
                    if (character != '-') {
                        score += (verticalScore) * (verticalScore) * (verticalScore) * (verticalScore);
                    }
                    verticalScore = 0;
                }
            }
        }
        return score;
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~COMPUTER AI Ends HERE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void createGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = '-';
            }
        }
    }

    public static void printGrid() {
        System.out.print("  1 2 3 4 5 6 7 8");
        for (int i = 0; i < gridSize; i++) {
            System.out.print("\n" + (char) (i + 65) + " ");	// 65 is the ASCII value for 'A'
            for (int j = 0; j < gridSize; j++) {
                System.out.print(grid[i][j] + " ");
            }
        }
        System.out.println();
    }

    // Manually place character, no AI here
    public static void placeCharacter(String s, char character) {
        int row = (int) s.charAt(0) - 65; 	// 65 ascii is 'A'
        int col = (int) s.charAt(1) - 49; 	// 49 ascii is '1'
        if (s.length() != 2) {
            System.out.println("OUT OF BOUNDS");
            return;
        }
        if (row >= 0 && row <= 8) {  // if first char in string is >= A and <= H...
            if (col >= 0 && col < 8) { // if second char is >= 0 and < 8
                grid[row][col] = character;
            } else {
                System.out.println("OUT OF BOUNDS");
            }
        } else {
            System.out.println("OUT OF BOUNDS");
        }
    }

    public static boolean checkGameOver() {
        // Check Horizontal
        int horizontalScore;
        char character = '-';
        for (int i = 0; i < gridSize; i++) {
            horizontalScore = 0;
            character = '-';
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] != '-') {
                    if (character == grid[i][j]) {
                        horizontalScore++;
                    } else {
                        horizontalScore = 1;
                    }
                    character = grid[i][j];
                } else {
                    horizontalScore = 0;
                }
                if (horizontalScore >= 4) {
                    System.out.println("GAME OVER, " + character + " wins!");
                    return true;
                }
            }
        }
        // Check Vertical
        int verticalScore;
        character = '-';
        for (int i = 0; i < gridSize; i++) {
            verticalScore = 0;
            character = '-';
            for (int j = 0; j < gridSize; j++) {
                if (grid[j][i] != '-') {
                    if (character == grid[j][i]) {
                        verticalScore++;
                    } else {
                        verticalScore = 1;
                    }
                    character = grid[j][i];
                } else {
                    verticalScore = 0;
                }
                if (verticalScore >= 4) {
                    System.out.println("GAME OVER, " + character + " wins!");
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkValidMove(String loc) {
        loc = loc.toUpperCase();
        for (int i = 0; i < gridSize; i++) {
            if (loc.charAt(0) == (char) (i + 65)) { // check letter
                for (int j = 0; j < gridSize; j++) {
                    if (((int) loc.charAt(1) - 48) == (j + 1)) { // check #
                        return true;
                    }
                }
            }
        }
        return false;
    }
}