import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    private static UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
        System.setProperty("hibernate.hbm2ddl.auto", "create");
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.shutdown();
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl();
        clearDB();
    }

    private void clearDB() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        }
    }

    @Test
    void saveTest() {
        User user = User.builder()
                .name("Daniil Medvedev")
                .email("medvedev@mail.ru")
                .age(29)
                .build();


        User savedUser = userDao.save(user);
        Optional<User> dbUser = userDao.findById((long) savedUser.getId());

        assertTrue(dbUser.isPresent());
        assertEquals(user.getName(), dbUser.get().getName());
        assertEquals(user.getEmail(), dbUser.get().getEmail());
        assertEquals(user.getAge(), dbUser.get().getAge());
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
        User user = User.builder()
                .name("Hamad Medjedovic")
                .email("medjedovic@mail.ru")
                .age(21)
                .build();

        User savedUser = userDao.save(user);
        assertTrue(userDao.findById((long) savedUser.getId()).isPresent());


        userDao.delete(savedUser);
        assertTrue(userDao.findById((long) savedUser.getId()).isEmpty());
    }
}