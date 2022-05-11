import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {

    int[][] arr = {{5, 8, 0, 2, 0, 0, 4, 7, 0},
            {0, 2, 0, 0, 0, 0, 0, 3, 0},
            {0, 3, 0, 0, 5, 4, 0, 0, 0},
            {0, 0, 0, 5, 6, 0, 0, 0, 0},
            {0, 0, 7, 0, 3, 0, 9, 0, 0},
            {0, 0, 0, 0, 9, 1, 0, 0, 0},
            {0, 0, 0, 8, 2, 0, 0, 6, 0},
            {0, 7, 0, 0, 0, 0, 0, 8, 0},
            {0, 9, 4, 0, 0, 6, 0, 1, 5}};

    JTextField[] numberFields = new JTextField[81];

    Main () {
        JFrame sudokuFrame = new JFrame ("Sudoku Solver");
        sudokuFrame.setLayout (new GridLayout (10,9));
        sudokuFrame.setSize (900, 1000);
        sudokuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton buttonSolve = new JButton("Solve");
        JButton buttonReset = new JButton("Reset");
        buttonReset.addActionListener(new ListenerReset());
        buttonSolve.addActionListener(new ListenerSolve());
        JLabel[] empty = new JLabel[7];

        for (int i = 0; i < numberFields.length; i++) {
            numberFields[i] = new JTextField (1);
            sudokuFrame.add(numberFields[i]);
        }

        sudokuFrame.add(buttonReset);

        for (int i = 0; i < empty.length; i++) {
            empty[i] = new JLabel ("");
            sudokuFrame.add(empty[i]);
        }

        sudokuFrame.add(buttonSolve);
        sudokuFrame.setVisible(true);
    }

    class ListenerReset implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < numberFields.length; i++) {
                numberFields[i].setText("");
                arr[i / 9][i % 9] = 0;
            }
        }
    }

    class ListenerSolve implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < numberFields.length; i++) {
                String str = numberFields[i].getText();
                if (!str.equals("")) arr[i / 9][i % 9] = Integer.parseInt(numberFields[i].getText());
                else arr[i / 9][i % 9] = 0;
            }
            print_initial(arr, arr.length);
            if (sudoku(arr)) {
                System.out.println("AFTER SOLVING : ");

                for (int i = 0; i < numberFields.length; i++) {
                    numberFields[i].setText(String.valueOf(arr[i / 9][i % 9]));
                }

                PopUp okMessage = new PopUp("Solved!");
                okMessage.show();
                print(arr, arr.length);
            }
            else {
                System.out.println("UNSOLVABLE");
                PopUp badMessage = new PopUp("This sudoku can`t be solved!");
                badMessage.show();
            }
        }
    }

    record PopUp(String text) {
        public void show() {
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, text);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public static boolean sudoku(int[][] grid) {
        boolean proceed = false;

        for (int i = 0; i < grid.length; i++) {
            if (grid[i / 9][i % 9] != 0) {
                proceed = true;
                break;
            }
        }

        if (proceed){
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
