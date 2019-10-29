/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.ResponseManager;
import Model.Entity.Flight;
import Model.Entity.Ticket;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arthur Novikov
 */
public class JDBCParser implements Parser {
    
    List<Flight> flight_list;
    
    public JDBCParser() throws SQLException, FileNotFoundException, IOException {
        createDatabase();
        flight_list = new ArrayList<>();
    }
    
    private Connection getConnection() {
        Connection сonnection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC-драйвер отсутствует. Печалька ;(");
            System.exit(0);
        }
        try {
            String url = "jdbc:mysql://localhost:3306/mydb";
            String name = "root";
            String pass = "100000";
            сonnection = DriverManager
                .getConnection(url, name, pass);
            return сonnection;
        } catch (SQLException e) {
            System.out.println("Подключение не выполнено. Печалька ;(");
            System.out.println(e.getMessage());
        }
        return сonnection;
    }
    
    private BufferedReader readCommandFromFile(String filename) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(new File(filename));
        return new BufferedReader(fr);
    }
    
    private void createDatabase() throws SQLException, FileNotFoundException, IOException {
        Connection сonnection = null;
        Statement statement = null;

        try {
            сonnection = getConnection();
            statement = сonnection.createStatement();
            
            // Файлы со скриптами создания таблиц
            StringBuilder sb = new StringBuilder();
            BufferedReader br = readCommandFromFile("data/create_schema.sql");
            while (br.ready()) sb.append(br.readLine()).append("\n");
            statement.execute(sb.toString());
            sb = new StringBuilder();
            br = readCommandFromFile("data/create_table_1.sql");
            while (br.ready())
                sb.append(br.readLine()).append("\n");
            statement.execute(sb.toString());
            sb = new StringBuilder();
            br = readCommandFromFile("data/create_table_2.sql");
            while (br.ready())
                sb.append(br.readLine()).append("\n");
            statement.execute(sb.toString());
            br = readCommandFromFile("data/data_script.sql");
            while (br.ready())
                statement.execute(br.readLine());
            System.out.println("Successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (сonnection != null) {
                сonnection.close();
            }
        }
    }
    
    @Override
    public synchronized String addFlight(Flight flight) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement
                ("INSERT INTO Flight VALUES (DEFAULT, ?, ?, ?, ?, ?)");
            statement.setInt(1, flight.getFlightCode());
            statement.setDate(2, new java.sql.Date
                (flight.getDispatchDate().getTime()));
            statement.setString(3, flight.getDispatchCity());
            statement.setDate(4, new java.sql.Date
                (flight.getTargetDate().getTime()));
            statement.setString(5, flight.getTargetCity());
            statement.executeUpdate();
            updateFlightList(-1, -1);
            return new StringBuilder().append("Добавлен рейс ")
                .append(Integer.toString(flight.getFlightCode()))
                .append(": ").append(flight.getDispatchCity())
                .append(" - ").append(flight.getTargetCity()).toString();
        } catch (SQLException e) {
            ResponseManager.message_DatabaseRequestException();
            System.out.println(e.getMessage());
        }
        return ResponseManager._objectExists;
    }

    @Override
    public synchronized String updateFlight(Flight flight) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement
                ("UPDATE Flight SET flightCode = ?, dispatchDate = ?, "
                    + "dispatchCity = ?, targetDate = ?, targetCity = ? "
                    + "WHERE flightId = ?");
            statement.setInt(1, flight.getFlightCode());
            statement.setDate(2, new java.sql.Date
                (flight.getDispatchDate().getTime()));
            statement.setString(3, flight.getDispatchCity());
            statement.setDate(4, new java.sql.Date
                (flight.getTargetDate().getTime()));
            statement.setString(5, flight.getTargetCity());
            statement.setInt(6, flight.getFlightId());
            statement.executeUpdate();
            updateFlightList(flight.getFlightId(), 0);
            return new StringBuilder().append("Рейс ")
                .append(Integer.toString(flight.getFlightCode()))
                .append(": ").append(flight.getDispatchCity())
                .append(" - ").append(flight.getTargetCity())
                .append(" был обновлён").toString();
        } catch (SQLException e) {
            ResponseManager.message_DatabaseRequestException();
            System.out.println(e.getMessage());
        }
        return ResponseManager._objectNotFound;
    }

    @Override
    public synchronized String deleteFlight(int id) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement
                ("DELETE FROM Flight WHERE flightId = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            updateFlightList(id, 1);
            return new StringBuilder().append("Рейс ").append(id)
                .append(" удалён").toString();
        } catch (SQLException e) {
            ResponseManager.message_DatabaseRequestException();
            System.out.println(e.getMessage());
        }
        return ResponseManager._objectNotFound;
    }
    
    public void updateFlightList(int id, int type) {
        switch (type) {
            case 0:
                for (int i = 0; i < flight_list.size(); i++) {
                    if (flight_list.get(i).getFlightId() == id) {
                        flight_list.set(i, getFlightById(id));
                        break;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < flight_list.size(); i++) {
                    if (flight_list.get(i).getFlightId() == id) {
                        flight_list.remove(i);
                        break;
                    }
                }
                break;
            case -1:
                flight_list.add(getFlightById(id));
                break;
        }
    }
    
    @Override
    public synchronized Flight getFlightById(int id) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String query;
            if (id == -1)
                query = "SELECT MAX(flightId) FROM Flight";
            else
                query = "SELECT * FROM Flight WHERE flightId = " + id;
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Flight flight = new Flight();
                flight.setFlightId(rs.getInt("flightId"));
                flight.setFlightCode(rs.getInt("flightCode"));
                flight.setDispatchDate(rs.getDate("dispatchDate"));
                flight.setDispatchCity(rs.getString("dispatchCity"));
                flight.setTargetDate(rs.getDate("targetDate"));
                flight.setTargetCity(rs.getString("targetCity"));
                return flight;
            }
        } catch (SQLException e) {
            ResponseManager.message_DatabaseRequestException();
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    @Override
    public synchronized List<Flight> getFlights(boolean isReplicated) {
        try {
            if (isReplicated) {
                flight_list = new ArrayList<>();
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM Flight");
                while (rs.next()) {
                    Flight flight = new Flight();
                    flight.setFlightId(rs.getInt("flightId"));
                    flight.setFlightCode(rs.getInt("flightCode"));
                    flight.setDispatchDate(rs.getDate("dispatchDate"));
                    flight.setDispatchCity(rs.getString("dispatchCity"));
                    flight.setTargetDate(rs.getDate("targetDate"));
                    flight.setTargetCity(rs.getString("targetCity"));
                    flight_list.add(flight);
                }
                return flight_list;
            }
            else return flight_list;
        } catch (SQLException e) {
            ResponseManager.message_DatabaseRequestException();
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    @Override
    public synchronized String addTicket(int flight, Ticket ticket) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement
                ("INSERT INTO Ticket VALUES (DEFAULT, ?, ?, ?, ?)");
            statement.setInt(1, ticket.getCost());
            statement.setString(2, ticket.getPassengerFio());
            statement.setInt(3, ticket.getPassportSeries());
            statement.setInt(4, ticket.getPassportNumber());
            statement.executeUpdate();
            return new StringBuilder().append("Добавлен билет на имя: ")
                .append(ticket.getPassengerFio())
                .append(" рейса ")
                .append(getFlightById(flight).getFlightCode())
                .toString();
        } catch (SQLException e) {
            ResponseManager.message_DatabaseRequestException();
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public synchronized String updateTicket(int flight, Ticket ticket) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement
                ("UPDATE Ticket SET cost = ?, passengerFio = ?, "
                    + "passportSeries = ?, passportNumber = ? "
                    + "WHERE ticketId = ?");
            statement.setInt(1, ticket.getCost());
            statement.setString(2, ticket.getPassengerFio());
            statement.setInt(3, ticket.getPassportSeries());
            statement.setInt(4, ticket.getPassportNumber());
            statement.setInt(5, ticket.getTicketId());
            statement.executeUpdate();
            return new StringBuilder().append("Данные билета на имя: ")
                .append(ticket.getPassengerFio())
                .append(" рейса ")
                .append(getFlightById(flight).getFlightCode())
                .append(" были обновлены").toString();
        } catch (SQLException e) {
            ResponseManager.message_DatabaseRequestException();
            System.out.println(e.getMessage());
        }
        return ResponseManager._objectNotFound;
    }

    @Override
    public synchronized String deleteTicket(int flight, int id) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement
                ("DELETE FROM Ticket WHERE ticketId = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            return new StringBuilder().append("Пассажир рейса ")
                .append(getFlightById(flight).getFlightCode())
                .append(" удалён ").toString();
        } catch (SQLException e) {
            ResponseManager.message_DatabaseRequestException();
            System.out.println(e.getMessage());
        }
        return ResponseManager._objectNotFound;
    }
    
    @Override
    public List<Ticket> getTickets(int flight) {
        try {
            List<Ticket> ticket_list = new ArrayList<>();
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Ticket");
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticketId"));
                ticket.setCost(rs.getInt("cost"));
                ticket.setPassengerFio(rs.getString("passengerFio"));
                ticket.setPassportSeries(rs.getInt("passportSeries"));
                ticket.setPassportNumber(rs.getInt("passportNumber"));
                ticket.setCost(rs.getInt("cost"));
                ticket.setFlight(getFlightById(flight));
                ticket_list.add(ticket);
            }
            return ticket_list;
        } catch (SQLException e) {
            ResponseManager.message_DatabaseRequestException();
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Object getObject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveObject(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}