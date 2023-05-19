package fr.iut.licenceproservlet;

import javax.persistence.*;

import javax.persistence.*;

@Entity
@Table(name = "client")
public class Client {

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

    public Client() {
    }

    public Client(String lastname, String firstname, String email) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return lastname;
    }

    public void setNom(String name) {
        this.lastname = name;
    }

    public String getPrename() {
        return firstname;
    }

    public void setPrename(String surname) {
        this.firstname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstname;
    }

    public String getLastName() {
        return lastname;
    }


    public void setName(String clientName) {
        this.lastname = clientName;
    }
}
