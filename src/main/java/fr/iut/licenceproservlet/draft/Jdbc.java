package fr.iut.licenceproservlet.draft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Jdbc {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {

        String url = "jdbc:mysql://localhost:3306/test";

        Connection connection;
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, "root", "");

        return connection;
    }
    public static  void main(String[] args) throws SQLException, ClassNotFoundException {

        String url = "jdbc:mysql://localhost:3306/test";

        Connection connection;

        {
            connection = DriverManager.getConnection(url, "root", "");
        }

    }
}
