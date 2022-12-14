package org.dyu5thdorm.DormDBUpdater.command;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;
import org.dyu5thdorm.DormDBUpdater.configuration.Config;

import java.io.IOException;
import java.sql.SQLException;

import static org.dyu5thdorm.DormDBUpdater.DormDBUpdater.logger;

/**
 * Reload command.
 */
public class ReloadCommand implements Command {
    public ReloadCommand() {
        logger.info(
                String.format(
                        "%s is ready.", this.getClass().getSimpleName()
                )
        );
    }

    /**
     * Reload configuration node, re-connected to database and update database.
     */
    @Override
    public synchronized void execute() {
        try {
            Config.reloadConfig();
            DormDBUpdater.connectToDatabase();
            DormDBUpdater.commands.get("update").execute();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param args argument of this command
     */
    @Override
    public void execute(String... args) {
        execute();
    }
}
