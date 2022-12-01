package org.dyu5thdorm.DormDBUpdater.command;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;

import java.sql.SQLException;

/**
 * Stop command.
 */
public class StopCommand implements Command {

    /**
     * Stop this application.
     */
    @Override
    public void execute() {
        try {
            DormDBUpdater.receiveCommandThread.interrupt();
            DormDBUpdater.autoUpdateDBThread.interrupt();
            DormDBUpdater.database.shutdown();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(String... args) {
        execute();
    }
}
