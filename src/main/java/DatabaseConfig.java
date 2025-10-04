package ru.rtk;

import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

public class DatabaseConfig {
    public Connection getConnection() throws Exception {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
            props.load(input);
        }

        Class.forName("org.postgresql.Driver");

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        System.out.println("Подключаемся к базе данных: " + url);
        Connection connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
        return connection;
    }
}