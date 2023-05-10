package fr.iut.licenceproservlet;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import static java.lang.System.out;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        // Retrieve all cars from the cars table
        List<Car> cars = new ArrayList<>();
        try {
            Connection connection = Jdbc.getConnection();
            ResultSet resultSet;
            try (Statement statement = connection.createStatement()) {
                resultSet = statement.executeQuery("SELECT * FROM cars");
            }
            while (resultSet.next()) {
                int id = resultSet.getInt("id_car");
                String model = resultSet.getString("model_car");
                String color = resultSet.getString("color_car");
                Car car = new Car(id, model, color);
                cars.add(car);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Set the cars attribute in the request object
        request.setAttribute("cars", cars);

        // Forward the request to the JSP page
        RequestDispatcher dispatcher = request.getRequestDispatcher("cars.jsp");
        dispatcher.forward(request, response);
        // Add a link to cars.jsp in the response
        PrintWriter out = response.getWriter();
        out.println("<a href='cars.jsp'>View all cars</a>");
    }

//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        response.setContentType("text/html");
//
//        // Get connection from Jdbc class
//        Connection connection = null;
//        try {
//            connection = Jdbc.getConnection();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // Select all cars from cars table
//        String sql = "SELECT * FROM cars";
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        try {
//            statement = connection.prepareStatement(sql);
//            resultSet = statement.executeQuery();
//
//            // Display all cars
//            PrintWriter out = response.getWriter();
//            out.println("<html><body>");
//            out.println("<h1>" + message + "</h1>");
//            out.println("<table>");
//            out.println("<tr><th>ID</th><th>Model</th><th>Color</th></tr>");
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id_car");
//                String model = resultSet.getString("model_car");
//                String color = resultSet.getString("color_car");
//                out.println("<tr><td>" + id + "</td><td>" + model + "</td><td>" + color + "</td></tr>");
//            }
//            out.println("</table>");
//            out.println("</body></html>");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // Close statement and result set
//            try {
//                if (resultSet != null) {
//                    resultSet.close();
//                }
//                if (statement != null) {
//                    statement.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Hello
////        PrintWriter out = response.getWriter();
////        out.println("<html><body>");
////        out.println("<h1>" + message + "</h1>");
////        out.println("</body></html>");
//
////        Person person = new Person("John Doe");
////        request.setAttribute("person", person);
////        request.getRequestDispatcher("person.jsp").forward(request, response);
//    }

    public void destroy() {
    }
}