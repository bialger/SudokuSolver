import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {

    int[][] arr = new int[9][9];

    JTextField[] numberFields = new JTextField[81];

    Main () {
        JFrame sudokuFrame = new JFrame ("Sudoku Solver");
        sudokuFrame.setLayout (new GridLayout (10,9));
        sudokuFrame.setSize (900, 1000);
        sudokuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sudokuFrame.setResizable(false);
        JButton buttonSolve = new JButton("Solve");
        JButton buttonReset = new JButton("Reset");
        buttonReset.addActionListener(new ListenerReset());
        buttonSolve.addActionListener(new ListenerSolve());
        buttonReset.setFont(new Font("Serif", Font.PLAIN ,28));
        buttonSolve.setFont(new Font("Serif", Font.PLAIN ,28));
        JLabel[] empty = new JLabel[7];

        for (int i = 0; i < numberFields.length; i++) {
            numberFields[i] = new JTextField (1);
            int bottomBorder = (i / 9 == 2 || i / 9 == 5) ? 2 : 0;
            int rightBorder = (i % 9 == 2 || i % 9 == 5) ? 2 : 0;
            bottomBorder = (i / 9 == 8) ? 1 : bottomBorder;
            rightBorder = (i % 9 == 8) ? 1 : rightBorder;
            numberFields[i].setBorder(BorderFactory.createMatteBorder(1, 1,
                    bottomBorder, rightBorder, Color.BLACK));
            numberFields[i].setFont(new Font("Serif", Font.PLAIN ,48));
            numberFields[i].setHorizontalAlignment(JTextField.CENTER);
            if ((i % 9 < 3 || i % 9 > 5) && (i / 9 < 3 || i / 9 > 5) || (i % 9 > 2 && i % 9 < 6) && (i / 9 > 2 &&
                    i / 9 < 6)) numberFields[i].setBackground(new java.awt.Color(130, 170, 200));
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
                if (!str.equals("")) {
                    if (str.charAt(0) >= '0' && str.charAt(0) <= '9' && str.length() == 1)
                        arr[i / 9][i % 9] = Integer.parseInt(numberFields[i].getText());
                    else {
                        arr[i / 9][i % 9] = 0;
                        numberFields[i].setText("");
                    }
                }
            }

            print_initial(arr, arr.length);
            if (sudoku(arr)) {

                for (int i = 0; i < numberFields.length; i++) {
                    numberFields[i].setText(String.valueOf(arr[i / 9][i % 9]));
                }

                System.out.println("AFTER SOLVING : ");
                print(arr, arr.length);
                new PopUp("Solved!").show();
            }
            else {
                System.out.println("UNSOLVABLE");
                new PopUp("This sudoku can`t be solved!").show();
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
