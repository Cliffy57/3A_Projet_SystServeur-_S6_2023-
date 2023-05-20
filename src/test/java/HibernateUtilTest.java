import fr.iut.licenceproservlet.Appointment;
import fr.iut.licenceproservlet.Client;
import fr.iut.licenceproservlet.Employee;
import fr.iut.licenceproservlet.utils.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class HibernateUtilTest {

    /**
     * Test the insertion of a client
     * This test is not really useful, it's just to show you how to insert a client
      */
    @Test
    void testSaveClient() {
      Session session = HibernateUtil.getSession();
      session.beginTransaction();
        Client john = new Client("Doe", "John", "johndoe@hotmail.com");
        //add the client to the client table
        session.save(john);

      List<Client> clients = session.createQuery("from Client", Client.class).list();
      for(Client client : clients) {
        System.out.println(client.getLastName());
      }
      session.getTransaction().commit();
    }

    /**
     * Test the insertion of an appointment
     * This test is not really useful, it's just to show you how to insert an appointment
     */
    @Test
    void testSaveAppointment() {
        Session session = HibernateUtil.getSession();
        session.clear(); // Clear the session to ensure it's in sync with the database
        session.beginTransaction();

        List<Client> clients = session.createQuery("from Client", Client.class).list();
        List<Employee> employees = session.createQuery("from Employee", Employee.class).list();

        //get a random client from clients
        Client client = clients.get(1);
        //get a random employee from employees
        Employee employee = employees.get(1);
        //get a duration
        int duration = 1;

        Appointment appointment = new Appointment(LocalDateTime.now(),duration,employee,client);

        //add the appointment to the appointment table
        session.save(appointment);

        System.out.println("Appointment ID: " + appointment.getId());
        System.out.println("Date: " + appointment.getDate());
        System.out.println("Client: " + appointment.getClient());
        System.out.println("Employee: " + appointment.getEmployee());


        session.getTransaction().commit();
    }

    /**
     * Test the retrieval of all appointments
     * This test is not really useful, it's just to show you how to retrieve all appointments
     * from the database
     * You can use this method to check if your appointment has been saved
     */
    @Test
    void testRetrieveAllAppointments() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        List<Appointment> appointments = session.createQuery("from Appointment", Appointment.class).list();
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getId());
            System.out.println("Date: " + appointment.getDate());
            System.out.println("Duration: " + appointment.getDuration());
            System.out.println("Client: " + appointment.getClient());
            System.out.println("Employee: " + appointment.getEmployee());
            System.out.println("---------------------------");
        }

        session.getTransaction().commit();
    }




}
