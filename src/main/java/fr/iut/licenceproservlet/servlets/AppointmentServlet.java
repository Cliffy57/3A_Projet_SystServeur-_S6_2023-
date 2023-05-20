package fr.iut.licenceproservlet.servlets;

import fr.iut.licenceproservlet.Appointment;
import fr.iut.licenceproservlet.Client;
import fr.iut.licenceproservlet.Employee;
import fr.iut.licenceproservlet.ReservationManager;
import fr.iut.licenceproservlet.utils.HibernateUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;

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
        String action = request.getParameter("action");

        if (action == null || "sort".equals(action)) {
            List<Appointment> appointments;

            if ("sort".equals(action)) {
                String sortBy = request.getParameter("by");
                switch (sortBy) {
                    case "date":
                        appointments = reservationManager.getAppointmentsSortedByDate();
                        break;
                    case "client":
                        appointments = reservationManager.getAppointmentsSortedByClient();
                        break;
                    case "employee":
                        appointments = reservationManager.getAppointmentsSortedByEmployee();
                        break;
                    default:
                        appointments = reservationManager.getAppointments();
                        break;
                }
            } else {
                appointments = reservationManager.getAppointments();
            }

            System.out.println("Retrieved " + appointments.size() + " appointments by Servlet");

            // Set the appointments as an attribute in the request scope
            request.setAttribute("appointments", appointments);

            // Forward the request to the JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("new_index.jsp");
            dispatcher.forward(request, response);
        } else {
            switch (action) {
                case "new":
                    showNewAppointmentForm(request, response);
                    break;
                case "modify":
                    // handle modify action (show modify appointment form)
                    String idStr = request.getParameter("id");
                    if (idStr != null) {
                        Long id = Long.parseLong(idStr);
                        Appointment appointment = reservationManager.getAppointmentById(id);
                        if (appointment != null) {
                            // Set the clients as an attribute in the request scope
                            request.setAttribute("clients", reservationManager.getClients());
                            // Set the employees as an attribute in the request scope
                            request.setAttribute("employees", reservationManager.getEmployees());
                            // Set the appointment as an attribute in the request scope
                            request.setAttribute("appointment", appointment);
                            request.getRequestDispatcher("/editAppointment.jsp").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Appointment not found");
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No appointment ID provided");
                    }
                    break;
                default:
                    // Handle unexpected action values accordingly
            }
        }
    }


    private void showNewAppointmentForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        List<Client> clients = session.createQuery("from Client", Client.class).list();
        List<Employee> employees = session.createQuery("from Employee", Employee.class).list();

        session.getTransaction().commit();

        request.setAttribute("clients", clients);
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("/addAppointment.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "add":
                    addAppointment(request, response);
                    break;
                case "update":
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
        int duration = Integer.parseInt(request.getParameter("duration"));
        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Long clientId = Long.parseLong(request.getParameter("client"));
        Long employeeId = Long.parseLong(request.getParameter("employee"));

        // Get session
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Fetch existing client
        Client client = reservationManager.getClientById(clientId);

        // Fetch existing employee
        Employee employee = reservationManager.getEmployeeById(employeeId);

        // Check for conflicting appointments
        if (reservationManager.hasConflictingAppointments(client, employee, date)) {
            // Handle conflicting appointments, show error message or redirect to a different page
            System.out.println("Conflicting appointments");
            request.setAttribute("error", "Appointment already exists");
            // Fetch appointments and set as attribute
            List<Appointment> appointments = reservationManager.getAppointments();
            request.setAttribute("appointments", appointments);
            // Forward to JSP
            request.getRequestDispatcher("/new_index.jsp").forward(request, response);
        } else {
            // Create a new appointment object
            Appointment appointment = new Appointment(date, duration, employee, client);

            // Save the appointment
            session.save(appointment);

            // Commit transaction and close session
            session.getTransaction().commit();

            // Redirect or forward to appropriate page
            response.sendRedirect(request.getContextPath() + "/appointments");
        }
    }


    private void editAppointment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the form data
        Long id = Long.parseLong(request.getParameter("id"));
        String dateStr = request.getParameter("date");
        LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Long clientId = Long.parseLong(request.getParameter("client_id"));
        Long employeeId = Long.parseLong(request.getParameter("employee_id"));


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
