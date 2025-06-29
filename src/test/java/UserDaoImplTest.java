import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoImplTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    private static UserDao userDao;

    @BeforeAll
    static void setUp() {
        postgres.start();
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        userDao = new UserDaoImpl();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void clearDB() {
        Transaction transaction = null;
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Test
    void saveTest() {
        User user = User.builder()
                .name("Daniil Medvedev")
                .email("medvedev@mail.ru")
                .age(29)
                .build();

        userDao.save(user);
        assertTrue(user.getId() >= 0);
        assertEquals("Daniil Medvedev", user.getName());
    }

    @Test
    void findByIdTest() {
        User user = userDao.save(User.builder()
                .name("Andrey Rublev")
                .email("rublev@mail.ru")
                .age(27)
                .build());


        assertNotNull(userDao.findById((long) user.getId()));
    }

    @Test
    void findAllTest() {
        userDao.save(User.builder()
                .name("Alexander Bublik")
                .email("bublik@mail.ru")
                .age(28)
                .build());

        userDao.save(User.builder()
                .name("Jannik Sinner")
                .email("sinner@mail.ru")
                .age(23)
                .build());

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void updateTest() {
        User user = userDao.save(User.builder()
                .name("Novak Djokovic")
                .email("djokovic@mail.ru")
                .age(36)
                .build());

        user.setName("Novak");
        userDao.update(user);

        assertTrue((userDao.findById((long) user.getId()).isPresent()));
        assertEquals("Novak", userDao.findById((long) user.getId()).get().getName());
    }

    @Test
    void deleteTest() {
        User user = userDao.save(User.builder()
                .name("Hamad Medjedovic")
                .email("medjedovic@mail.ru")
                .age(21)
                .build());

        userDao.delete(user);
        assertTrue(userDao.findById((long) user.getId()).isEmpty());
    }
}