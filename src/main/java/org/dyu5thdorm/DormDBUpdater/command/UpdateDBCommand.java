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
    public UpdateDBCommand() {
        logger.info(
                String.format(
                        "%s is ready.", this.getClass().getSimpleName()
                )
        );
    }

    /**
     * Update database.
     */
    @Override
    public void execute() {
        logger.info("Database update starts now...");

        try {
            List<Room> fetchedRooms = RoomDataFetcher.getData(
                    Config.dataFetchingParameter
            );

            logger.info(
                    String.format(
                            "Get %d room data from RoomDataFetcher", fetchedRooms.size()
                    )
            );

            for (Room room : fetchedRooms) {
                studentRepository.insert(room.student());
                roomRepository.insert(room);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("Database successfully updated!");
    }

    @Override
    public synchronized void execute(String... args) {
        this.execute();
    }
}
