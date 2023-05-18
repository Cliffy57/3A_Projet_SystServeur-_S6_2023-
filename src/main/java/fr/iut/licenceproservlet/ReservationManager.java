package fr.iut.licenceproservlet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import fr.iut.licenceproservlet.exception.ClientEmployeOverlapException;
import fr.iut.licenceproservlet.utils.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class ReservationManager {

    private EntityManagerFactory emf;

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
        System.out.println("Retrieved " + result.size() + " appointments by reservation manager");
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

    public List<Appointment> getAppointmentByEmploye(Employee employee) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Appointment> query = em.createQuery("SELECT r FROM Appointment r WHERE r.employee = :employe ORDER" +
                " BY r.date, r.client.lastname", Appointment.class);
        query.setParameter("employe", employee);
        List<Appointment> result = query.getResultList();
        em.close();
        return result;
    }




    public void modifierAppointment(Appointment rendezVous) throws ClientEmployeOverlapException {
        if (isClientEmployeOverlap(rendezVous)) {
            throw new ClientEmployeOverlapException("Le client ou l'employé a déjà un rendez-vous prévu à cette date.");
        }
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(rendezVous);
        tx.commit();
        em.close();

    }

    private boolean isClientEmployeOverlap(Appointment rendezVous) {
        LocalDateTime date = rendezVous.getDate();
        Employee employee = rendezVous.getEmployee();
        Client client = rendezVous.getClient();

        List<Appointment> rendezVousList = getAppointmentByDate(date);
        for (Appointment rv : rendezVousList) {
            if (!Objects.equals(rv.getId(), rendezVous.getId()) &&
                    (rv.getEmployee().equals(employee) || rv.getClient().equals(client))) {
                return true; // un chevauchement a été trouvé
            }
        }
        return false; // pas de chevauchement trouvé
    }

    private List<Appointment> getAppointmentByDate(LocalDateTime date) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        List<Appointment> result = session.createQuery("FROM Appointment WHERE date = :date ORDER BY employee.lastname, client.lastname", Appointment.class)
                .setParameter("date", date)
                .getResultList();
        session.getTransaction().commit();
        return result;
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

    public boolean hasConflictingAppointments(Appointment appointment) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Check if there are any conflicting appointments for the same client and employee
        List<Appointment> conflictingAppointments = session.createQuery(
                        "SELECT a FROM Appointment a WHERE a.client = :client AND a.employee = :employee AND a.date = :date AND a.id != :id",
                        Appointment.class)
                .setParameter("client", appointment.getClient())
                .setParameter("employee", appointment.getEmployee())
                .setParameter("date", appointment.getDate())
                .setParameter("id", appointment.getId())
                .getResultList();

        session.getTransaction().commit();

        return !conflictingAppointments.isEmpty();
    }

    public void addAppointment(Appointment appointment) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Check for conflicting appointments
        if (hasConflictingAppointments(appointment)) {
            throw new IllegalStateException("Cannot add appointment. Conflicting appointment already exists.");
        }

        // Save the appointment
        session.save(appointment);
        session.getTransaction().commit();
    }

    public void updateAppointment(Appointment appointment) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Check for conflicting appointments
        if (hasConflictingAppointments(appointment)) {
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

}