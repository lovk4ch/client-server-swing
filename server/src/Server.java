/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Controller.ModelManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Arthur Novikov
 */
public class Server {
    static final int PORT = 8080;
    static int threadcount = 0;
    static ServerSocket s;
    static ModelManager model;
    static Semaphore semaphore;
    
    public static void main(String[] args) throws IOException, SQLException, JAXBException {
        
        ServerSocket s = new ServerSocket(PORT);
        semaphore = new Semaphore(5);
        model = new ModelManager();
        
        System.out.println("Server is ready");
        try {
            while (true) {
                Socket socket = s.accept();
                try {
                    new ServerThread(socket, model);
                }
                catch (IOException e) {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его:
                    socket.close();
                }
            }
        }
        finally {
            s.close();
        }
    }
}