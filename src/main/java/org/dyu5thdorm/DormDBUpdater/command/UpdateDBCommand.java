package org.dyu5thdorm.DormDBUpdater.command;

import org.dyu5thdorm.DormDBUpdater.configuration.Config;
import org.dyu5thdorm.RoomDataFetcher.RoomDataFetcher;
import org.dyu5thdorm.RoomDataFetcher.models.Room;

import java.io.IOException;
import java.util.List;

import static org.dyu5thdorm.DormDBUpdater.DormDBUpdater.*;

/**
 * Update command.
 */
public class UpdateDBCommand implements Command{

    /**
     * Update database.
     */
    @Override
    public void execute() {
        logger.info("Database update starts now...");

        List<Room> rooms = null;
        try {
            rooms = RoomDataFetcher.getData(Config.dataFetchingParameter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Room room : rooms) {
            if (room.student() == null) continue;
            studentRepository.insert(room.student());
            roomRepository.insert(room);
        }

        logger.info("Database successfully updated!");
    }

    @Override
    public synchronized void execute(String... args) {
        this.execute();
    }
}
