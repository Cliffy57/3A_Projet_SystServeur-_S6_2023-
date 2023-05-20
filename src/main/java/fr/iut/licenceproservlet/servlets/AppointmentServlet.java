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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "appointmentServlet", value = "/appointment-servlet")
public class AppointmentServlet extends HttpServlet {

    /**
     * The reservation manager.
     * This is the class that will handle the database operations.
     * It is instantiated in the init() method and closed in the destroy() method.
     * It is used in all the methods of the servlet.
     */
    private transient ReservationManager reservationManager;
    /**
     * The logger.
     * It is used to log messages to the console.
     * It is instantiated in the init() method and closed in the destroy() method.
     */
    private static final Logger logger = Logger.getLogger(AppointmentServlet.class.getName());

    private static final String APPOINTMENTS_PAGE = "/appointments";


    @Override
    public void init() throws jakarta.servlet.ServletException {
        super.init();
        reservationManager = new ReservationManager();
        logger.info("Appointment servlet initialized");
    }

    @Override
    public void destroy() {
        reservationManager.close();
        logger.info("Appointment servlet destroyed");
        super.destroy();
    }

    /**
     * @param request The request object
     * @param response The response object
     * @throws ServletException If the request for the GET could not be handled
     * @throws IOException If an input or output error is detected when the servlet handles the GET request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || "sort".equals(action)) {
            processAppointmentRequest(request, response, action);
        } else {
            processActionRequest(request, response, action);
        }
    }

    /**
     * @throws IOException
     *
     * This method is called when the action parameter is null or "sort".
     * It retrieves the list of appointments from the reservation manager and forwards it to the JSP.
     * If the action parameter is "sort", it sorts the list of appointments before forwarding it.
     * The sorting is done by the sortAppointments() method.
     */

    private void processAppointmentRequest(HttpServletRequest request, HttpServletResponse response, String action) throws ServletException, IOException {
        List<Appointment> appointments;

        if ("sort".equals(action)) {
            appointments = sortAppointments(request.getParameter("by"));
        } else {
            appointments = reservationManager.getAppointments();
        }

        logger.log(Level.INFO,"Retrieved {0} appointments by Servlet",appointments.size() );
        request.setAttribute("appointments", appointments);
        RequestDispatcher dispatcher = request.getRequestDispatcher("new_index.jsp");
        dispatcher.forward(request, response);
    }
    /**
      sortAppointments() method
        This method sorts the list of appointments according to the value of the sortBy parameter.
        The sortBy parameter can have the following values:
        - "date" (sort by date)
        - "client" (sort by client)
        - "employee" (sort by employee)
        - any other value (no sorting)
     */
    private List<Appointment> sortAppointments(String sortBy) {
        switch (sortBy) {
            case "date":
                return reservationManager.getAppointmentsSortedByDate();
            case "client":
                return reservationManager.getAppointmentsSortedByClient();
            case "employee":
                return reservationManager.getAppointmentsSortedByEmployee();
            default:
                return reservationManager.getAppointments();
        }
    }
    /**
        processActionRequest() method
            This method is called when the action parameter is not null and not "sort".
            It retrieves the value of the action parameter and calls the appropriate method.
            The action parameter can have the following values:
            - "new" (show the new appointment form)
            - "modify" (show the edit appointment form)
            - any other value (handle the action accordingly)
     */
    private void processActionRequest(HttpServletRequest request, HttpServletResponse response, String action) throws ServletException, IOException {
        switch (action) {
            case "new":
                showNewAppointmentForm(request, response);
                break;
            case "modify":
                modifyAppointment(request, response);
                break;
            default:
                // Handle unexpected action values accordingly
        }
    }
    /**
        modifyAppointment() method
            This method is called when the action parameter is "modify".
            It retrieves the value of the id parameter and calls the prepareAndForwardToEditAppointment() method.
            If the id parameter is null, it sends an error response with the status code 400 (Bad Request).
            If the id parameter is not null but the appointment is not found, it sends an error response with the status code 404 (Not Found).
     */
    private void modifyAppointment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            Long id = Long.parseLong(idStr);
            Appointment appointment = reservationManager.getAppointmentById(id);
            if (appointment != null) {
                prepareAndForwardToEditAppointment(request, response, appointment);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Appointment not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No appointment ID provided");
        }
    }
    /**
        prepareAndForwardToEditAppointment() method
            This method prepares the request attributes and forwards the request to the editAppointment.jsp page.
            It is called by the modifyAppointment() method.
     */
    private void prepareAndForwardToEditAppointment(HttpServletRequest request, HttpServletResponse response, Appointment appointment) throws ServletException, IOException {
        request.setAttribute("clients", reservationManager.getClients());
        request.setAttribute("employees", reservationManager.getEmployees());
        request.setAttribute("appointment", appointment);
        request.getRequestDispatcher("/editAppointment.jsp").forward(request, response);
    }
    /**
        showNewAppointmentForm() method
            This method prepares the request attributes and forwards the request to the addAppointment.jsp page.
            It is called by the processActionRequest() method.
     */
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
    /**
     doPost method
        This method is called when the HTTP POST request is sent to the servlet.
        It retrieves the value of the action parameter and calls the appropriate method.
        The action parameter can have the following values:
        - "add" (add a new appointment)
        - "update" (update an existing appointment)
        - "delete" (delete an existing appointment)
        - any other value (handle the action accordingly)
     */
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
        }  else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
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
            // Handle conflicting appointments, show error message or redirect to a different page;
            logger.log(Level.INFO,"Conflicting appointments");
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
            response.sendRedirect(request.getContextPath() + APPOINTMENTS_PAGE);
        }
    }


    private void editAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            response.sendRedirect(request.getContextPath() + APPOINTMENTS_PAGE);
        } else {
            // Appointment not found, handle accordingly
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Appointment not found");
        }
    }

    private void deleteAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the appointment ID to be deleted
        Long id = Long.parseLong(request.getParameter("id"));

        // Retrieve the existing appointment using its ID
        Appointment appointment = reservationManager.getAppointmentById(id);

        if (appointment != null) {
            // Delete the appointment using the reservationManager
            reservationManager.deleteAppointment(appointment);

            // Redirect or forward to appropriate page
            response.sendRedirect(request.getContextPath() + APPOINTMENTS_PAGE);
        }  else {
            // Appointment not found, throw an exception or show an error page
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Appointment not found");
        }

    }

}
