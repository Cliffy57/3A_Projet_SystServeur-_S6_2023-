package fr.iut.licenceproservlet;

import java.time.LocalDateTime;
import java.util.List;

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

    public boolean hasConflictingAppointments(Client client, Employee employee, LocalDateTime date) {
        // Query the database for appointments with the same date and either the same client or the same employee
        String hql = "FROM Appointment WHERE date = :date AND (client.id = :clientId OR employee.id = :employeeId)";
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery(hql);
        query.setParameter("date", date);
        query.setParameter("clientId", client.getId());
        query.setParameter("employeeId", employee.getId());

        List<Appointment> conflictingAppointments = query.list();
        System.out.printf("Found %d conflicting appointments%n", conflictingAppointments.size());
        System.out.println(conflictingAppointments);
        // Return true if conflicting appointments are found, false otherwise
        return !conflictingAppointments.isEmpty();
    }


        public void updateAppointment(Appointment appointment) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        // Check for conflicting appointments
        if (hasConflictingAppointments(appointment.getClient(), appointment.getEmployee(), appointment.getDate())) {
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
        System.out.println("Retrieved " + result.size() + " clients by reservation manager");
        session.getTransaction().commit();
        session.close();
        return result;
    }
    public  List<Employee> getEmployees() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        List<Employee> result = session.createQuery("from Employee", Employee.class).list();
        System.out.println("Retrieved " + result.size() + " employees by reservation manager");
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


}