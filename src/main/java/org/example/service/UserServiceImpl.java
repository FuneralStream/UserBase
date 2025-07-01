package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private static final Scanner scanner = new Scanner(System.in);

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

    @Override
    public void handleUserCommand(int command) {
        switch (command) {
            case CREATE_USER -> createUser();
            case FIND_USER_BY_ID -> findUserById();
            case LIST_ALL_USERS -> listAllUsers();
            case UPDATE_USER -> updateUser();
            case DELETE_USER -> deleteUser();
            case EXIT -> {
                System.out.println("Выход из программы");
                HibernateUtil.shutdown();
                scanner.close();
            }
            default -> System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
        }
    }

    private void createUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите возраст: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        try {
            save(user);
            System.out.println("Пользователь успешно создан с ID: " + user.getId());
        } catch (Exception e) {
            logger.error("Ошибка создания пользователя: ", e);
            System.out.println("Ошибка создания пользователя: " + e.getMessage());
        }
    }

    private void findUserById() {
        System.out.print("Введите ID пользователя: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            var user = findById(id);
            if (user.isPresent()) {
                System.out.println("Пользователь найден: " + user.get());
            } else {
                System.out.println("Пользователь не найден с ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Ошибка поиска пользователя: ", e);
            System.out.println("Ошибка поиска пользователя: " + e.getMessage());
        }
    }

    private void listAllUsers() {
        try {
            List<User> users = findAll();
            if (users.isEmpty()) {
                System.out.println("Пользователи не найдены.");
            } else {
                System.out.println("Список пользователей:");
                users.forEach(System.out::println);
            }
        } catch (Exception e) {
            logger.error("Ошибка вывода пользователей: ", e);
            System.out.println("Ошибка вывода пользователей: " + e.getMessage());
        }
    }

    private void updateUser() {
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            var userOptional = findById(id);
            if (userOptional.isEmpty()) {
                System.out.println("Пользователь не найден с ID: " + id);
                return;
            }

            User user = userOptional.get();

            System.out.print("Введите новое имя (текущее: " + user.getName() + "): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                user.setName(name);
            }

            System.out.print("Введите новый email (текущий: " + user.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) {
                user.setEmail(email);
            }

            System.out.print("Введите новый возраст (текущий: " + user.getAge() + "): ");
            String ageInput = scanner.nextLine();
            if (!ageInput.isEmpty()) {
                user.setAge(Integer.parseInt(ageInput));
            }

            update(user);
            System.out.println("Пользователь успешно обновлён.");
        } catch (Exception e) {
            logger.error("Ошибка обновления пользователя: ", e);
            System.out.println("Ошибка обновления пользователя: " + e.getMessage());
        }
    }

    private void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            var userOptional = findById(id);
            if (userOptional.isEmpty()) {
                System.out.println("Пользователь не найден с ID: " + id);
                return;
            }

            delete(userOptional.get());
            System.out.println("Пользователь успешно удалён.");
        } catch (Exception e) {
            logger.error("Ошибка удаления пользователя: ", e);
            System.out.println("Ошибка удаления пользователя: " + e.getMessage());
        }
    }
}