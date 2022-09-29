/*
Bawi "Joel" Lian
04 - 05
 */


import java.io.*;
import java.net.Socket;
import java.util.Random;


public class Serverthread extends Thread { //Sock to communicate with the Client
    private Socket toClientSocket;
    private DataInputStream inputStream; //stores input stream of the socket
    private DataOutputStream outputStream; //stores output stream of the socket
    private PrintWriter out; //this enables us to use print() and println() method
    private BufferedReader in; //the BufferedReader will allow us to use readLine() method
    private Random gen; //used to select random moves
    private static char[][] board; //implemented game board as a matrix of char to make printing and comparisons easier
    private int row, col; //holds current row and/or column values for moves


    //A constructor receiving a single argument of type Socket
    Serverthread(Socket toClientSocket) {
       this.toClientSocket = toClientSocket;
        gen = new Random();
        try {
            inputStream = new DataInputStream(toClientSocket.getInputStream());
            outputStream = new DataOutputStream(toClientSocket.getOutputStream());
            //instantiate printwriter object named out
            out = new PrintWriter(outputStream, true);
            //use DataOutputStream object to instantiate BufferedReader as "in"
            in = new BufferedReader(new InputStreamReader(inputStream));

        } catch (IOException e) {
            System.err.println("Serverthread: " + e);
        }

        board = new char[4][4];
        for (int x = 0; x <= 3; x++) {
            for (int y = 0; y <= 3; y++) {
                board[x][y] = ' ';
            }
        }
        row = -1;
        col = -1;
    }

    public Socket gettoClientSocket() {
      return toClientSocket;
    }

    public void run() {
        int counter = 0;
        String response = "";
        boolean gameover = false;
        boolean turn = true;

        //random number generator to determine move
        if (gen.nextInt(2) == 0) {
            turn = false;
            out.println("CLIENT");
        }
        else
            turn = true;

        do {
            if (!turn) {
                try {
                    response = in.readLine();
                    System.out.println("\nCLIENT-MOVE");
                } catch (IOException e) {
                    System.err.println("Some sort of read error on sock in server thread " + e);
                    System.exit(-1);
                }
                String[] data = response.split("\\s+");
                row = Integer.parseInt(data[1]);
                col = Integer.parseInt(data[2]);
                board[row][col] = 'O';
                printboard();
                counter++;
                if (Checkwin() || counter == 16) {
                    gameover = true;
                    if (Checkwin())
                        out.println("MOVE " + row + " " + col + " WIN");
                    else
                        out.println("MOVE " + row + " " + col + " TIE");
                } //else {
                   // out.println("MOVE " + row + " " + col);
              //  }
            } else {   // this is the computer server’s move code
                System.out.println("\nSERVER-MOVE");
                Makemove();
                counter++;
                board[row][col] = 'X';
                printboard();
                if (Checkwin() || counter == 16) {
                    gameover = true;
                    if (Checkwin())
                        out.println("MOVE " + row + " " + col + " LOSS");
                    else
                        out.println("MOVE " + row + " " + col + " TIE");
                } else {
                    out.println("MOVE " + row + " " + col);
                }
            }
            turn = !turn;
        } while (!gameover);

    }



    void Makemove() {
        boolean legalMove = false;
        while (!legalMove) {
            row = gen.nextInt(4);
            col = gen.nextInt(4);
            if (board[row][col] == ' ') { //empty grid
                legalMove = true;
            }
        }

    }



    boolean Checkwin() {
        //check for a row-win
        for (int x = 0; x <= 3; x++) {
            if (board[x][0] == board[x][1] && board[x][1] == board[x][2]
                    && board[x][2] == board[x][3] && board[x][0] != ' ')
                return true;

            //check for col win
            else if (board[0][x] == board[1][x] && board[1][x] == board[2][x]
                    && board[2][x] == board[3][x] && board[0][x] != ' ')
                return true;

            //check for diagnol win
            else if (board[0][0] == board[1][1] && board[1][1] == board[2][2]
                    && board[2][2] == board[3][3] && board[0][0] != ' ')
                return true;

            //check for anti-diagnol win
            else if (board[3][0] == board[2][1] && board[2][1] == board[1][2]
                    && board[1][2] == board[0][3] && board[0][3] != ' ')
                return true;
              }

            return false;
            }




        //check for a diagnol-win




    void printboard() {
        System.out.println(board[0][0] + "  |  " + board[0][1] + "  |  " + board[0][2] + "  |  " + board[0][3]);
        System.out.println("--------------------");
        System.out.println(board[1][0] + "  |  " + board[1][1] + "  |  " + board[1][2] + "  |  " + board[1][3]);
        System.out.println("--------------------");
        System.out.println(board[2][0] + "  |  " + board[2][1] + "  |  " + board[2][2] + "  |  " + board[2][3]);
        System.out.println("--------------------");
        System.out.println(board[3][0] + "  |  " + board[3][1] + "  |  " + board[3][2] + "  |  " + board[3][3]);
       }
  }


//end of class Server







