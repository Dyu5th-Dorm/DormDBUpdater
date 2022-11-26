package org.dyu5thdorm.DormDBUpdater.configuration;

import java.util.Objects;

/**
 * <h2>Database connect parameter.</h2>
 */
public record DataBaseParameter(String dbName, String user, String pwd) {
    /**
     * @param dbName Connection Database name.
     * @param user   User of Database name.
     * @param pwd    password of Database name.
     */
    public DataBaseParameter { }

    @Override
    public String toString() {
        return "DataBaseConfig{" +
                "dbName='" + dbName + '\'' +
                ", user='" + user + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DataBaseParameter) obj;
        return Objects.equals(this.dbName, that.dbName) &&
                Objects.equals(this.user, that.user) &&
                Objects.equals(this.pwd, that.pwd);
    }

}
