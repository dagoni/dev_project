package ru.rtk;

import ru.rtk.model.Product;
import ru.rtk.model.Customer;
import ru.rtk.model.Order;
import ru.rtk.model.OrderInfo;
import ru.rtk.repository.ProductRepository;
import ru.rtk.repository.CustomerRepository;
import ru.rtk.repository.OrderRepository;

import java.sql.Connection;
import java.util.List;

public class App {
    private Connection connection; // Соединение с БД
    private ProductRepository productRepository; // Репозиторий для работы с товарами
    private CustomerRepository customerRepository; // Репозиторий для работы с покупателями
    private OrderRepository orderRepository; // Репозиторий для работы с заказами

    public static void main(String[] args) {
        App app = new App();
        try {
            app.run();
        } catch (Exception e) {
            System.err.println("Ошибка при работе приложения: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Запуск приложения
    public void run() throws Exception {
        System.out.println("=== Запуск приложения учета заказов ===");

        // Подключение к БД и миграции
        connectToDatabase();
        runMigrations();

        // Инициализация репозиториев
        productRepository = new ProductRepository(connection); // Создаем новый репозиторий для товаров
        customerRepository = new CustomerRepository(connection); // Создаем новый репозиторий для покупателей
        orderRepository = new OrderRepository(connection); // Создаем новый репозиторий для заказов

        // Демонстрация CRUD операций
        demonstrateCRUDOperations();

        // Закрытие соединения
        closeConnection();

        System.out.println("=== Приложение завершило работу ===");
    }

    private void connectToDatabase() throws Exception {
        ru.rtk.DatabaseConfig config = new ru.rtk.DatabaseConfig(); // Создаем конфигурацию для подключения к БД
        connection = config.getConnection(); // Получаем соединение с БД
        System.out.println("Подключение к БД успешно установлено!");
    }

    private void runMigrations() {
        System.out.println("\n--- Запуск миграций Flyway ---");
        ru.rtk.FlywayMigrator migrator = new ru.rtk.FlywayMigrator(); // Создаем новый миграцию
        migrator.migrate(connection); // Запускаем миграции
        System.out.println("Миграции выполнены успешно!");
    }

    private void demonstrateCRUDOperations() throws Exception {
        System.out.println("\n--- Демонстрация CRUD операций ---");
        // Открываем транзакцию
        try {
            // 1. CREATE - Вставка новых данных
            System.out.println("\n=== CREATE ОПЕРАЦИИ ===");
            insertNewProductAndCustomer(); //

            // 2. READ - Чтение данных
            System.out.println("\n=== READ ОПЕРАЦИИ ===");
            readLastFiveOrders(); // Чтение последних 5 заказов
            demonstrateReadOperations(); // Демонстрация чтения данных

            // 3. UPDATE - Обновление данных
            System.out.println("\n=== UPDATE ОПЕРАЦИИ ===");
            updateProductPriceAndQuantity(); // Обновление цены и количества
            demonstrateUpdateOperations();  // Демонстрация обновления данных

            // 4. DELETE - Удаление данных
            System.out.println("\n=== DELETE ОПЕРАЦИИ ===");
            demonstrateDeleteOperations(); // Демонстрация удаления данных
            deleteTestRecords();    // Удаление тестовых записей

            connection.commit(); // Коммит транзакции
            System.out.println("\n=== ВСЕ CRUD ОПЕРАЦИИ ВЫПОЛНЕНЫ УСПЕШНО! ===");

        } catch (Exception e) {
            System.err.println("Ошибка при выполнении операций: " + e.getMessage());
            connection.rollback(); // Откат транзакции
            System.err.println("Транзакция откачена!");
            throw e;
        }
    }

    private void insertNewProductAndCustomer() throws Exception {
        System.out.println("\n1. CREATE: Вставка нового товара и покупателя");

        // Вставка нового товара
        Product newProduct = new Product(); // Создаем новый товар
        newProduct.setDescription("Новый планшет Samsung");
        newProduct.setPrice(35000.00); // Цена
        newProduct.setQuantity(20); // Количество
        newProduct.setCategory("Электроника");  // Категория

        int productId = productRepository.insertProduct(newProduct); // Вставляем товар в БД


        //System.out.println("Добавлен новый товар: " + newProduct.getDescription() + " (ID: " + productId + ")");

        if (productId <= 0) {
            System.out.println("Не удалось вставить новый товар: возвращён ID = " + productId);
        } else {
            System.out.println("Добавлен новый товар: " + newProduct.getDescription() + " (ID: " + productId + ")");
        }

        // Вставка нового покупателя
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Андрей");
        newCustomer.setLastName("Тестовый");
        newCustomer.setPhone("+79011234567");
        newCustomer.setEmail("testoviy@mail.ru");
        // Создаем нового покупателя
        int customerId = customerRepository.insertCustomer(newCustomer);
        System.out.println("Добавлен новый клиент: " + newCustomer.getFirstName() + " " +
                newCustomer.getLastName() + " (ID: " + customerId + ")");

        // Создание заказа для нового покупателя
        Order newOrder = new Order();
        newOrder.setProductId(productId);
        newOrder.setCustomerId(customerId);
        newOrder.setQuantity(1);
        newOrder.setStatusId(1); // Новый заказ

        orderRepository.insertOrder(newOrder);
        System.out.println("Создан новый заказ для клиента");
    }
    // Чтение данных и демонстрация операций
    private void readLastFiveOrders() throws Exception {
        System.out.println("\n2. READ: Последние 5 заказов");

        List<OrderInfo> orders = orderRepository.getLastFiveOrders();
        // Формируем таблицу с информацией о заказах
        //System.out.println("Последние 5 заказов в таблице:");
        System.out.println("┌────┬────────────┬──────────────────┬──────────────────────┬────────┬───────────┬─────────────────┐");
        System.out.println("│ ID │ Дата       │ Клиент           │ Товар                │ Кол-во │ Стоимость │ Статус          │");
        System.out.println("├────┼────────────┼──────────────────┼──────────────────────┼────────┼───────────┼─────────────────┤");
        // Выводим информацию о заказах
        for (OrderInfo order : orders) {
            System.out.printf("│ %-2d │ %-10s │ %-16s │ %-20s │ %-6d │ %-9.2f │ %-10s   │%n",
                    order.getOrderId(), order.getOrderDate(),
                    order.getCustomerName(),
                    (order.getProductDescription().length() > 20 ?
                            order.getProductDescription().substring(0, 17) + "..." : order.getProductDescription()),
                    order.getQuantity(), order.getTotalPrice(), order.getStatus());
        }
        System.out.println("└────┴────────────┴──────────────────┴──────────────────────┴────────┴───────────┴─────────────────┘");
    }
    // Дополнительные операции чтения
    private void demonstrateReadOperations() throws Exception {
        System.out.println("\n3. READ: Дополнительные операции чтения");

        // Товары с малым количеством
        List<Product> lowStockProducts = productRepository.getProductsLowInStock(10);
        System.out.println("Товары с количеством менее 10:");
        if (lowStockProducts.isEmpty()) {
            System.out.println("   Нет товаров с малым количеством");
        } else {
            for (Product product : lowStockProducts) {
                System.out.printf("   • %s (осталось: %d)%n",
                        product.getDescription(), product.getQuantity());
            }
        }

        // Клиенты без заказов
        List<Customer> customersWithoutOrders = customerRepository.getCustomersWithoutOrders();
        System.out.println("\nКлиенты без заказов:");
        if (customersWithoutOrders.isEmpty()) {
            System.out.println("   Все клиенты имеют заказы");
        } else {
            for (Customer customer : customersWithoutOrders) {
                System.out.printf("   • %s %s%n",
                        customer.getFirstName(), customer.getLastName());
            }
        }
    }
    // Обновление цен и количества
    private void updateProductPriceAndQuantity() throws Exception {
        System.out.println("\n4. UPDATE: Обновление цены и количества товара");

        // Обновление цены товара
        productRepository.updateProductPrice(1, 52000.00);
        System.out.println("Цена ноутбука HP обновлена: 50 000 → 52 000 руб.");

        // Обновление количества товара
        productRepository.updateProductQuantity(1, 5);
        System.out.println("Количество ноутбуков HP обновлено: 10 → 5 шт.");
    }
    // Дополнительные операции обновления
    private void demonstrateUpdateOperations() throws Exception {
        System.out.println("\n5. UPDATE: Дополнительные операции обновления");

        // Повышение цен на электронику
        int updatedCount = productRepository.updateProductPriceForCategory("Электроника", 1.05);
        System.out.println("Повышены цены на электронику (+5%): затронуто " + updatedCount + " товаров");

        // Обновление статуса заказа
        orderRepository.updateOrderStatus(1, 3); // Статус "Отправлен"
        System.out.println("Статус заказа ID=1 изменен на 'Отправлен'");
    }
    // Отмена заказов
    private void demonstrateDeleteOperations() throws Exception {
        System.out.println("\n6. DELETE: Операции удаления");

        // Удаление отмененных заказов (демонстрация)
        int cancelledOrders = orderRepository.countCancelledOrders();
        System.out.println("Найдено отмененных заказов: " + cancelledOrders + " шт.");

        if (cancelledOrders > 0) {
            System.out.println("ℹДля удаления отмененных заказов выполните DELETE запрос в IDE");
        }

        // Удаление клиентов без заказов (демонстрация)
        int customersToDelete = customerRepository.getCustomersWithoutOrders().size();
        System.out.println("Найдено клиентов без заказов: " + customersToDelete + " чел.");

        if (customersToDelete > 0) {
            System.out.println("ℹДля удаления клиентов без заказов выполните DELETE запрос в IDE");
        }
    }
    // Тестовые данные
    private void deleteTestRecords() throws Exception {
        System.out.println("\n7. DELETE: Удаление тестовых записей");

        int deletedOrders = orderRepository.deleteOrdersByCustomerEmail("testoviy@mail.ru");
        System.out.println("Удалено заказов тестового клиента: " + deletedOrders + " шт.");

        int deletedCustomers = customerRepository.deleteCustomerByEmail("testoviy@mail.ru");
        System.out.println("Удален тестовый клиент: затронуто " + deletedCustomers + " записей");
    }
    // Закрытие соединения
    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("\n Соединение с БД закрыто");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
}