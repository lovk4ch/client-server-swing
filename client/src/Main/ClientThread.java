/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Controller.MessageManager;
import Model.Socket.getObject;
import View.FlightView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arthur Novikov
 */
public class ClientThread extends Thread {
    
    public static ObjectInputStream in;
    public static ObjectOutputStream out;
    
    private FlightView flightView;
    private Socket socket;
    private static int counter = 0;
    private int id = counter++;
    private static int threadcount = 0;
   
    public static int threadCount() {
       return threadcount;
    }
   
    public ClientThread(InetAddress addr) throws ClassNotFoundException {
        System.out.println("Making client " + id + "... Connecting to master server...");
        threadcount++;
        try {
            socket = new Socket(addr, Client.PORT);
        }
        catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            start();
        }
        catch (IOException e) {
            // Сокет должен быть закрыт при любой
            // ошибке, кроме ошибки конструктора сокета:
            try {
                socket.close();
            }
            catch (IOException e2) {
                System.err.println("Socket not closed");
            }
        }
        // В противном случае сокет будет закрыт
        // в методе run() нити.
    }
   
    @Override
    public void run() {
        try {
            flightView = new FlightView();
            flightView.setVisible(true);
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof getObject) {
                    getObject resObj = (getObject) obj;
                    switch (resObj.type)
                    {
                        case Flight:
                            flightView.setFlights(resObj);
                            break;
                        case Ticket:
                            flightView.setTickets(resObj);
                            break;
                    }
                }
                else if (obj instanceof String) {
                    MessageManager.Info((String) obj);
                }
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }
}