package org.dyu5thdorm.DormDBUpdater;

import org.dyu5thdorm.DormDBUpdater.configuration.Config;
import org.dyu5thdorm.DormDBUpdater.db.Database;
import org.dyu5thdorm.DormDBUpdater.repositories.RoomRepository;
import org.dyu5thdorm.DormDBUpdater.repositories.StudentRepository;
import org.dyu5thdorm.DormDBUpdater.thread.ReceiveCommandThread;
import org.dyu5thdorm.RoomDataFetcher.RoomDataFetcher;
import org.dyu5thdorm.RoomDataFetcher.models.Room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DormDBUpdater {
   public static Database database;
   public static RoomRepository roomRepository;
   public static StudentRepository studentRepository;

   public static void main(String[] args) {
       try {
           Config.init();
           database = new Database(
                   Config.dataBaseParameter.dbName(),
                   Config.dataBaseParameter.user(),
                   Config.dataBaseParameter.pwd()
           );

           roomRepository = new RoomRepository();
           studentRepository = new StudentRepository();

           List<Room> room = RoomDataFetcher.getData(Config.dataFetchingParameter);
           for (var r : room) {
               if (r.student() != null) {
                   studentRepository.insert(r.student());
               }

               roomRepository.insert(r);
           }

           database.shutdown();
       } catch (SQLException | IOException | ClassNotFoundException e) {
           throw new RuntimeException(e);
       }
    }
}

