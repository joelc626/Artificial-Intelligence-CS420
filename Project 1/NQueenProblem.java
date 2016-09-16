/* Joel Castro
 * CS420 - Project 1
 *
 * Implement both the Steepest Hill Climbing and MIN-CONFLICTS
 * algorithm to solve the 16-queen problem
 */

package nqueenproblem;

public class NQueenProblem {

    public static void main(String[] args) {
        int n = 16; //For an nxn
        float instances = 200;
        float solved = 0;
        int unSolved = 0;
        int totalStepCost = 0;
        long start, finish, totalTime = 0;

        steepHillClimb steepHC;
        minConflicts minConf = new minConflicts(n);

        //Steepest HC
        for (int runs = 0; runs < instances; runs++) {
            steepHC = new steepHillClimb(n);
            start = System.currentTimeMillis();
            if (steepHC.steepHillClimb()) {
                solved++;
                finish = System.currentTimeMillis();
                totalTime += finish - start;
                totalStepCost += steepHC.getstepCost();
            } else {
                unSolved++;
            }
        }

        System.out.println("For " + (int) instances + " instances, with n = " + n + ":");
        System.out.println("Steepest HC solved: " + (int)solved);
        System.out.println("Steepest HC unsolved: " + unSolved);
        System.out.printf("Percentage of Steepest HC solved: %.1f%%\n",
                (100 * solved) / instances);
        System.out.printf("Average step cost for solved puzzles: %.2f\n", totalStepCost / solved);
        System.out.printf("Average running time for solved puzzles: %.3fms\n", totalTime / solved);

        solved = 0;
        unSolved = 0;
        
        //MIN-CONFLICTS
        for (int runs = 0; runs < instances; runs++) {
            start = System.currentTimeMillis();
            if (minConf.minConflicts()) {
                solved++;
                finish = System.currentTimeMillis();
                totalTime += finish - start;
                totalStepCost += minConf.getstepCost();
            } else {
                unSolved++;
            }
        }
        
        System.out.println("\nFor " + (int) instances + " instances, with n = " + n + ":");
        System.out.println("MIN-CONFLICTS solved: " + (int)solved);
        System.out.println("MIN-CONFLICTS unsolved: " + unSolved);
        System.out.printf("Percentage of MIN-CONFLICTS solved: %.1f%%\n",
                (100 * solved) / instances);
    }
}