package fr.iut.licenceproservlet;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "firstname")

    private String firstname;
    @Column(name = "email")

    private String email;

    // constructeurs, getters et setters

    public Employee() {
    }

    public Employee(String lastname, String surname, String email) {
        this.lastname = lastname;
        this.firstname = surname;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {return lastname;
    }

    public void setName(String employeeName) {
        this.lastname = employeeName;
    }
}
