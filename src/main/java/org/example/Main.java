package org.example;

import org.example.dao.UserDaoImpl;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl(new UserDaoImpl());
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            userService.handleUserCommand(choice);
            if (choice == UserService.EXIT) {
                running = false;
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\nМеню сервиса пользователей:");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти пользователя по ID");
        System.out.println("3. Вывести всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("6. Выйти");
        System.out.print("Выберите действие: ");
    }
}