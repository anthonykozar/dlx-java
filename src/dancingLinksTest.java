package DLX;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class dancingLinksTest {

    private byte[][] rightDownLinks(columnObject root, byte[][] inMatrix) {
        byte[][] matrix = new byte[inMatrix.length][inMatrix[0].length];

        columnObject j = root.right;
        columnObject n;
        while (!j.equals(root)) {
            n = j.down;
            while (!n.equals(j)) {
                matrix[n.i][n.j] = 1;
                n = n.down;
            }
            j = j.right;
        }

        return matrix;
    }

    private byte[][] leftUpLinks(columnObject root, byte[][] inMatrix) {
        byte[][] outMatrix = new byte[inMatrix.length][inMatrix[0].length];

        columnObject j = root.right;
        columnObject n;
        while (!j.equals(root)) {
            n = j.down;
            while (!n.equals(j)) {
                outMatrix[n.i][n.j] = 1;
                n = n.down;
            }
            j = j.right;
        }

        return outMatrix;
    }

    private boolean testUpDownAndListHeaderHorizontalLinks(columnObject root, byte[][] m) {
        boolean a = Arrays.deepEquals(m, leftUpLinks(root, m));
        boolean b = Arrays.deepEquals(m, rightDownLinks(root, m));

        return a && b;
    }

    private boolean testRightHorizontalLinks(columnObject root, byte[][] m) {
        // iterate over every item in circularly linked list
        // for every columnObject (not ListHeader) element sum row, check that sum from matrix is the same
        columnObject j = root.right;
        columnObject n;
        while (!j.equals(root)) {
            n = j.down;
            while (!n.equals(j)) {

                columnObject k = n.right; // row iterator
                int sum = 1;
                while (!k.equals(n)) {
                    sum++;
                    k = k.right;
                }

                byte[] row = m[n.i];
                int matrixSum = IntStream.range(0, row.length).filter(a -> row[a] == 1).toArray().length;

                if (sum != matrixSum) return false;

                n = n.down;
            }
            j = j.right;
        }
        return true;
    }

    private boolean testLeftHorizontalLinks(columnObject root, byte[][] m) {
        // iterate over every item in circularly linked list
        // for every columnObject (not ListHeader) element sum row, check that sum from matrix is the same
        columnObject j = root.right;
        columnObject n;
        while (!j.equals(root)) {
            n = j.down;
            while (!n.equals(j)) {

                columnObject k = n.left; // row iterator
                int sum = 1;
                while (!k.equals(n)) {
                    sum++;
                    k = k.left;
                }

                byte[] row = m[n.i];
                int matrixSum = IntStream.range(0, row.length).filter(a -> row[a] == 1).toArray().length;

                if (sum != matrixSum) return false;

                n = n.down;
            }
            j = j.right;
        }
        return true;
    }

    private boolean testColumnObjectHorizontalLinks(columnObject root, byte[][] m) {
        boolean a = testRightHorizontalLinks(root, m);
        boolean b = testLeftHorizontalLinks(root, m);

        return a && b;
    }

    private boolean testColumnSizeField(columnObject root) {
        columnObject j = root.right;
        columnObject n;
        while (!j.equals(root)) {
            n = j.down;
            int listHeaderSize = n.columnHead.size;
            int sum = 0;
            while (!n.equals(j)) {
                sum++;
                n = n.down;
            }

            if (listHeaderSize != sum) return false;

            j = j.right;
        }
        return true;
    }

    @Test
    public void testConstructor() throws Exception {
        // Matrix from knuths paper
        byte[][] testMatrix = new byte[][]{
                {0, 0, 1, 0, 1, 1, 0},
                {1, 0, 0, 1, 0, 0, 1},
                {0, 1, 1, 0, 0, 1, 0},
                {1, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1, 0, 1}
        };

        Object[] listHeaders = new String[]{"0", "1", "2", "3", "4", "5", "6"};

        listHeader root = dancingLinks.createCircularlyLinkedLists(testMatrix, listHeaders);


        Assert.assertTrue(testUpDownAndListHeaderHorizontalLinks(root, testMatrix));
        Assert.assertTrue(testColumnObjectHorizontalLinks(root, testMatrix));
        Assert.assertTrue(testColumnSizeField(root));
    }

    @Test
    public void testCreateSecondaryColumn() throws Exception {

        // Matrix from knuths paper
        byte[][] testMatrix = new byte[][]{
                {0, 0, 1, 0, 1, 1, 0},
                {1, 0, 0, 1, 0, 0, 1},
                {0, 1, 1, 0, 0, 1, 0},
                {1, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1, 0, 1}
        };

        Object[] listHeaders = new String[]{"0", "1", "2", "3", "4", "5", "6"};

        // what output from leftUpLinks and rightDownLinks should look like
        byte[][] matrixSecondaryColumn = new byte[][]{
                {0, 0, 1, 0, 1, 1, 0},
                {1, 0, 0, 1, 0, 0, 0},
                {0, 1, 1, 0, 0, 1, 0},
                {1, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 0}
        };

        listHeader root = dancingLinks.createCircularlyLinkedLists(testMatrix, listHeaders);

        dancingLinks.createSecondaryColumn(root);

        Assert.assertTrue(testUpDownAndListHeaderHorizontalLinks(root, matrixSecondaryColumn));
        Assert.assertTrue(testColumnSizeField(root));
        // horizontal column object links should still be as in original matrix
        Assert.assertTrue(testColumnObjectHorizontalLinks(root, testMatrix));
    }

    @Test
    public void testCoverColumn() throws Exception {
        // Matrix from knuths paper
        byte[][] testMatrix = new byte[][]{
                {0, 0, 1, 0, 1, 1, 0},
                {1, 0, 0, 1, 0, 0, 1},
                {0, 1, 1, 0, 0, 1, 0},
                {1, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1, 0, 1}
        };

        Object[] listHeaders = new String[]{"0", "1", "2", "3", "4", "5", "6"};

        // what output from leftUpLinks and rightDownLinks should look like
        byte[][] matrixCover = new byte[][]{
                {0, 0, 1, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0}, // <--
                {0, 1, 1, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0}, // <--
                {0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1, 0, 1}
        };
        //       ^
        //       |
        //     root.right

        listHeader root = dancingLinks.createCircularlyLinkedLists(testMatrix, listHeaders);

        dancingLinks.coverColumn(root.right);

        Assert.assertTrue(testUpDownAndListHeaderHorizontalLinks(root, matrixCover));
        Assert.assertTrue(testColumnObjectHorizontalLinks(root, matrixCover));
        Assert.assertTrue(testColumnSizeField(root));

        // cover another column

        // what output from leftUpLinks and rightDownLinks should look like
        byte[][] matrixCover2 = new byte[][]{
                {0, 0, 1, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0} // <--
        };
        //          ^     ^
        //          |     |
        //  root.right   root.right.right.right

        dancingLinks.coverColumn(root.right.right.right);

        Assert.assertTrue(testUpDownAndListHeaderHorizontalLinks(root, matrixCover2));
        Assert.assertTrue(testColumnObjectHorizontalLinks(root, matrixCover2));
        Assert.assertTrue(testColumnSizeField(root));

        // cover all

        // what output from leftUpLinks and rightDownLinks should look like
        byte[][] matrixCover3 = new byte[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };

        while (!root.right.equals(root)) dancingLinks.coverColumn(root.right);

        Assert.assertTrue(testUpDownAndListHeaderHorizontalLinks(root, matrixCover3));
        Assert.assertTrue(testColumnObjectHorizontalLinks(root, matrixCover3));
        Assert.assertTrue(testColumnSizeField(root));
    }

    @Test
    public void testUncoverColumn() throws Exception {

        Random rand = new Random();

        byte[][] testMatrix = new byte[][]{
                {0, 0, 1, 0, 1, 1, 0},
                {1, 0, 0, 1, 0, 0, 1},
                {0, 1, 1, 0, 0, 1, 0},
                {1, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1, 0, 1}
        };

        Object[] listHeaders = new String[]{"0", "1", "2", "3", "4", "5", "6"};


        listHeader root = dancingLinks.createCircularlyLinkedLists(testMatrix, listHeaders);

        testRightHorizontalLinks(root, testMatrix);

        // cover one column
        columnObject c = root.right;
        dancingLinks.coverColumn(c);
        dancingLinks.uncoverColumn(c);

        Assert.assertTrue(testUpDownAndListHeaderHorizontalLinks(root, testMatrix));
        Assert.assertTrue(testColumnObjectHorizontalLinks(root, testMatrix));
        Assert.assertTrue(testColumnSizeField(root));

        // cover two columns
        columnObject c1 = root.right.right.right;
        dancingLinks.coverColumn(c);
        dancingLinks.coverColumn(c1);
        dancingLinks.uncoverColumn(c);
        dancingLinks.uncoverColumn(c1);

        Assert.assertTrue(testUpDownAndListHeaderHorizontalLinks(root, testMatrix));
        Assert.assertTrue(testColumnObjectHorizontalLinks(root, testMatrix));
        Assert.assertTrue(testColumnSizeField(root));

        columnObject[] coveredColumns = new columnObject[listHeaders.length];
        int i = 0;
        columnObject n;
        // cover all the columns randomly
        while (!root.right.equals(root)) {
            n = root;
            int r = rand.nextInt(10);
            int j = 0;
            boolean o = rand.nextBoolean();
            while (j < r) {
                if (o) n = n.right;
                else n = n.left;
                j++;
            }
            if (!n.equals(root)) {
                dancingLinks.coverColumn(n);
                coveredColumns[i] = n;
                i++;
            }
        }

        // uncover in reverse
        for (int k = coveredColumns.length - 1; k > -1; k--) {
            if (coveredColumns[k] != null) {
                dancingLinks.uncoverColumn(coveredColumns[k]);
            }
        }

        Assert.assertTrue(testUpDownAndListHeaderHorizontalLinks(root, testMatrix));
        Assert.assertTrue(testColumnObjectHorizontalLinks(root, testMatrix));
        Assert.assertTrue(testColumnSizeField(root));


    }

    @Test
    public void testDlx() throws Exception {
        // This test sorta sucks. I'm not sure how to fix it at the moment.

        byte[][] testMatrix = new byte[][]{
                {0, 0, 1, 0, 1, 1, 0},
                {1, 0, 0, 1, 0, 0, 1},
                {0, 1, 1, 0, 0, 1, 0},
                {1, 0, 0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1, 0, 1},
                {0, 1, 0, 0, 0, 0, 1} // duplicate row to test that all solutions are actually produced
        };

        Object[] listHeaders = new String[]{"0", "1", "2", "3", "4", "5", "6"};

        ArrayList<Object[]> sol1 = dancingLinks.firstSolution(testMatrix, listHeaders, 0);

        ArrayList<ArrayList<Object[]>> allSol = dancingLinks.allSolutions(testMatrix, listHeaders, 0);

        for (ArrayList<Object[]> i : allSol) {
            System.out.println("sol ========");
            for (Object[] oa : i) {
                System.out.println(Arrays.deepToString(oa));
            }

        }

        Assert.assertTrue(allSol.size() == 2);
        Assert.assertTrue(sol1.size() == 3);

    }

    @Test
    public void testDlxWithSecondaryColumn() throws Exception {

        byte[][] testMatrix1 = new byte[][]{
                {0, 0, 1, 0, 1, 1, 0, 0},
                {1, 0, 0, 1, 0, 0, 1, 0},
                {0, 1, 1, 0, 0, 1, 0, 0},
                {1, 0, 0, 1, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 1, 1, 0, 1, 0},
                {0, 1, 0, 0, 0, 0, 1, 0} // duplicate row to test that all solutions are actually produced
        };

        Object[] listHeaders1 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7"};

        ArrayList<ArrayList<Object[]>> allSol1 = dancingLinks.allSolutions(testMatrix1, listHeaders1, 1);

        for (ArrayList<Object[]> i : allSol1) {
            System.out.println("sol ========");
            for (Object[] oa : i) {
                System.out.println(Arrays.deepToString(oa));
            }

        }

        Assert.assertTrue(allSol1.size() == 2);
    }

    public void printArray(byte[][] m) {
        for (byte[] row : m) {
            System.out.println(Arrays.toString(row));
        }
    }
}