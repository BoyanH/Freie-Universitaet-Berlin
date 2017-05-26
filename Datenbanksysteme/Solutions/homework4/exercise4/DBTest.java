package fu.alp4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBTest {

    public static void main(String[] args) {

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("PostgreSQL JDBC Driver not included in class path!");
            return;

        }

        System.out.println("JDBC registered succesffully :)");

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/dbs", "testuser", "testpass");
            statement = connection.createStatement();
        } catch (SQLException e) {

            System.out.println("Failed connecting to DB!");
            return;

        }

        System.out.println("Successfully connected to DB!");


        try {
            System.out.println("Students in DB: ");
            rs = statement.executeQuery("SELECT matrikelnummer, vorname, nachname FROM Student");
            while (rs.next()) {
                int matrikelnummer = rs.getInt("matrikelnummer");
                String firstName = rs.getString("vorname");
                String lastName = rs.getString("nachname");
                System.out.println(String.format("Matrikelnummer: %s; Vorname: %s; Nachname: %s",
                        matrikelnummer, firstName, lastName));
            }
            connection.close();
        }
        catch(SQLException e) {
            System.out.println("Error while executing sql query!");
        }

    }
}
