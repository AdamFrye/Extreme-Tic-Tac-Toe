import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
//TODO: move the checking if a move is valid into a method
//TODO: make a single player option with a guy who makes random moves.
//TODO: improve the win condition method by only checking row and colum of selRow and selCol.
//TODO: add function to quit.
public class Main {
    public static void main(String[] args) throws Exception {
        clearConsole();
        Scanner console = new Scanner(System.in);

        //prompting for game mode (single player or multiplayer)
        //TODO: add error handling.
        System.out.println("Do you want to play single player or multiplayer?");
        boolean singlePlayer;
        switch (console.nextInt()){
            case 1: singlePlayer = true; break;
            case 2: singlePlayer = false; break;
            default: singlePlayer = true;
        }

        //letting player choose which turn they want to take if single player is true
        //TODO: add error handling.
        int playerChoice = 1;
        if (singlePlayer){
            System.out.println("Do you want to play as X (1) or O (2)");
            playerChoice = console.nextInt();
        }

        //getting valid board size.
        //TODO: improve error handling.
        System.out.println("Enter desired board size.");
        boolean validInput = false;
        byte boardSize = 1;
        //error handling for boneheads.
        while(!validInput) {
            try {
                boardSize = console.nextByte();
                validInput = true;
            } catch (InputMismatchException err){
                clearConsole();
                System.out.println("Enter a VALID board size.");
                console.next();
            }
        }
        //in this board, 0 will be null space, 1 will be X, 2 will be O.
        int[][] board = new int[boardSize][boardSize];

        //if turn = 1, X will play, if turn = 2, O will play.
        byte userTurn = 1;
        int turnCount = 1;
        boolean invalidLocation = false;
        //main gameplay loop.
        while(true){
            clearConsole();
            printBoard(board);

            int selRow = 0;
            int selCol = 0;
            //taking the AI's turn.
            if (singlePlayer && userTurn != playerChoice){
                while(true){
                    Random rand = new Random();
                    selRow = rand.nextInt(0, board.length - 1);
                    selCol = rand.nextInt(0, board.length - 1);
                    if (checkValid(board, selRow, selCol)) break;
                }
                board[selRow][selCol] = userTurn;
                switch (userTurn) {
                    case 1: 
                        userTurn = 2;
                        break;
                    case 2: 
                        userTurn = 1;
                        break;
                }
                turnCount++; 
                continue;
            }

            if (invalidLocation) {
                System.out.println("Please enter a valid move");
                invalidLocation = false;
            }

            //getting users placement.
            System.out.printf("Player %d, enter your move %n", userTurn);
            
            //error handling for boneheads.
            try {
                selRow = console.nextInt() - 1;
                selCol = console.nextInt() - 1;
            } catch (InputMismatchException err) {
                console.next();
                invalidLocation = true;
                continue;
            }
            
            //making sure the moves are within bounds as well as not overlapping.
            if (checkValid(board, selRow, selCol))
                board[selRow][selCol] = userTurn;
            
            //checking win condition and printing win screen
            if (checkWinner(board, userTurn) || turnCount == Math.pow(board.length, 2)){
                clearConsole();
                printBoard(board);
                if (turnCount == Math.pow(board.length, 2))
                    System.out.print("Tie game. Player 1 and Player 2 lose.");
                else 
                    System.out.printf("Player %d Wins !!%n", userTurn);  
                break;
            }
            
            turnCount++;
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
            else
                //for printing the horizontal hashes.
                for (int j = 1; j < (3*board.length) + (board.length); j++) {
                    if (!(j % 4 == 0)) System.out.print("-");
                    else System.out.print("+");
                }
            System.out.println();
        }
    }

    //method to check if a move is valid
    public static boolean checkValid(int[][] board, int selRow, int selCol){
        try {
            if (board[selRow][selCol] > 0){
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException err){
            return false;
        }
        return true;
    }

    //long way of checking for winner. Could be done with recursion but idk which is faster.
    public static boolean checkWinner(int[][] board, int target){
        //checks rows for X win.
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board.length; j++){
                if (board[i][j] != target) break;
                else if (j == board.length-1) return true;
            }
        }
        //check columns for win.
        for (int j = 0; j < board.length; j++){
            for (int i = 0; i < board.length; i++){
                if (board[i][j] != target) break;
                else if (i == board.length-1) return true;
            }
        }
        //check left diagonal for win.
        for (int i = 0; i < board.length; i++){
            if (board[i][i] != target) break;
            else if (i == board.length-1) return true;
        }
        //check right diagonal for win.
        for (int i = 0, j = board.length-1; i < board.length; i++, j--){
            if (board[i][j] != target) break;
            else if (i == board.length-1) return true;
        }

        return false;
    }
}