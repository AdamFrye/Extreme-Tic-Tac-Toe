import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws Exception {
        clearConsole();
        Scanner console = new Scanner(System.in);

        //int this board, 0 will be null space, 1 will be X, 2 will be O.
        int[][] board = new int[3][3];

        //Main game loop
        boolean gameOver = false;
        //if turn = 1, X will play, if turn = 2, O will play.
        int userTurn = 1;
        while(!gameOver){
            printBoard(board);

            System.out.printf("Player %d, please enter your move %n", userTurn);

            //getting users placement.
            int selRow = console.nextInt() - 1;
            int selCol = console.nextInt() - 1;
            board[selRow][selCol] = userTurn;
            
            clearConsole();
            if (checkWinner(board, userTurn)){
                printBoard(board);
                System.out.printf("Player %d Wins !!%n", userTurn);  
                System.exit(0);
            }
            
            //logic for switching turns.
            switch (userTurn) {
                case 1: 
                    userTurn = 2;
                    break;
                case 2: 
                    userTurn = 1;
                    break;
            }
        }
    }

    public static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printBoard(int[][] board){
        System.out.println("TIC TAC TOE");
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (board[i][j] == 0) System.out.print("   ");
                else if (board[i][j] == 1) System.out.print(" X ");
                else System.out.print(" O ");
            }
            System.out.println();
        }
    }

    //long way of checking for winner. Could be done with recursion but idk which is faster.
    public static boolean checkWinner(int[][] board, int target){
        //checks rows for X win.
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (board[i][j] != target) break;
                else if (j == 2) return true;
            }
        }
        //check columns for win.
        for (int j = 0; j < 3; j++){
            for (int i = 0; i < 3; i++){
                if (board[i][j] != target) break;
                else if (i == 2) return true;
            }
        }
        //check left diagonal for win.
        for (int i = 0; i < 3; i++){
            if (board[i][i] != target) break;
            else if (i == 2) return true;
        }
        //check right diagonal for win.
        for (int i = 0, j = 2; i < 3; i++, j--){
            if (board[i][j] != target) break;
            else if (i == 2) return true;
        }

        return false;
    }
}
