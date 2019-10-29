package Main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author Arthur Novikov
 */
public class Client {
    public static final int PORT = 8080;
    static final int MAX_THREADS = 1;
    public static InetAddress addr;
    
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        addr = InetAddress.getByName(null);
        while (true) {
            if (ClientThread.threadCount() < MAX_THREADS)
               new ClientThread(addr);
        }
    }
}