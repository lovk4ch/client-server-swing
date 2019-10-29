package Model.Entity;

import java.io.Serializable;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Arthur Novikov
 */
@XmlRootElement(name = "ticket")
@XmlType(propOrder = {"ticketId","cost","passengerFio","passportSeries","passportNumber","flight"})
public class Ticket implements Serializable {
    private int ticketId;
    private int cost;
    private String passengerFio;
    private int passportSeries;
    private int passportNumber;
    private Flight flight;

    public int getTicketId() {
        return ticketId;
    }

    @XmlElement
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }
    
    public int getCost() {
        return cost;
    }

    @XmlElement
    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getPassengerFio() {
        return passengerFio;
    }

    @XmlElement
    public void setPassengerFio(String passengerFio) {
        this.passengerFio = passengerFio;
    }

    public int getPassportSeries() {
        return passportSeries;
    }

    @XmlElement
    public void setPassportSeries(int passportSeries) {
        this.passportSeries = passportSeries;
    }

    public int getPassportNumber() {
        return passportNumber;
    }

    @XmlElement
    public void setPassportNumber(int passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (passportNumber != ticket.passportNumber) return false;
        if (passportSeries != ticket.passportSeries) return false;
        if (ticketId != ticket.ticketId) return false;
        if (cost != ticket.cost) return false;
        if (passengerFio != null ? !passengerFio.equals(ticket.passengerFio) : ticket.passengerFio != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ticketId;
        result = 31 * result + (passengerFio != null ? passengerFio.hashCode() : 0);
        result = 31 * result + passportSeries;
        result = 31 * result + passportNumber;
        return result;
    }

    @Override
    public String toString() {
        return "Model.Model.Entity.View.Ticket{" +
            "ticketId='" + ticketId + '\'' +
            "cost='" + cost + '\'' +
            ", passengerFio=" + passengerFio +
            ", passportSeries=" + passportSeries +
            ", passportNumber=" + passportNumber +
            '}';
    }

    public Flight getFlight() {
        return flight;
    }

    @XmlElement
    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}