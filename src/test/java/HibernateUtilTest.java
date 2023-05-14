import fr.iut.licenceproservlet.Client;
import fr.iut.licenceproservlet.utils.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
}
