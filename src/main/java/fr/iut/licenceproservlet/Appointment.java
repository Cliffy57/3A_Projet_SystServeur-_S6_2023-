package fr.iut.licenceproservlet;



import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date",columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(name = "duration")
    private int duration;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Appointment(LocalDateTime date,int duration, Employee employee, Client client) {
        this.date = date;
        this.duration = duration;
        this.employee = employee;
        this.client = client;
    }

    public Appointment() {

    }



    // Constructeurs, getters et setters

    public LocalDateTime getDate() {
        return date;
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
        this.date = dateTime;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getDuration() {
        return duration;
    }
}
