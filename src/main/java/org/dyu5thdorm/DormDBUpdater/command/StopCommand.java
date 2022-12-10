package org.dyu5thdorm.DormDBUpdater.command;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;

import java.sql.SQLException;

import static org.dyu5thdorm.DormDBUpdater.DormDBUpdater.logger;

/**
 * Stop command.
 */
public class StopCommand implements Command {
    public StopCommand() {
        logger.info(
                String.format(
                        "%s is ready.", this.getClass().getSimpleName()
                )
        );
    }

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
