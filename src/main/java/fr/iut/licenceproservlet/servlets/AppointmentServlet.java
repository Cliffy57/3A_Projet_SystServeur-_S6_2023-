package fr.iut.licenceproservlet.servlets;

import fr.iut.licenceproservlet.Appointment;
import fr.iut.licenceproservlet.Client;
import fr.iut.licenceproservlet.Employee;
import fr.iut.licenceproservlet.ReservationManager;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "appointmentServlet", value = "/appointment-servlet")
public class AppointmentServlet extends HttpServlet {

    private ReservationManager reservationManager;

    public void init() throws jakarta.servlet.ServletException {
        super.init();
        reservationManager = new ReservationManager();
    }

    public void destroy() {
        reservationManager.close();
        super.destroy();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Assuming you have a List<Appointment> appointments
        List<Appointment> appointments = reservationManager.getAppointments(); // Your logic to retrieve appointments

        System.out.println("Retrieved " + appointments.size() + " appointments by Servlet");  // <-- Add this line

        // Set the appointments as an attribute in the request scope
        request.setAttribute("appointments", appointments);

        // Forward the request to the JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("new_index.jsp");
        dispatcher.forward(request, response);
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "add":
                    addAppointment(request, response);
                    break;
                case "edit":
                    editAppointment(request, response);
                    break;
                case "delete":
                    deleteAppointment(request, response);
                    break;
                default:
                    break;
            }
        } else {
            // Invalid action parameter, handle accordingly
        }
    }

    private void getAppointmentById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, jakarta.servlet.ServletException {
        String appointmentId = request.getParameter("id");

        if (appointmentId != null) {
            Long id = Long.parseLong(appointmentId);
            Appointment appointment = reservationManager.getAppointmentById(id);

            if (appointment != null) {
                request.setAttribute("appointment", appointment);
                request.getRequestDispatcher("/appointment.jsp").forward(request, response);
            } else {
                // Appointment not found, handle accordingly
            }
        } else {
            // Invalid ID parameter, handle accordingly
        }
    }

    private void addAppointment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the form data
        String dateStr = request.getParameter("date");
        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Long clientId = Long.parseLong(request.getParameter("clientId"));
        Long employeeId = Long.parseLong(request.getParameter("employeeId"));

        // Perform validation
        // ...

        // Create a new appointment object
        Client client = reservationManager.getClientById(clientId);
        Employee employee = reservationManager.getEmployeeById(employeeId);
        Appointment appointment = new Appointment(date, employee, client);

        // Check for conflicting appointments
        if (reservationManager.hasConflictingAppointments(appointment)) {
            // Handle conflicting appointments, show error message or redirect to a different page
        } else {
            // Save the appointment using the reservationManager
            reservationManager.addAppointment(appointment);

            // Redirect or forward to appropriate page
            response.sendRedirect(request.getContextPath() + "/appointments");
        }
    }


    private void editAppointment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the form data
        Long id = Long.parseLong(request.getParameter("id"));
        String dateStr = request.getParameter("date");
        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Long clientId = Long.parseLong(request.getParameter("clientId"));
        Long employeeId = Long.parseLong(request.getParameter("employeeId"));

        // Perform validation
        // ...

        // Retrieve the existing appointment using its ID
        Appointment appointment = reservationManager.getAppointmentById(id);

        if (appointment != null) {
            // Update the appointment object with the new data
            Client client = reservationManager.getClientById(clientId);
            Employee employee = reservationManager.getEmployeeById(employeeId);
            appointment.setDate(date);
            appointment.setClient(client);
            appointment.setEmployee(employee);

            // Save the updated appointment using the reservationManager
            reservationManager.updateAppointment(appointment);
            // Redirect or forward to appropriate page
            response.sendRedirect(request.getContextPath() + "/appointments");
        } else {
            // Appointment not found, handle accordingly
        }
    }

        private void deleteAppointment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // Get the appointment ID to be deleted
            Long id = Long.parseLong(request.getParameter("id"));

            // Retrieve the existing appointment using its ID
            Appointment appointment = reservationManager.getAppointmentById(id);

            if (appointment != null) {
                // Delete the appointment using the reservationManager
                reservationManager.deleteAppointment(appointment);

                // Redirect or forward to appropriate page
                response.sendRedirect(request.getContextPath() + "/appointments");
            } else {
                // Appointment not found, handle accordingly
            }
        }

}