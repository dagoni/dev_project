-- 1. ЧТЕНИЕ: Список всех заказов за последние 7 дней с именем покупателя и описанием товара
SELECT o.id, o.order_date, c.first_name, c.last_name, p.description, o.quantity, os.status_name
FROM orders o
JOIN customer c ON o.customer_id = c.id
JOIN product p ON o.product_id = p.id
JOIN order_status os ON o.status_id = os.id
WHERE o.order_date >= CURRENT_DATE - INTERVAL '7 days'
ORDER BY o.order_date DESC;

-- 2. ЧТЕНИЕ: Топ-3 самых популярных товаров
SELECT p.description, SUM(o.quantity) as total_ordered
FROM orders o
JOIN product p ON o.product_id = p.id
GROUP BY p.description
ORDER BY total_ordered DESC
LIMIT 3;

-- 3. ЧТЕНИЕ: Общая стоимость всех заказов по клиентам
SELECT c.first_name, c.last_name, SUM(p.price * o.quantity) as total_spent
FROM orders o
JOIN customer c ON o.customer_id = c.id
JOIN product p ON o.product_id = p.id
GROUP BY c.id, c.first_name, c.last_name
ORDER BY total_spent DESC;

-- 4. ЧТЕНИЕ: Заказы со статусом "На утверждении"
SELECT o.id, o.order_date, c.first_name, c.last_name, p.description, o.quantity
FROM orders o
JOIN customer c ON o.customer_id = c.id
JOIN product p ON o.product_id = p.id
WHERE o.status_id = (SELECT id FROM order_status WHERE status_name = 'На утверждении');

-- 5. ЧТЕНИЕ: Заказы, ожидающие оплаты
SELECT * FROM orders
WHERE status_id = (SELECT id FROM order_status WHERE status_name = 'Ожидает оплаты');

-- 6. ЧТЕНИЕ: Заказы с возвратом
SELECT * FROM orders
WHERE status_id = (SELECT id FROM order_status WHERE status_name = 'Возврат');

-- 7. ЧТЕНИЕ: Статистика по статусам
SELECT s.status_name, COUNT(*) AS count
FROM orders o
JOIN order_status s ON o.status_id = s.id
GROUP BY s.status_name
ORDER BY count DESC;

-- 8. ИЗМЕНЕНИЕ: Обновление количества товара на складе после продажи
UPDATE product
SET quantity = quantity - 2
WHERE id = 1;

-- 9. ИЗМЕНЕНИЕ: Обновление статуса заказа
UPDATE orders
SET status_id = (SELECT id FROM order_status WHERE status_name = 'Отправлен')
WHERE id = 1;

-- 10. УДАЛЕНИЕ: Удаление клиентов без заказов
DELETE FROM customer
WHERE id NOT IN (SELECT DISTINCT customer_id FROM orders);

-- 11. УДАЛЕНИЕ: Удаление заказов со статусами "Отменен" и "Возврат"
DELETE FROM orders
WHERE status_id IN (
    SELECT id FROM order_status
    WHERE status_name IN ('Отменен', 'Возврат')
);

-- 12. Заказы со статусом "Оплачен"
SELECT * FROM orders
WHERE status_id = (SELECT id FROM order_status WHERE status_name = 'Оплачен');

-- 13. Заказы со статусом "Сдан на склад"
SELECT * FROM orders
WHERE status_id = (SELECT id FROM order_status WHERE status_name = 'Сдан на склад');

-- 14. Обновление статуса заказа на "Доставлен"
UPDATE orders
SET status_id = (SELECT id FROM order_status WHERE status_name = 'Доставлен')
WHERE id = 3;

-- 15. Обновление статуса заказа на "Отменен"
UPDATE orders
SET status_id = (SELECT id FROM order_status WHERE status_name = 'Отменен')
WHERE id = 5;


-- Проверка после удаления
SELECT 'Клиенты после удаления:' as info;
SELECT * FROM customer;

SELECT 'Заказы после удаления:' as info;
SELECT * FROM orders;
