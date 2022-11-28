package org.dyu5thdorm.DormDBUpdater.db;

import org.dyu5thdorm.DormDBUpdater.configuration.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <h2>To connect to database.</h2>
 */
public class Database {
    final private String url;
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

        url = "jdbc:mysql://127.0.0.1/";
        connection = DriverManager.getConnection(url, user, pwd);
        this.checkDatabaseAndTables();
    }

    private void checkDatabaseAndTables() throws SQLException {
        this.createDatabaseAndUse();
        this.createStudentTable();
        this.createRoomTable();
    }

    private boolean createDatabaseAndUse() throws SQLException {
        this.connection.prepareStatement(
                String.format(
                        "CREATE DATABASE IF NOT EXISTS %s;",
                        Config.dataBaseParameter.dbName()
                )
        ).execute();

        return this.connection.prepareStatement(
                String.format(
                        "use %s",
                        Config.dataBaseParameter.dbName())
        ).execute();
    }

    private boolean createRoomTable() throws SQLException {
        return this.connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `room`(" +
                        "room_id CHAR(6) PRIMARY KEY, " +
                        "s_id CHAR(8), " +
                        "FOREIGN KEY (s_id) REFERENCES student(s_id) " +
                        "ON UPDATE CASCADE ON DELETE SET NULL);"
        ).execute();
    }

    private boolean createStudentTable() throws SQLException {
        return this.connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `student`(" +
                        "s_id CHAR(8) PRIMARY KEY, " +
                        "name VARCHAR(30) NOT NULL, " +
                        "sex CHAR(1) NOT NULL, " +
                        "major VARCHAR(20) NOT NULL, " +
                        "citizenship VARCHAR(20) NOT NULL);"
        ).execute();
    }


    /**
     * Get database Connection url.
     * @return database Connection url.
     */
    public String getUrl() { return url; }

    /**
     * Get Connection of database.
     * @return Connection of database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Shutdown Database connection.
     * @throws SQLException Database shutdown exception.
     */
    public void shutdown() throws SQLException {
        this.connection.close();
    }
}
