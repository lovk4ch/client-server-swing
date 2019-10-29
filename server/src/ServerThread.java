
import Controller.ModelManager;
import Controller.ResponseManager;
import Model.Observer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arthur Novikov
 */
public class ServerThread extends Thread implements Observer {
    private final ModelManager model;
    public static int thread;
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
   
    private void logMessage(String message) {
        Date date = new Date();
        StringBuilder builder;
        builder = new StringBuilder().append(date.toString()).append(": ").append(message);
        System.out.println(builder.toString());
    }
    
    public ServerThread(Socket socket, ModelManager model) throws IOException {
        thread = Server.threadcount++;
        this.socket = socket;
        this.model = model;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        this.model.registerObserver(this);
        logMessage("Клиент " + thread + ": присоединён, всего клиентов: "
            + Server.threadcount);
        start();
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                Server.semaphore.acquire();
                Object obj = in.readObject();
                logMessage("Клиент " + thread + ": принят запрос объекта: "
                    + obj.getClass().getSimpleName()
                    + ", свободных обработчиков: "
                    + Server.semaphore.availablePermits());
                
                Object newObj = model.executeRequest(obj);
                
                if (newObj instanceof String
                    && !newObj.equals(ResponseManager._objectNotFound)
                    && !newObj.equals(ResponseManager._objectExists)) {
                        model.notifyObservers
                            ("Клиент " + thread + " -> " + (String) newObj);
                }
                else {
                    out.writeObject(newObj);
                    out.flush();
                }
                Server.semaphore.release();
                logMessage("Клиент " + thread + ": отправлен объект: "
                    + obj.getClass().getSimpleName()
                    + ", свободных обработчиков: "
                    + Server.semaphore.availablePermits());
            }
        }
        catch (SocketException e) {
            System.err.println("Соединение с клиентом " + thread + " прервано");
        }
        catch (IOException | ClassNotFoundException | InterruptedException
                | JAXBException | CloneNotSupportedException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            // Всегда закрывает:
            try {
                Server.semaphore.release();
                socket.close();
                Server.threadcount--;
                model.removeObserver(this);
                logMessage("Клиент " + thread
                    + ": завершил сеанс, осталось: " + Server.threadcount);
            }
            catch (IOException e) {
                System.err.println("Socket not closed");
            }
        }
    }

    @Override
    public void update(String message) {
        try {
            out.writeObject(message);
            out.flush();
            logMessage("Клиент " + thread + ": обновил данные");
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}