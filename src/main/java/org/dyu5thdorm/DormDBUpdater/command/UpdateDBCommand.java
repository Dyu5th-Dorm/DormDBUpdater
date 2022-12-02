package org.dyu5thdorm.DormDBUpdater.command;

import org.dyu5thdorm.DormDBUpdater.configuration.Config;
import org.dyu5thdorm.RoomDataFetcher.RoomDataFetcher;
import org.dyu5thdorm.RoomDataFetcher.models.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.dyu5thdorm.DormDBUpdater.DormDBUpdater.*;

/**
 * Update command.
 */
public class UpdateDBCommand implements Command{
    private List<Room> fetchedRooms;

    public UpdateDBCommand() {
        fetchedRooms = new ArrayList<>();
    }

    /**
     * Update database.
     */
    @Override
    public void execute() {
        logger.info("Database update starts now...");

        try {
            fetchedRooms = RoomDataFetcher.getData(Config.dataFetchingParameter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Room room : fetchedRooms) {
            studentRepository.insert(room.student());
            roomRepository.insert(room);
        }

        fetchedRooms.clear();
        logger.info("Database successfully updated!");
    }

    @Override
    public synchronized void execute(String... args) {
        this.execute();
    }
}
