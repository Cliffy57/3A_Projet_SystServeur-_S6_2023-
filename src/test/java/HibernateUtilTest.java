import fr.iut.licenceproservlet.Appointment;
import fr.iut.licenceproservlet.Client;
import fr.iut.licenceproservlet.Employee;
import fr.iut.licenceproservlet.utils.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

public class HibernateUtilTest {

    private HibernateUtil hibernateUtils;
    @Test
    public void testSaveClient() {
      Session session = HibernateUtil.getSession();
      session.beginTransaction();
        Client john = new Client("Doe", "John", "johndoe@hotmail.com");
        //add the client to the client table
        session.save(john);

      List<Client> clients = session.createQuery("from Client", Client.class).list();
      for(Client client : clients) {
        System.out.println(client.getNom());
      }
      session.getTransaction().commit();
    }
    @Test
    public void testRetrieveAllAppointments() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        List<Appointment> appointments = session.createQuery("from Appointment", Appointment.class).list();
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getId());
            System.out.println("Date: " + appointment.getDate());
            System.out.println("Client: " + appointment.getClient());
            System.out.println("Employee: " + appointment.getEmployee());
            System.out.println("---------------------------");
        }

        session.getTransaction().commit();
    }



}
