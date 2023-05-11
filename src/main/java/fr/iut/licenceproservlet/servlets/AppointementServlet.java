package fr.iut.licenceproservlet.servlets;

import fr.iut.licenceproservlet.Appointment;
import fr.iut.licenceproservlet.ReservationManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class AppointementServlet extends HttpServlet {

    private ReservationManager reservationManager;

    public void init() throws ServletException {
        super.init();
        reservationManager = new ReservationManager();
    }

    public void destroy() {
        reservationManager.close();
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Appointment> appointment = reservationManager.getAppointment();
        request.setAttribute("appointment", appointment);
        request.getRequestDispatcher("/appointment.jsp").forward(request, response);
    }
}
