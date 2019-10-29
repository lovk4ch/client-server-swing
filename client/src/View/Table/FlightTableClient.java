package View.Table;

import Model.Entity.Flight;
import Model.Socket.addObject;
import Model.Socket.deleteObject;
import Model.Socket.getObject;
import Model.Socket.updateObject;
import Controller.MessageManager;
import static Main.ClientThread.out;
import Model.Socket.socketTypes;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FlightTableClient extends AbstractTableModel {

    private final Set<TableModelListener> listeners;
    private List<Flight> flight_list;

    public FlightTableClient() throws IOException, ClassNotFoundException {
        this.listeners = new HashSet<>();
        getFlights(true);
    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return int.class;
            case 1:
                return int.class;
            case 2:
                return Date.class;
            case 3:
                return String.class;
            case 4:
                return Date.class;
            case 5:
                return String.class;
        }
        return Object.class;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 1:
                return "Код рейса";
            case 2:
                return "Дата отправления";
            case 3:
                return "Город отправления";
            case 4:
                return "Дата прибытия";
            case 5:
                return "Пункт назначения";
        }
        return "";
    }

    @Override
    public int getRowCount() {
        return flight_list == null || flight_list.isEmpty() ? 0 : flight_list.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Flight flight = this.flight_list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return flight.getFlightId();
            case 1:
                return flight.getFlightCode();
            case 2:
                return flight.getDispatchDate();
            case 3:
                return flight.getDispatchCity();
            case 4:
                return flight.getTargetDate();
            case 5:
                return flight.getTargetCity();
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }

    public void add(int flightCode, Date dispatchDate, String dispatchCity,
        Date targetDate, String targetCity) throws IOException, ClassNotFoundException {
        
            Flight flight = new Flight();
            flight.setFlightCode(flightCode);
            flight.setDispatchDate(dispatchDate);
            flight.setDispatchCity(dispatchCity);
            flight.setTargetDate(targetDate);
            flight.setTargetCity(targetCity);
            
            addObject obj = new addObject(socketTypes.Flight);
            obj.obj = flight;
            out.writeObject(obj);
            out.flush();
            getFlights(false);
    }
    
    public void update(int id, int flightCode, Date dispatchDate, String dispatchCity,
        Date targetDate, String targetCity) throws IOException, ClassNotFoundException {
        if (MessageManager.updateConfirm()) {
            
            Flight flight = new Flight();
            flight.setFlightId(id);
            flight.setFlightCode(flightCode);
            flight.setDispatchDate(dispatchDate);
            flight.setDispatchCity(dispatchCity);
            flight.setTargetDate(targetDate);
            flight.setTargetCity(targetCity);
            
            updateObject obj = new updateObject(socketTypes.Flight);
            obj.obj = flight;
            out.writeObject(obj);
            out.flush();
            getFlights(false);
        }
    }

    public void delete(int delete_id) throws IOException, ClassNotFoundException {
        if (MessageManager.deleteConfirm()) {
            deleteObject obj = new deleteObject(socketTypes.Flight);
            obj.delete_id = delete_id;
            out.writeObject(obj);
            out.flush();
            getFlights(false);
        }
    }
    
    public void getFlights(boolean isReplicated) throws ClassNotFoundException,
        IOException {
            getObject obj;
            if (isReplicated)
                obj = new getObject(socketTypes.Flight, isReplicated);
            else
                obj = new getObject(socketTypes.Flight);
            out.writeObject(obj);
            out.flush();
    }
    
    public void setFlights(getObject obj) {
        flight_list = obj.getList() == null ? new ArrayList<>() : (List<Flight>) obj.getList();
    }
}