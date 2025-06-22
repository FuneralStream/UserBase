package org.example;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final UserDao userDao = new UserDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            boolean running = true;
            while (running) {
                System.out.println("\nМеню сервиса пользователей:");
                System.out.println("1. Создать пользователя");
                System.out.println("2. Найти пользователя по ID");
                System.out.println("3. Вывести всех пользователей");
                System.out.println("4. Обновить пользователя");
                System.out.println("5. Удалить пользователя");
                System.out.println("6. Выйти");
                System.out.print("Выберите действие: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        findUserById();
                        break;
                    case 3:
                        listAllUsers();
                        break;
                    case 4:
                        updateUser();
                        break;
                    case 5:
                        deleteUser();
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            }
        } catch (Exception e) {
            logger.error("Произошла ошибка: ", e);
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
        }
    }

    private static void createUser() {
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
            userDao.save(user);
            System.out.println("Пользователь успешно создан с ID: " + user.getId());
        } catch (Exception e) {
            logger.error("Ошибка создания пользователя: ", e);
            System.out.println("Ошибка создания пользователя: " + e.getMessage());
        }
    }

    private static void findUserById() {
        System.out.print("Введите ID пользователя: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            var user = userDao.findById(id);
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

    private static void listAllUsers() {
        try {
            List<User> users = userDao.findAll();
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

    private static void updateUser() {
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            var userOptional = userDao.findById(id);
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

            userDao.update(user);
            System.out.println("Пользователь успешно обновлён.");
        } catch (Exception e) {
            logger.error("Ошибка обновления пользователя: ", e);
            System.out.println("Ошибка обновления пользователя: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            var userOptional = userDao.findById(id);
            if (userOptional.isEmpty()) {
                System.out.println("Пользователь не найден с ID: " + id);
                return;
            }

            userDao.delete(userOptional.get());
            System.out.println("Пользователь успешно удалён.");
        } catch (Exception e) {
            logger.error("Ошибка удаления пользователя: ", e);
            System.out.println("Ошибка удаления пользователя: " + e.getMessage());
        }
    }
}