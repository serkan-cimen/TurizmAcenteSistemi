package com.turizmAcenteSistemi.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private Connection conn = null;
    public Connection connectDB() {
        try {
            this.conn = DriverManager.getConnection(Config.DB_URL, Config.DB_USERNAME, Config.DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.conn;
    }

    public static Connection getInstance() {
        DBConnector dbConn = new DBConnector();
        return dbConn.connectDB();
    }
}
