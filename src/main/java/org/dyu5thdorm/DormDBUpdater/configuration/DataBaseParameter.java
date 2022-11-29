package org.dyu5thdorm.DormDBUpdater.configuration;

import java.util.Objects;

/**
 * <h2>Database connect parameter.</h2>
 */
public record DataBaseParameter(String host, String dbName, String user, String pwd) {
    /**
     * Constructor
     */
    public DataBaseParameter {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DataBaseParameter) obj;
        return Objects.equals(this.host, that.host) &&
                Objects.equals(this.dbName, that.dbName) &&
                Objects.equals(this.user, that.user) &&
                Objects.equals(this.pwd, that.pwd);
    }

    @Override
    public String toString() {
        return "DataBaseParameter[" +
                "host=" + host + ", " +
                "dbName=" + dbName + ", " +
                "user=" + user + ", " +
                "pwd=" + pwd + ']';
    }
}
