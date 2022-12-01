package org.dyu5thdorm.DormDBUpdater.command;

public interface Command {
    void execute();
    void execute(String... args);
}
