package org.dyu5thdorm.DormDBUpdater.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <h2>To connect to database.</h2>
 */
public class Database {
    final private String url;
    final private Statement statement;
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

        url = String.format("jdbc:mysql://127.0.0.1:3306/%s", db);
        connection = DriverManager.getConnection(url, user, pwd);
        statement = connection.createStatement();
    }

    /**
     * Get the statement of database.
     * @return Statement of database.
     */
    public Statement getStatement() {
        return statement;
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
        this.statement.close();
    }
}
