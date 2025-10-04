package ru.rtk;

import org.flywaydb.core.Flyway;
import java.sql.Connection;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

public class FlywayMigrator {
    public void migrate(Connection connection) {
        try {
            Properties props = new Properties();
            try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
                props.load(input);
            }

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            Flyway flyway = Flyway.configure()
                    .dataSource(url, username, password)
                    .locations("classpath:db/migration")
                    .load();

            flyway.migrate();
            System.out.println("Миграции Flyway выполнены успешно!");

        } catch (Exception e) {
            System.err.println("Ошибка при выполнении миграций Flyway: " + e.getMessage());
            e.printStackTrace();
        }
    }
}