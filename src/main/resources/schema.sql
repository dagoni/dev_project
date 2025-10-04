-- Создание таблицы статусов заказов
CREATE TABLE IF NOT EXISTS order_status (
    id SERIAL PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE
);

-- Создание таблицы продуктов
CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    category VARCHAR(100) NOT NULL
);

COMMENT ON TABLE product IS 'Таблица товаров';
COMMENT ON COLUMN product.price IS 'Стоимость товара (должна быть >= 0)';
COMMENT ON COLUMN product.quantity IS 'Количество на складе (должно быть >= 0)';

-- Создание таблицы клиентов
CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(150) UNIQUE
);

COMMENT ON TABLE customer IS 'Таблица клиентов';

-- Создание таблицы заказов
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    order_date DATE NOT NULL DEFAULT CURRENT_DATE,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    status_id INTEGER NOT NULL,
    
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (status_id) REFERENCES order_status(id)
);

COMMENT ON TABLE orders IS 'Таблица заказов';
COMMENT ON COLUMN orders.quantity IS 'Количество товара в заказе';

-- Создание индексов
CREATE INDEX IF NOT EXISTS idx_orders_product_id ON orders(product_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer_id ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_order_date ON orders(order_date);
CREATE INDEX IF NOT EXISTS idx_orders_status_id ON orders(status_id);

-- Заполнение справочника статусов
INSERT INTO order_status (status_name) VALUES
('Новый'),
('В обработке'),
('Отправлен'),
('Доставлен'),
('Отменен'),
('Ожидает оплаты'),
('Оплачен'),
('На утверждении'),
('Возврат'),
('Сдан на склад')
ON CONFLICT (status_name) DO NOTHING;

-- Заполнение тестовыми данными продуктов
INSERT INTO product (description, price, quantity, category) VALUES
('Ноутбук HP', 50000.00, 10, 'Электроника'),
('Мышь беспроводная', 1500.00, 25, 'Электроника'),
('Клавиатура механическая', 3500.00, 15, 'Электроника'),
('Книга "Java для начинающих"', 1200.00, 30, 'Книги'),
('Наушники Sony', 8000.00, 8, 'Электроника'),
('Смартфон Samsung', 45000.00, 12, 'Электроника'),
('Кофеварка', 6000.00, 5, 'Бытовая техника'),
('Футболка', 800.00, 50, 'Одежда'),
('Стул офисный', 3500.00, 7, 'Мебель'),
('Настольная лампа', 1200.00, 20, 'Освещение')
ON CONFLICT (id) DO NOTHING;

-- Заполнение тестовыми данными клиентов
INSERT INTO customer (first_name, last_name, phone, email) VALUES
('Иван', 'Петров', '+79161234567', 'ivan@mail.ru'),
('Мария', 'Сидорова', '+79167654321', 'maria@mail.ru'),
('Алексей', 'Козлов', '+79031112233', 'alex@mail.ru'),
('Елена', 'Николаева', '+79035556677', 'elena@mail.ru'),
('Дмитрий', 'Васильев', '+79189998877', 'dmitry@mail.ru'),
('Ольга', 'Смирнова', '+79024445566', 'olga@mail.ru'),
('Сергей', 'Иванов', '+79163334455', 'sergey@mail.ru'),
('Анна', 'Кузнецова', '+79027778899', 'anna@mail.ru'),
('Павел', 'Морозов', '+79160001122', 'pavel@mail.ru'),
('Наталья', 'Павлова', '+79033334455', 'natalya@mail.ru')
ON CONFLICT (id) DO NOTHING;

-- Заполнение тестовыми данными заказов с использованием всех статусов
INSERT INTO orders (product_id, customer_id, order_date, quantity, status_id) VALUES
(1, 1, '2024-01-15', 1, 1),   -- Новый
(2, 2, '2024-01-16', 2, 2),   -- В обработке
(3, 3, '2024-01-17', 1, 3),   -- Отправлен
(4, 4, '2024-01-18', 3, 4),   -- Доставлен
(5, 5, '2024-01-19', 1, 5),   -- Отменен
(6, 6, '2024-01-20', 1, 6),   -- Ожидает оплаты
(7, 7, '2024-01-21', 1, 7),   -- Оплачен
(8, 8, '2024-01-22', 5, 8),   -- На утверждении
(9, 9, '2024-01-23', 2, 9),   -- Возврат
(10, 10, '2024-01-24', 1, 10) -- Сдан на склад
ON CONFLICT (id) DO NOTHING;