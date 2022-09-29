
/*
Bawi "Joel" Lian
04 - 05
 */

import java.net.*;
import java.net.ServerSocket;
import java.util.*;
import java.io.*;
import java.net.Socket;


public class Dispatcher {
    // standard main() method
    // in main method:
    public static ServerSocket Port;

    public static void main(String[] args) {
        Serverthread Server;
        Socket toClientSocket;


     try{

         Port = new ServerSocket(8787); //port 8787: door number, server: door client will use to connect
         System.out.println("waiting for connection");
         while (true) {
             toClientSocket = Port.accept(); //toClientSocket: tool to communicate with client, Until a request is received it'll block
             //A connection has been made
             System.out.println("RECEIVED REQUEST");

             Server= new Serverthread(toClientSocket);
             Server.start();
         }
     } catch (IOException e) {
         e.printStackTrace();
      }

    }


}




//end of class dispatcher



