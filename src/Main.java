import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
//TODO: add function to quit.
public class Main {
    public static void main(String[] args) throws Exception {
        clearConsole();
        Scanner console = new Scanner(System.in);

        //Prompt for Single Player or Multiplayer.
        boolean singlePlayer = true;
        while (true) {
            try {
                clearConsole();
                System.out.println("Do you want to play Single Player (1) or Multiplayer (2)?");
                switch (console.nextInt()){
                    case 1: singlePlayer = true; break;
                    case 2: singlePlayer = false; break;
                    default: continue;
                } 
            } catch (InputMismatchException err) {
                console.next();
                continue;
            }
            break;
        }

        //Prompt for turn order (assuming Single Player was selected).
        int playerChoice = 1;
        while (singlePlayer) {
            try {
                clearConsole();
                System.out.println("Do you want to play as X (1) or O (2)?");
                switch (console.nextInt()){
                    case 1: playerChoice = 1; break;
                    case 2: playerChoice = 2; break;
                    default: continue;
                } 
            } catch (InputMismatchException err) {
                console.next();
                continue;
            }
            break;
        }

        //Prompt for board size.
        int boardSize = 1;
        while (true) {
            try {
                clearConsole();
                System.out.println("Enter desired board size.");
                boardSize = console.nextInt(); 
            } catch (InputMismatchException err) {
                console.next();
                continue;
            }
            break;
        }

        //in this board, 0 will be null space, 1 will be X, 2 will be O.
        int[][] board = new int[boardSize][boardSize];

        //if turn = 1, X will play, if turn = 2, O will play.
        //userTurn can be replaced by a boolean because it's only ever 1 or 2. I'm just lazy.
        byte userTurn = 1;
        int turnCount = 1;
        boolean invalidLocation = false;
        //main gameplay loop.
        while(true) {
            clearConsole();
            printBoard(board);

            //catching people who can't follow directions.
            if (invalidLocation) {
                System.out.println("Please enter a valid move");
                invalidLocation = false;
            }

            int selRow = 1;
            int selCol = 1;

            //taking the AI's turn.
            if (singlePlayer && userTurn != playerChoice){
                while(true){
                    Random rand = new Random();
                    selRow = rand.nextInt(0, board.length);
                    selCol = rand.nextInt(0, board.length);
                    if (checkValid(board, selRow, selCol)) {
                        board[selRow][selCol] = userTurn;
                        break;
                    }
                }
            }
            // //taking players turn.
            else {
                System.out.printf("Player %d, enter your move %n", userTurn);
                try {
                    selRow = console.nextInt() - 1;
                    selCol = console.nextInt() - 1;
                } catch (InputMismatchException err) {
                    console.next();
                    invalidLocation = true;
                    continue;
                }
                if (checkValid(board, selRow, selCol))
                    board[selRow][selCol] = userTurn;
                else {
                    invalidLocation = true;
                    continue;
                }
            }

            //checking win condition and printing win screen
            if (checkWinner(board, userTurn, selRow, selCol)) {
                clearConsole();
                printBoard(board);
                System.out.printf("Player %d Wins !!%n", userTurn);
                break;
            }
            else if (turnCount == Math.pow(board.length, 2)) {
                clearConsole();
                printBoard(board);
                System.out.println("Tie game. Player 1 and Player 2 lose.");
                break;
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
            turnCount++;
        }
        console.close();
    }

    //method for clearing the console.
    public static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    //method for printing the board
    public static void printBoard(int[][] board){
        System.out.println("TIC TAC TOE");
        for (int i = 0, row = 0; i < (2*board.length) - 1; i++){
            if (i % 2 == 0){
                for (int j = 0; j < board.length; j++){
                    if (board[row][j] == 0) System.out.print("   ");
                    else if (board[row][j] == 1) System.out.print(" X ");
                    else System.out.print(" O ");

                    //for printing the vertical hashes.
                    if (j != board.length - 1) System.out.print("|");
                }
                row++;
            }
            else {
                //for printing the horizontal hashes.
                for (int j = 1; j < (3*board.length) + (board.length); j++) {
                    if (!(j % 4 == 0)) System.out.print("-");
                    else System.out.print("+");
                }
            }
            System.out.println();
        }
    }

    //method to check if a move is valid
    public static boolean checkValid(int[][] board, int selRow, int selCol){
        try {
            if (board[selRow][selCol] > 0)
                return false;
        } catch (ArrayIndexOutOfBoundsException err) {
            return false;
        }
        return true;
    }

    //checking for winner along selRow and selCol. 
    //Left diagonal only checks if selRow and selCol are equal.
    //Right diagonal only checks if selRow + selCol equals the board length - 1. This is an interesting property im sure has been found before me.
    public static boolean checkWinner(int[][] board, int target, int selRow, int selCol){
        //checks rows for X win.
        for (int j = 0; j < board.length && board[selRow][j] == target; j++)
            if (j == board.length-1) return true;
        //check columns for win.
        for (int i = 0; i < board.length && board[i][selCol] == target; i++)
            if (i == board.length-1) return true;
        //check left diagonal for win.
        if (selRow == selCol){
            for (int i = 0; i < board.length; i++){
                if (board[i][i] != target) break;
                else if (i == board.length-1) return true;
            }
        }
        //check right diagonal for win.
        if (selRow + selCol == board.length - 1){
            for (int i = 0, j = board.length-1; i < board.length; i++, j--){
                if (board[i][j] != target) break;
                else if (i == board.length-1) return true;    
            }
        }
        return false;
    }
}