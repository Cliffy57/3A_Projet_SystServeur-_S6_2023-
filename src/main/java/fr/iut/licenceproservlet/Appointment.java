package fr.iut.licenceproservlet;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employe employee;

    public Appointment(LocalDateTime date, Employe employe, Client client) {
    }

    public Appointment() {

    }

    public Appointment(Date date, Employe employe, Client client) {
    }

    // Constructeurs, getters et setters

    // Méthode pour vérifier si un rendez-vous existe déjà pour un client et un employé donnés
    public static boolean existsForClientAndEmployee(EntityManager entityManager, Client client, Employe employee,
                                                     Date date) {
        List<Appointment> appointments = entityManager.createQuery("SELECT a FROM Appointment a WHERE a.client = :client AND a.employee = :employee AND a.date = :date", Appointment.class)
                .setParameter("client", client)
                .setParameter("employee", employee)
                .setParameter("date", date)
                .getResultList();
        return !appointments.isEmpty();
    }

    // Méthodes pour trier les rendez-vous par date, employé, client
    public static List<Appointment> findByDate(EntityManager entityManager) {
        return entityManager.createQuery("SELECT a FROM Appointment a ORDER BY a.date", Appointment.class).getResultList();
    }

    public static List<Appointment> findByEmployee(EntityManager entityManager) {
        return entityManager.createQuery("SELECT a FROM Appointment a ORDER BY a.employee.lastname", Appointment.class).getResultList();
    }

    public static List<Appointment> findByClient(EntityManager entityManager) {
        return entityManager.createQuery("SELECT a FROM Appointment a ORDER BY a.client.lastname", Appointment.class).getResultList();
    }

    // Méthode pour récupérer un rendez-vous par ID
    public static Appointment findById(EntityManager entityManager, Long id) {
        return entityManager.find(Appointment.class, id);
    }

    public LocalDateTime getDate() {
        return getDate();
    }

    public Employe getEmploye() {
        return employee;
    }

    public Client getClient() {
        return client;
    }

    public Long getId() {
        return id;
    }

    // Autres méthodes pour modifier, supprimer, etc. des rendez-vous
}
