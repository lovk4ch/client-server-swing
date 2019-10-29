package Model.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Collection;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Arthur Novikov
 */
@XmlRootElement(name = "flight")
@XmlType(propOrder = {"flightId","flightCode","dispatchDate","dispatchCity","targetDate","targetCity","tickets"})
public class Flight implements Serializable {
    private int flightId;
    private int flightCode;
    private Date dispatchDate;
    private String dispatchCity;
    private Date targetDate;
    private String targetCity;
    private Collection<Ticket> tickets;

    public int getFlightId() {
        return flightId;
    }

    @XmlElement
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getFlightCode() {
        return flightCode;
    }

    @XmlElement
    public void setFlightCode(int flightCode) {
        this.flightCode = flightCode;
    }

    public Date getDispatchDate() {
        return dispatchDate;
    }

    @XmlElement
    public void setDispatchDate(Date dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getDispatchCity() {
        return dispatchCity;
    }

    @XmlElement
    public void setDispatchCity(String dispatchCity) {
        this.dispatchCity = dispatchCity;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    @XmlElement
    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public String getTargetCity() {
        return targetCity;
    }

    @XmlElement
    public void setTargetCity(String targetCity) {
        this.targetCity = targetCity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        if (flightId != flight.flightId) return false;
        if (flightCode != flight.flightCode) return false;
        if (dispatchDate != null ? !dispatchDate.equals(flight.dispatchDate) : flight.dispatchDate != null)
            return false;
        if (targetDate != null ? !targetDate.equals(flight.targetDate) : flight.targetDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = flightId;
        result = 31 * result + (dispatchDate != null ? dispatchDate.hashCode() : 0);
        result = 31 * result + (targetDate != null ? targetDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Model.Flight{" +
            "flightId='" + flightId + '\'' +
            ", flightCode=" + flightCode +
            ", dispatchDate=" + dispatchDate +
            ", dispatchCity=" + dispatchCity +
            ", targetDate=" + targetDate +
            ", targetCity=" + targetCity +
            '}';
    }

    public Collection<Ticket> getTickets() {
        return tickets;
    }

    @XmlElement
    @XmlElementWrapper
    public void setTickets(Collection<Ticket> tickets) {
        this.tickets = tickets;
    }
}