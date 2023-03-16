package DLX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class DLXMain {
    private int         numColumns;
    private int         numRows;
    private int         numSecondaryCols;
    private String[]    columnHeaders;
    private byte[][]    rowMatrix;
    
    private ArrayList<ArrayList<Object[]>> solutions;
    
    public boolean readInputData(Scanner in) {
        numColumns = in.nextInt();
        numSecondaryCols = in.nextInt();
        numRows = in.nextInt();
        
        columnHeaders = new String[numColumns] ;
        for (int col=0; col < numColumns; ++col) {
            try {
                columnHeaders[col] = in.next();
            }
            catch (NoSuchElementException e) {
                System.err.println("Ran out of input while reading column headers.");
                return false;
            }
            catch (IllegalStateException e) {
                System.err.println("Scanner error while reading column headers.");
                return false;
            }
        }
        
        rowMatrix = new byte[numRows][numColumns];
        for (int row=0; row < numRows; ++row) {
            for (int col=0; col < numColumns; ++col) {
                try {
                    rowMatrix[row][col] = in.nextByte();
                }
                catch (NoSuchElementException e) {
                    System.err.println("Ran out of input while reading row data.");
                    return false;
                }
                catch (IllegalStateException e) {
                    System.err.println("Scanner error while reading row data.");
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public int numOfSolutions() {
        if (solutions != null) {
            return solutions.size();
        }
        else return 0;
    }
    public void findFirstSolution() {
        solutions = new ArrayList<ArrayList<Object[]>>();
        solutions.add(dancingLinks.firstSolution(rowMatrix, columnHeaders, numSecondaryCols));
    }
    
    public void findAllSolutions() {
        solutions = dancingLinks.allSolutions(rowMatrix, columnHeaders, numSecondaryCols);
    }
    
    public void printSolutions() {
        for (ArrayList<Object[]> sol : solutions) {
            System.out.println("sol ========");
            for (Object[] oa : sol) {
                System.out.println(Arrays.deepToString(oa));
            }
        }
    }

/*    public void printArray(byte[][] m) {
        for (byte[] row : m) {
            System.out.println(Arrays.toString(row));
        }
    }
*/    
    static public void main(String[] args) {
        DLXMain problem = new DLXMain();
        Scanner input = new Scanner(System.in);
        
        if (problem.readInputData(input)) {
            int numsol;
            problem.findAllSolutions();
            numsol = problem.numOfSolutions();
            System.out.printf("Number of solutions found: %d\n\n", numsol);
            if (numsol > 0) {
                problem.printSolutions();
            }
        }
        
        return;
    }
}
