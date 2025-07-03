import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
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
    private User testUser;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.shutdown();
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl();
        insertTestData();
    }

    private void insertTestData() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();

            testUser = User.builder()
                    .name("Daniil Medvedev")
                    .email("medvedev@mail.ru")
                    .age(29)
                    .build();

            session.persist(testUser);
            transaction.commit();
            session.refresh(testUser);
        }
    }

    private Optional<User> findById(long id) {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session.createQuery("FROM User WHERE id = :id", User.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.uniqueResult());
        }
    }

    @Test
    void saveTest() {
        User user = User.builder()
                .name("Andrey Rublev")
                .email("rublev@mail.ru")
                .age(27)
                .build();


        User savedUser = userDao.save(user);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Optional<User> dbUser = session.createQuery(
                            "FROM User WHERE email = :email", User.class)
                    .setParameter("email", user.getEmail())
                    .uniqueResultOptional();

            assertTrue(dbUser.isPresent());
            assertEquals("Andrey Rublev", dbUser.get().getName());
            assertEquals("rublev@mail.ru", dbUser.get().getEmail());
            assertEquals(27, dbUser.get().getAge());
        }
    }

    @Test
    void findByIdTest() {
        assertNotNull(userDao.findById((long) testUser.getId()));
    }

    @Test
    void findAllTest() {
        List<User> users = userDao.findAll();
        assertEquals(1, users.size());
    }

    @Test
    void updateTest() {
        testUser.setName("Novak");
        userDao.update(testUser);

        assertTrue(findById((long) testUser.getId()).isPresent());
        assertEquals("Novak", findById((long) testUser.getId()).get().getName());
    }

    @Test
    void deleteTest() {
        assertTrue(findById((long) testUser.getId()).isPresent());
        userDao.delete(testUser);
        assertTrue(findById((long) testUser.getId()).isEmpty());
    }
}