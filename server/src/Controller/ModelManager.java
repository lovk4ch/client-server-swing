/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Parser;
import Model.Entity.Flight;
import Model.Entity.Ticket;
import Model.JDBCParser;
import Model.Observable;
import Model.Observer;
import Model.Socket.addObject;
import Model.Socket.deleteObject;
import Model.Socket.getObject;
import Model.Socket.updateObject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Arthur Novikov
 */
public class ModelManager implements Observable {
    
    public List<Observer> observers;
    private final Parser parser;
    
    public ModelManager() throws JAXBException, IOException, SQLException {
        observers = new ArrayList<>();
        parser = new JDBCParser();
    }
    
    public Object executeRequest(Object obj) throws IOException, JAXBException,
        CloneNotSupportedException {
        
        if (obj instanceof getObject) {
            return getObject(obj);
        }
        else if (obj instanceof addObject) {
            return addObject(obj);
        }
        else if (obj instanceof updateObject) {
            return updateObject(obj);
        }
        else if (obj instanceof deleteObject) {
            return deleteObject(obj);
        }
        return obj;
    }
    
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers)
            observer.update(message);
    }
    
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }
    
    public Object getObject(Object obj) throws IOException, JAXBException,
        CloneNotSupportedException {
        
        getObject outObj = (getObject) obj;
        switch (outObj.type)
        {
            case Flight:
                outObj.setList(parser.getFlights(outObj.isReplicated()));
                break;
            case Ticket:
                outObj.setList(parser.getTickets(outObj.id));
                break;
                
        }
        return outObj.clone();
    }
   
    
    public String addObject(Object obj) throws IOException, JAXBException {
        addObject outObj = (addObject) obj;
        switch (outObj.type)
        {
            case Flight:
                return parser.addFlight((Flight)outObj.obj);
            case Ticket:
                return parser.addTicket(outObj.id, (Ticket)outObj.obj);
        }
        return null;
    }
    
    public String updateObject(Object obj) throws IOException, JAXBException {
        updateObject outObj = (updateObject)obj;
        switch (outObj.type)
        {
            case Flight:
                return parser.updateFlight((Flight)outObj.obj);
            case Ticket:
                return parser.updateTicket(outObj.id, (Ticket)outObj.obj);
        }
        return null;
    }
    
    public String deleteObject(Object obj) throws IOException, JAXBException {
        deleteObject outObj = (deleteObject) obj;
        switch (outObj.type)
        {
            case Flight:
                return parser.deleteFlight(outObj.delete_id);
            case Ticket:
                return parser.deleteTicket(outObj.id, outObj.delete_id);
        }
        return null;
    }
}