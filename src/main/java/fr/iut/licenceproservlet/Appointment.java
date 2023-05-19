package fr.iut.licenceproservlet;



import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Column(name = "duration")
    private int duration;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Appointment(LocalDateTime date,int duration, Employee employee, Client client) {
        this.date = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
        this.duration = duration;
        this.employee = employee;
        this.client = client;
    }

    public Appointment() {

    }

    public Appointment(LocalDateTime now, Employee employee, Client client)
    {
        this(now, 30, employee, client);
    }


    // Constructeurs, getters et setters

    // Méthode pour vérifier si un rendez-vous existe déjà pour un client et un employé donnés
    public static boolean existsForClientAndEmployee(EntityManager entityManager, Client client, Employee employee,
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
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public Employee getEmployee() {
        return employee;
    }

    public Client getClient() {
        return client;
    }

    public Long getId() {
        return id;
    }


    public void setDate(LocalDateTime dateTime) {
        this.date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDuration() {
        return String.valueOf(duration);
    }

    // Autres méthodes pour modifier, supprimer, etc. des rendez-vous
}
