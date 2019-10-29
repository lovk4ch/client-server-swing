package Model;

import Controller.ResponseManager;
import Model.Entity.Flight;
import Model.Entity.Ticket;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Arthur Novikov
 */
public class JaxbParser implements Parser {
    
    public FlightList flights;
    List<Flight> flight_list;
    
    public JaxbParser() {
        flights = new FlightList();
        flight_list = flights.getFlightList(this);
    }
    
    @Override
    public synchronized String addFlight(Flight flight) {
        for (int i = 0; i < flight_list.size(); i++) {
            if (flight_list.get(i).getFlightCode() == flight.getFlightCode())
                return ResponseManager._objectExists;
        }
        flight.setFlightId(flight_list.isEmpty() ? 1 : flight_list.get(flight_list.size() - 1).getFlightId() + 1);
        flight_list.add(flight);
        saveObject(flights);
        return new StringBuilder().append("Добавлен рейс ")
            .append(Integer.toString(flight.getFlightCode()))
            .append(": ").append(flight.getDispatchCity())
            .append(" - ").append(flight.getTargetCity()).toString();
    }

    @Override
    public synchronized String updateFlight(Flight flight) {
        for (int i = 0; i < flight_list.size(); i++) {
            if (flight_list.get(i).getFlightCode() == flight.getFlightCode()
                    && flight_list.get(i).getFlightId() != flight.getFlightId()) {
                return ResponseManager._objectExists;
            }
            if (flight_list.get(i).getFlightId() == flight.getFlightId()) {
                flight_list.get(i).setFlightCode(flight.getFlightCode());
                flight_list.get(i).setDispatchCity(flight.getDispatchCity());
                flight_list.get(i).setDispatchDate(flight.getDispatchDate());
                flight_list.get(i).setTargetCity(flight.getTargetCity());
                flight_list.get(i).setTargetDate(flight.getTargetDate());
                saveObject(flights);
                return new StringBuilder().append("Рейс ")
                    .append(Integer.toString(flight.getFlightCode()))
                    .append(": ").append(flight.getDispatchCity())
                    .append(" - ").append(flight.getTargetCity())
                    .append(" был обновлён").toString();
            }
        }
        return ResponseManager._objectNotFound;
    }

    @Override
    public synchronized String deleteFlight(int id) {
        for (int i = 0; i < flight_list.size(); i++) {
            if (flight_list.get(i).getFlightId() == id) {
                String result = new StringBuilder().append("Рейс ")
                    .append(Integer.toString(flight_list.get(i).getFlightCode()))
                    .append(": ").append(flight_list.get(i).getDispatchCity())
                    .append(" - ").append(flight_list.get(i).getTargetCity())
                    .append(" удалён").toString();
                flight_list.remove(i);
                saveObject(flights);
                return result;
            }
        }
        return ResponseManager._objectNotFound;
    }
    
    @Override
    public synchronized List<Flight> getFlights(boolean isReplicated) {
        return flight_list;
    }

    @Override
    public synchronized Flight getFlightById(int id) {
        for (int i = 0; i < flight_list.size(); i++) {
            if (flight_list.get(i).getFlightId() == id) {
                return flight_list.get(i);
            }
        }
        return null;
    }
    
    @Override
    public synchronized String addTicket(int flight, Ticket ticket) {
        List<Ticket> ticket_list = getTickets(flight);
        for (int i = 0; i < ticket_list.size(); i++) {
            if (ticket_list.get(i).getPassportSeries() == ticket.getPassportSeries()
                && ticket_list.get(i).getPassportNumber() == ticket.getPassportNumber()) {
                return ResponseManager._objectExists;
            }
        }
        ticket.setTicketId(ticket_list.isEmpty() ? 1 :ticket_list.get(ticket_list.size() - 1).getTicketId() + 1);
        ticket_list.add(ticket);
        getFlightById(flight).setTickets(ticket_list);
        saveObject(flights);
        return new StringBuilder().append("Был добавлен билет на имя: ")
            .append(ticket.getPassengerFio())
            .append(" рейса ")
            .append(getFlightById(flight).getFlightCode())
            .toString();
    }

    @Override
    public synchronized String updateTicket(int flight, Ticket ticket) {
        List<Ticket> ticket_list = getTickets(flight);
        for (int i = 0; i < ticket_list.size(); i++) {
            if (ticket_list.get(i).getPassportSeries() == ticket.getPassportSeries()
                && ticket_list.get(i).getPassportNumber() == ticket.getPassportNumber()
                && ticket_list.get(i).getTicketId() != ticket.getTicketId()) {
                return ResponseManager._objectExists;
            }
            if (ticket_list.get(i).getTicketId() == ticket.getTicketId()) {
                ticket_list.set(i, ticket);
                getFlightById(flight).setTickets(ticket_list);
                saveObject(flights);
                return new StringBuilder().append("Данные билета на имя: ")
                    .append(ticket_list.get(i).getPassengerFio())
                    .append(" рейса ")
                    .append(getFlightById(flight).getFlightCode())
                    .append(" были обновлены ").toString();
            }
        }
        return ResponseManager._objectNotFound;
    }

    @Override
    public synchronized String deleteTicket(int flight, int id) {
        List<Ticket> ticket_list = getTickets(flight);
        for (int i = 0; i < ticket_list.size(); i++) {
            if (ticket_list.get(i).getTicketId() == id) {
                String result = new StringBuilder().append("Пассажир ")
                    .append(ticket_list.get(i).getPassengerFio())
                    .append(" рейса ")
                    .append(getFlightById(flight).getFlightCode())
                    .append(" удалён ").toString();
                ticket_list.remove(i);
                getFlightById(flight).setTickets(ticket_list);
                saveObject(flights);
                return result;
            }
        }
        return ResponseManager._objectNotFound;
    }
    
    @Override
    public List<Ticket> getTickets(int flight) {
        List<Ticket> ticket_list = (List<Ticket>)getFlightById(flight).getTickets();
        if (ticket_list == null)
            ticket_list = new ArrayList<>();
        return ticket_list;
    }
    
    @Override
    public Object getObject() {
        try {
            File file = new File("data/Flights.xml");
            JAXBContext context = JAXBContext.newInstance(FlightList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object object = unmarshaller.unmarshal(file);
            
            return object;
        } catch (JAXBException ex) {
            Logger.getLogger(JaxbParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void saveObject(Object o) {
        try {
            File file = new File("data/Flights.xml");
            if (!file.exists()) {
                file.createNewFile();
            }
            JAXBContext context = JAXBContext.newInstance(o.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(o, file);
        }
        catch (JAXBException exception) {
            Logger.getLogger(Application.class.getName()).
            log(Level.SEVERE, "marshallExample threw JAXBException", exception);
        } catch (IOException ex) {
            Logger.getLogger(JaxbParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}