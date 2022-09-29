/*
Bawi "Joel" Lian
04 - 05
 */

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    private static String hostName = "localhost";
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Socket toServerSocket; //tool to communicate with dispatcher
    private static char[][] board;
    private static int row, col;

    public static void main(String[] args) {
        //next we will create an empty local board filled with blanks
        board = new char[4][4];
        for (int x = 0; x <= 3; x++)
            for (int y = 0; y <= 3; y++) {board[x][y] = ' ';}
        row = -1;
        col = -1;

        System.out.println("CLIENT is attempting connection...");

        try {
            toServerSocket = new Socket(hostName, 8787); //connect to port via door number
            //connection has been made
            System.out.println("CLIENT CONNECTION SUCCESS");
            inputStream = new DataInputStream(toServerSocket.getInputStream());
            outputStream = new DataOutputStream(toServerSocket.getOutputStream());
            out = new PrintWriter(outputStream, true);
             in = new BufferedReader((new InputStreamReader(inputStream)));
          playGame(in, out); //calling playGame method & passing it BufferedReader & PrintWriter objects
        } catch (IOException e) {
            System.err.println("CLIENT: " + e);
        }

    }
    public static void playGame(BufferedReader in, PrintWriter out) throws IOException {
        //Declare Scanner variable named inp to read from standard input
        Scanner inp = new Scanner(System.in);
        //Declare a String named response to hold messages from the server
        String response;
        //Declare a boolean flag name turn initialized to false indicating server's turn to talk
        boolean turn = false;
        //declare a second boolean variable named gameover which is set to false
        boolean gameover = false;
        while(!gameover)
        {
            if (turn) {
                do {//ensure move is legal
                    System.out.print("\nEnter Row: ");
                    row = inp.nextInt();
                    System.out.print("Enter Column: ");
                    col = inp.nextInt();
                }

                while (row < 0 || row > 3 || col > 3 || board[row][col] != ' ');
                //set the correct cell of board matrix to 'O'
                board[row][col] = 'O';
                //now send the move message to the server via the
                // PrintWriter object's println() method
                out.println("MOVE " + row +" " + col);

             }
            else {
                // read a msg from BufferedReader object using readLine() method and store it in the Response string
                response = in.readLine();
                System.out.println("MSG RECEIVED BY CLIENT: " + response);
                if (!response.equals("CLIENT")) { //must be server's move
                    String[] arg = response.split("\\s+"); //this statement will split the message into tokens
                    if (arg.length > 3) {
                        row = Integer.parseInt(arg[1]);
                        col = Integer.parseInt(arg[2]);
                        //args[3] NOT equal to "WIN" AND row NOT EQUAL to -1
                        if (!arg[3].equals("WIN") && row != -1){
                            //set appropriate cell of board matrix to "X"
                            board[row][col] = 'X';
                    }
                        switch (arg[3]) {
                            case "WIN":
                                System.out.println("\n\nCongratulations!!! You WON the game!");
                                break;

                            case "TIE":
                                System.out.println("\nThe game was a TIE!");
                                break;
                            case "LOSS":
                                System.out.println("\nSORRY! You LOST the game!");
                                break;
                        }
                        gameover = true;
                    }
                    else // move was not a win, loss, or tie - just a regular old move
                    {
                        //get row from args[1]
                        row = Integer.parseInt(arg[1]);
                        //get col from args[2]
                        col = Integer.parseInt(arg[2]);
                        //set cell of board matrix to 'X'
                        board[row][col] = 'X';
                    }
                }//end if - server's move code ends
                else {
                    System.out.println("\nYOU MOVE FIRST");
                }
            }//end else

            printboard(); //call printboard method
            turn = !turn; //change the turn
        }
        System.out.println("\n\nHere is the final game board");
        printboard();
    }

    /*put your printboard method here!!!! */
   static void printboard() {
        System.out.println(board[0][0] + "  |  " + board[0][1] + "  |  " + board[0][2] + "  |  " + board[0][3]);
        System.out.println("--------------------");
        System.out.println(board[1][0] + "  |  " + board[1][1] + "  |  " + board[1][2] + "  |  " + board[1][3]);
        System.out.println("--------------------");
        System.out.println(board[2][0] + "  |  " + board[2][1] + "  |  " + board[2][2] + "  |  " + board[2][3]);
        System.out.println("--------------------");
        System.out.println(board[3][0] + "  |  " + board[3][1] + "  |  " + board[3][2] + "  |  " + board[3][3]);
      }
    }


//end of class Client




