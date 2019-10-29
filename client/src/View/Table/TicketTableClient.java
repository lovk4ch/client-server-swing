package View.Table;

import Model.Entity.Ticket;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TicketTableClient extends AbstractTableModel {

    private final Set<TableModelListener> listeners;
    private List<Ticket> ticket_list;
    private final int flight;

    public TicketTableClient(int flight) throws IOException, ClassNotFoundException {
        this.listeners = new HashSet<>();
        this.flight = flight;
        getTickets();
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
                return String.class;
            case 3:
                return int.class;
            case 4:
                return int.class;
        }
        return Object.class;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 1:
                return "Цена билета";
            case 2:
                return "ФИО пассажира";
            case 3:
                return "Серия паспорта";
            case 4:
                return "Номер паспорта";
        }
        return "";
    }

    @Override
    public int getRowCount() {
        return ticket_list == null || ticket_list.isEmpty() ? 0 : ticket_list.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ticket ticket = this.ticket_list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return ticket.getTicketId();
            case 1:
                return ticket.getCost();
            case 2:
                return ticket.getPassengerFio();
            case 3:
                return ticket.getPassportSeries();
            case 4:
                return ticket.getPassportNumber();
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

    public void add(int cost, String fio,
        int series, int number) throws IOException, ClassNotFoundException {
        
            Ticket ticket = new Ticket();
            ticket.setCost(cost);
            ticket.setPassengerFio(fio);
            ticket.setPassportSeries(series);
            ticket.setPassportNumber(number);

            addObject obj = new addObject(socketTypes.Ticket);
            obj.obj = ticket;
            obj.id = flight;
            out.writeObject(obj);
            out.flush();
            getTickets();
    }

    public void update(int id, int cost, String fio,
        int series, int number) throws IOException, ClassNotFoundException {
        if (MessageManager.updateConfirm()) {
            
            Ticket ticket = new Ticket();
            ticket.setTicketId(id);
            ticket.setCost(cost);
            ticket.setPassengerFio(fio);
            ticket.setPassportSeries(series);
            ticket.setPassportNumber(number);

            updateObject obj = new updateObject(socketTypes.Ticket);
            obj.obj = ticket;
            obj.id = flight;
            out.writeObject(obj);
            out.flush();
            getTickets();
        }
    }

    public void delete(int delete_id) throws IOException, ClassNotFoundException {
        if (MessageManager.deleteConfirm()) {
            deleteObject obj = new deleteObject(socketTypes.Ticket);
            obj.delete_id = delete_id;
            obj.id = flight;
            out.writeObject(obj);
            out.flush();
            getTickets();
        }
    }
    
    public void getTickets() throws IOException, ClassNotFoundException {
        getObject obj = new getObject(socketTypes.Ticket);
        obj.id = flight;
        out.writeObject(obj);
        out.flush();
    }
    
    public void setTickets(getObject obj) {
        ticket_list = obj.getList() == null ? new ArrayList<>() : (List<Ticket>) obj.getList();
    }
}