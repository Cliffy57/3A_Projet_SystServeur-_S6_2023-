package fr.iut.licenceproservlet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.iut.licenceproservlet.servlets.AppointmentServlet;
import fr.iut.licenceproservlet.utils.HibernateUtil;
import org.hibernate.Session;

import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class ReservationManager {

    private EntityManagerFactory emf;

    private static final Logger logger = Logger.getLogger(AppointmentServlet.class.getName());

    public ReservationManager() {
        emf = Persistence.createEntityManagerFactory("reservation-pu");
    }

    public void close() {
        emf.close();
    }

    public List<Appointment> getAppointments() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        List<Appointment> result = session.createQuery("from Appointment", Appointment.class).list();
        logger.log(Level.INFO,"Retrieved " + result.size() + " appointments by reservation manager");
        session.getTransaction().commit();
        session.close();
        return result;
    }
    public Appointment getAppointmentById(Long id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Appointment appointment = session.get(Appointment.class, id);
        session.getTransaction().commit();
        return appointment;
    }


    public Client getClientById(Long clientId) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Client client = session.get(Client.class, clientId);
        session.getTransaction().commit();
        return client;
    }

    public Employee getEmployeeById(Long employeeId) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Employee employee = session.get(Employee.class, employeeId);
        session.getTransaction().commit();
        return employee;
    }

    /**
     * Checks if there are any appointments for the given client or employee on the same day as the new appointment
     * that is UPDATED to the database.
     * @param client
     * @param employee
     * @param startDate
     * @param duration
     * @return
     */

    public boolean hasConflictingAppointments(Client client, Employee employee, LocalDateTime startDate, int duration, Long appointmentId) {
        // Calculate the end date of the new appointment
        LocalDateTime endDate = startDate.plusMinutes(duration);

        // Get the start and end of the day in question
        LocalDateTime startOfDay = startDate.withHour(0).withMinute(0);
        LocalDateTime endOfDay = startDate.withHour(23).withMinute(59);

        // Query the database for appointments of the client or the employee that occur on the same day as the new appointment
        // Exclude the appointment being updated
        String hql = "FROM Appointment WHERE " +
                "date BETWEEN :startOfDay AND :endOfDay AND " +
                "id != :appointmentId AND " +
                "(client.id = :clientId OR employee.id = :employeeId)";

        Session session = HibernateUtil.getSession();
        Query query = session.createQuery(hql);
        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);
        query.setParameter("clientId", client.getId());
        query.setParameter("employeeId", employee.getId());
        query.setParameter("appointmentId", appointmentId);

        List<Appointment> appointmentsOnSameDay = query.list();

        // Loop over the appointments and check if any of them overlap with the new appointment
        for (Appointment appointment : appointmentsOnSameDay) {
            LocalDateTime existingStartDate = appointment.getDate();
            LocalDateTime existingEndDate = existingStartDate.plusMinutes(appointment.getDuration());

            // Check if the new appointment starts or ends during the existing one
            if ((startDate.isEqual(existingStartDate) || startDate.isAfter(existingStartDate)) && startDate.isBefore(existingEndDate)
                    || (endDate.isAfter(existingStartDate) && endDate.isBefore(existingEndDate))
                    || (startDate.isBefore(existingStartDate) && endDate.isAfter(existingEndDate))) {
                logger.log(Level.WARNING,"Found a conflicting appointment: " + appointment);
                return true;
            }
        }

        // No conflicting appointments found
        return false;
    }




    public void updateAppointment(Appointment appointment) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Check for conflicting appointments
        if (hasConflictingAppointments(appointment.getClient(), appointment.getEmployee(), appointment.getDate(),
                appointment.getDuration(), appointment.getId())) {
            throw new IllegalStateException("Cannot update appointment. Conflicting appointment already exists.");
        }

        // Update the appointment
        session.update(appointment);
        session.getTransaction().commit();
    }


    public void deleteAppointment(Appointment appointment) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.remove(appointment);
        tx.commit();
        em.close();
    }

    public  List<Client> getClients() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        List<Client> result = session.createQuery("from Client", Client.class).list();
        logger.log(Level.INFO,"Retrieved " + result.size() + " clients by reservation manager");
        session.getTransaction().commit();
        session.close();
        return result;
    }
    public  List<Employee> getEmployees() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        List<Employee> result = session.createQuery("from Employee", Employee.class).list();
        logger.log(Level.INFO,"Retrieved " + result.size() + " employees by reservation manager");
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public List<Appointment> getAppointmentsSortedByEmployee() {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);
        query.orderBy(cb.asc(root.get("employee").get("lastname")), cb.asc(root.get("employee").get("firstname")));
        List<Appointment> result = em.createQuery(query).getResultList();
        em.close();
        return result;
    }

    public List<Appointment> getAppointmentsSortedByClient() {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);
        query.orderBy(cb.asc(root.get("client").get("lastname")), cb.asc(root.get("client").get("firstname")));
        List<Appointment> result = em.createQuery(query).getResultList();
        em.close();
        return result;
    }

    public List<Appointment> getAppointmentsSortedByDate() {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Appointment> query = cb.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);
        query.orderBy(cb.asc(root.get("date")));
        List<Appointment> result = em.createQuery(query).getResultList();
        em.close();
        return result;
    }

    /**
     * Checks if there are any appointments for the given client or employee on the same day as the new appointment
     * that is ADDED to the database.
     * @param client
     * @param employee
     * @param startDate
     * @param duration
     * @return
     */
    public boolean hasConflictingAppointments(Client client, Employee employee, LocalDateTime startDate, int duration) {
        // Calculate the end date of the new appointment
        LocalDateTime endDate = startDate.plusMinutes(duration);

        // Get the start and end of the day in question
        LocalDateTime startOfDay = startDate.withHour(0).withMinute(0);
        LocalDateTime endOfDay = startDate.withHour(23).withMinute(59);

        // Query the database for appointments of the client or the employee that occur on the same day as the new appointment
        String hql = "FROM Appointment WHERE " +
                "date BETWEEN :startOfDay AND :endOfDay AND " +
                "(client.id = :clientId OR employee.id = :employeeId)";

        Session session = HibernateUtil.getSession();
        Query query = session.createQuery(hql);
        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);
        query.setParameter("clientId", client.getId());
        query.setParameter("employeeId", employee.getId());

        List<Appointment> appointmentsOnSameDay = query.list();

        // Loop over the appointments and check if any of them overlap with the new appointment
        for (Appointment appointment : appointmentsOnSameDay) {
            LocalDateTime existingStartDate = appointment.getDate();
            LocalDateTime existingEndDate = existingStartDate.plusMinutes(appointment.getDuration());

            // Check if the new appointment starts or ends during the existing one
            if ((startDate.isEqual(existingStartDate) || startDate.isAfter(existingStartDate)) && startDate.isBefore(existingEndDate)
                    || (endDate.isAfter(existingStartDate) && endDate.isBefore(existingEndDate))
                    || (startDate.isBefore(existingStartDate) && endDate.isAfter(existingEndDate))) {
                logger.log(Level.WARNING,"Found a conflicting appointment: " + appointment);
                return true;
            }
        }

        // No conflicting appointments found
        return false;
    }

}