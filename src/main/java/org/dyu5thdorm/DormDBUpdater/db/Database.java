package org.dyu5thdorm.DormDBUpdater.db;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;
import org.dyu5thdorm.DormDBUpdater.configuration.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <h2>To connect to database.</h2>
 */
public class Database {
    final private Connection connection;

    /**
     * Build a database connection.
     * @param db Database name.
     * @param user User of database name.
     * @param pwd password of database name.
     * @throws SQLException Database connection exception.
     */
    public Database(String db, String user, String pwd) throws SQLException {
        if (db.isEmpty() || user.isEmpty() || pwd.isEmpty()) {
            throw new RuntimeException("Database connection failed. - Not Integrity of Database parameter.");
        }

        String url = String.format(
                "jdbc:mysql://%s/",
                Config.dataBaseParameter.host()
        );
        this.connection = DriverManager.getConnection(url, user, pwd);
        DormDBUpdater.logger.info("Database was connected successfully.");
        this.checkDatabaseAndTables();
    }

    /**
     * Check all table and database that application needed when run application.
     */
    private void checkDatabaseAndTables() throws SQLException {
        this.createDatabaseAndUse();
        this.createStudentTable();
        this.createRoomTable();
    }

    /**
     * Create database and use this database if database not exists.
     * @throws SQLException When create database error.
     */
    private void createDatabaseAndUse() throws SQLException {
        this.connection.prepareStatement(
                String.format(
                        "CREATE DATABASE IF NOT EXISTS %s;",
                        Config.dataBaseParameter.dbName()
                )
        ).execute();

        PreparedStatement useDb = this.connection.prepareStatement(
                String.format(
                        "use %s;",
                        Config.dataBaseParameter.dbName())
        );

        useDb.execute();
        DormDBUpdater.logger.info(
                String.format(
                        "Using database `%s`.",
                        Config.dataBaseParameter.dbName()
                )
        );
    }

    /**
     * Create room database if room table not exists.
     * @throws SQLException When create database error.
     */
    private void createRoomTable() throws SQLException {
        PreparedStatement createRoomTable = this.connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS room(" +
                        "room_id CHAR(6) PRIMARY KEY, " +
                        "s_id CHAR(8) UNIQUE, " +
                        "FOREIGN KEY (s_id) REFERENCES student(s_id) " +
                        "ON UPDATE CASCADE ON DELETE SET NULL);"
        );
        
        createRoomTable.execute();
    }

    /**
     * Create student database if student table not exists.
     * @throws SQLException When create database error.
     */
    private void createStudentTable() throws SQLException {
        PreparedStatement createStudentTable = this.connection.prepareStatement(
                ("CREATE TABLE IF NOT EXISTS student(" +
                        "s_id CHAR(8) PRIMARY KEY, " +
                        "name VARCHAR(30) NOT NULL, " +
                        "sex CHAR(1) NOT NULL, " +
                        "major VARCHAR(20) NOT NULL, " +
                        "citizenship VARCHAR(20) NOT NULL);")
        );
        createStudentTable.execute();
    }

    /**
     * Get Connection of database.
     * @return Connection of database
     */
    public Connection getConnection() {
        try {
            this.connection.prepareStatement(
                    String.format(
                            "use %s;", Config.dataBaseParameter.dbName()
                    )
            ).execute();
        } catch (SQLException e) {
            try {
                checkDatabaseAndTables();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        return this.connection;
    }

    /**
     * Shutdown Database connection.
     * @throws SQLException Database shutdown exception.
     */
    public void shutdown() throws SQLException {
        this.connection.close();
        DormDBUpdater.logger.info("Database connection closed...");
    }
}
