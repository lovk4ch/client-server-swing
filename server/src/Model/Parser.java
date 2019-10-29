/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Entity.Flight;
import Model.Entity.Ticket;
import java.util.List;

/**
 *
 * @author Arthur Novikov
 */
public interface Parser {

    String addFlight(Flight flight);

    String addTicket(int flight, Ticket ticket);

    String deleteFlight(int id);

    String deleteTicket(int flight, int id);

    Flight getFlightById(int id);

    Object getObject();
    
    List<Flight> getFlights(boolean isReplicated);
    
    void saveObject(Object o);

    List<Ticket> getTickets(int flight);

    String updateFlight(Flight flight);

    String updateTicket(int flight, Ticket ticket);
}