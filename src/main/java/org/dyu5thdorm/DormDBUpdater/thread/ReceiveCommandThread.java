package org.dyu5thdorm.DormDBUpdater.thread;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;
import org.dyu5thdorm.DormDBUpdater.command.Command;

import java.util.Scanner;

/**
 * Receive command thread.
 */
public class ReceiveCommandThread extends Thread {
    private final Scanner scanner;

    /**
     * Constructor.
     */
    public ReceiveCommandThread() {
        scanner = new Scanner(System.in);
        super.setName(this.getClass().getSimpleName());
    }


    @Override
    public void run() {
        super.run();

        while (!Thread.currentThread().isInterrupted()) {
            String command = scanner.next().toLowerCase();
            Command getCommand = DormDBUpdater.commands.get(command);
            if (getCommand == null) {
                DormDBUpdater.logger.info(
                        String.format("Unknown command: %s", command)
                );
                continue;
            }
            getCommand.execute();
        }
    }
}
