package org.dyu5thdorm.DormDBUpdater;

import org.dyu5thdorm.DormDBUpdater.configuration.Config;
import org.dyu5thdorm.DormDBUpdater.db.Database;

import java.io.IOException;
import java.sql.SQLException;

public class DormDBUpdater {
   public static Database database;

   public static void main(String[] args) {
       try {
           Config.init();

           database = new Database(
                   Config.dataBaseParameter.dbName(),
                   Config.dataBaseParameter.user(),
                   Config.dataBaseParameter.pwd()
           );

           database.shutdown();
       } catch (SQLException | IOException | ClassNotFoundException e) {
           throw new RuntimeException(e);
       }
    }
}

