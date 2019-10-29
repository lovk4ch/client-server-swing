/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Entity.Flight;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Arthur Novikov
 */
@XmlType(propOrder = {"flight_list"})
@XmlRootElement
public class FlightList {
    
    @XmlElement
    @XmlElementWrapper
    public List<Flight> flight_list;
    
    public List<Flight> getFlightList(Parser parser) {
        flight_list =((FlightList) parser.getObject()).flight_list;
        if (flight_list == null)
            flight_list = new ArrayList<>();
        return flight_list;
    }
    
    public void setFlightList(List<Flight> flight_list) {
        this.flight_list = flight_list;
    }
}