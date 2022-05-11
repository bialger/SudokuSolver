public class Main {

    public static void main(String[] args) {
        int[][] arr = {{5, 8, 0, 2, 0, 0, 4, 7, 0},
                {0, 2, 0, 0, 0, 0, 0, 3, 0},
                {0, 3, 0, 0, 5, 4, 0, 0, 0},
                {0, 0, 0, 5, 6, 0, 0, 0, 0},
                {0, 0, 7, 0, 3, 0, 9, 0, 0},
                {0, 0, 0, 0, 9, 1, 0, 0, 0},
                {0, 0, 0, 8, 2, 0, 0, 6, 0},
                {0, 7, 0, 0, 0, 0, 0, 8, 0},
                {0, 9, 4, 0, 0, 6, 0, 1, 5}};
        print_initial(arr, arr.length);
        if (sudoku(arr)) {
            System.out.println("AFTER SOLVING : ");
            print(arr, arr.length);
        }
        else System.out.println("UNSOLVABLE");
    }

    public static boolean sudoku(int[][] grid) {
        int[] ra = Unassigned(grid);
        if (ra[0] == -1) return true;

        int row = ra[0];
        int col = ra[1];

        for (int num = 1; num <= 9; num++) {
            if (isSafe(grid, row, col, num)) {
                grid[row][col] = num;
                boolean check = sudoku(grid);
                if (check) return true;
                grid[row][col] = 0;
            }
        }

        return false;
    }

    public static int[] Unassigned(int[][] arr) {
        int[] ra = new int[2];
        ra[0] = -1;
        ra[1] = -1;

        for (int row = 0; row < arr.length; row++) {
            for (int col = 0; col < arr.length; col++) {
                if (arr[row][col] == 0) {
                    ra[0] = row;
                    ra[1] = col;
                    return ra;
                }
            }
        }

        return ra;
    }

    public static boolean usedInRow(int[][] grid, int row, int num) {

        for (int i = 0; i < grid.length; i++)
            if (grid[row][i] == num) return true;

        return false;
    }

    public static boolean usedInCol(int[][] grid, int col, int num) {

        for (int[] ints : grid) {
            if (ints[col] == num) {
                return true;
            }
        }

        return false;
    }

    public static boolean usedInBox(int[][] grid, int row1Start, int col1Start, int num) {

        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                if (grid[row + row1Start][col + col1Start] == num) return true;

        return false;
    }

    public static boolean isSafe(int[][] grid, int row, int col, int num) {
        return (!usedInCol(grid, col, num) && !usedInRow(grid, row, num) && !usedInBox(grid, row - row % 3,
                col - col % 3, num));
    }

    public static void print(int[][] arr, int n) {

        for (int i = 0; i < n; i++) {
            if (i % 3 == 0 && i != 0) System.out.println("----------|---------|----------");

            for (int j = 0; j < n; j++) {
                if (j % 3 == 0) System.out.print("|");
                System.out.print(" " + arr[i][j] + " ");
            }

            System.out.println();
        }
    }

    public static void print_initial(int[][] arr, int n) {

        for (int i = 0; i < n; i++) {
            if (i % 3 == 0 && i != 0) System.out.println("----------|---------|----------");

            for (int j = 0; j < n; j++) {
                if (j % 3 == 0) System.out.print("|");
                if (arr[i][j] == 0) System.out.print(" " + "-" + " ");
                else System.out.print(" " + arr[i][j] + " ");
            }

            System.out.println();
        }
    }
}
