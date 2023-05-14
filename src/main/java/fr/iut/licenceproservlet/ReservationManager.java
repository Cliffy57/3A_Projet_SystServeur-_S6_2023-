package fr.iut.licenceproservlet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import fr.iut.licenceproservlet.exception.ClientEmployeOverlapException;
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

    public List<Appointment> getAppointment() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Appointment> query = em.createQuery("SELECT r FROM Appointment r ORDER BY r.date, r.employee.lastname," +
                " " +
                "r" +
                ".client.lastname", Appointment.class);
        List<Appointment> result = query.getResultList();
        em.close();
        return result;
    }

    public List<Appointment> getAppointmentByDate(LocalDateTime date) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Appointment> query = em.createQuery("SELECT r FROM Appointment r WHERE r.date = :date ORDER BY r" +
                ".employee.lastname, r.client.lastname", Appointment.class);
        query.setParameter("date", date);
        List<Appointment> result = query.getResultList();
        em.close();
        return result;
    }

    public List<Appointment> getAppointmentByEmploye(Employe employe) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Appointment> query = em.createQuery("SELECT r FROM Appointment r WHERE r.employee = :employe ORDER" +
                " BY r.date, r.client.lastname", Appointment.class);
        query.setParameter("employe", employe);
        List<Appointment> result = query.getResultList();
        em.close();
        return result;
    }

    public List<Appointment> getAppointmentByClient(Client client) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Appointment> query = em.createQuery("SELECT r FROM Appointment r WHERE r.client = :client ORDER BY" +
                " r.date, r.employee.lastname", Appointment.class);
        query.setParameter("client", client);
        List<Appointment> result = query.getResultList();
        em.close();
        return result;
    }

    public Appointment getAppointmentById(Long id) {
        EntityManager em = emf.createEntityManager();
        Appointment rendezVous = em.find(Appointment.class, id);
        em.close();
        return rendezVous;
    }

    public void ajouterAppointment(Appointment appointment) throws ClientEmployeOverlapException {
        if (isClientEmployeOverlap(appointment)) {
            throw new ClientEmployeOverlapException("Le client ou l'employé a déjà un rendez-vous prévu à cette date.");
        }
        Appointment rendezVous = new Appointment(appointment.getDate(), appointment.getEmploye(), appointment.getClient());
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(rendezVous);
        tx.commit();
        em.close();
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
        Employe employe = rendezVous.getEmploye();
        Client client = rendezVous.getClient();

        List<Appointment> rendezVousList = getAppointmentByDate(date);
        for (Appointment rv : rendezVousList) {
            if (!Objects.equals(rv.getId(), rendezVous.getId()) &&
                    (rv.getEmploye().equals(employe) || rv.getClient().equals(client))) {
                return true; // un chevauchement a été trouvé
            }
        }
        return false; // pas de chevauchement trouvé
    }

}