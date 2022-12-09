package org.dyu5thdorm.DormDBUpdater;

import org.dyu5thdorm.DormDBUpdater.command.Command;
import org.dyu5thdorm.DormDBUpdater.command.ReloadCommand;
import org.dyu5thdorm.DormDBUpdater.command.StopCommand;
import org.dyu5thdorm.DormDBUpdater.command.UpdateDBCommand;
import org.dyu5thdorm.DormDBUpdater.configuration.Config;
import org.dyu5thdorm.DormDBUpdater.db.Database;
import org.dyu5thdorm.DormDBUpdater.repositories.DormitoryRepository;
import org.dyu5thdorm.DormDBUpdater.repositories.RoomRepository;
import org.dyu5thdorm.DormDBUpdater.repositories.StudentRepository;
import org.dyu5thdorm.DormDBUpdater.thread.AutoUpdateDBThread;
import org.dyu5thdorm.DormDBUpdater.thread.ReceiveCommandThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DormDBUpdater {
    public static Logger logger;
    public static Database database;
    public static DormitoryRepository roomRepository, studentRepository;
    public static HashMap<String, Command> commands;
    public static Thread receiveCommandThread, autoUpdateDBThread;

    static {
        logger = LoggerFactory.getLogger(DormDBUpdater.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        logger.info(
                String.format(
                        "---------------DormDBUpdater %s---------------",
                        dateFormat.format(new Date())
                )
        );
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            Config.init();
            registerCommands();
            connectToDatabase();
            initRepositories();
            initThreads();
       } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
       }
    }

    private static void initThreads() {
        receiveCommandThread = new ReceiveCommandThread();
        autoUpdateDBThread = new AutoUpdateDBThread();
        receiveCommandThread.start();
        autoUpdateDBThread.start();
    }

    public static void connectToDatabase() throws SQLException {
        database = new Database(
                Config.dataBaseParameter.dbName(),
                Config.dataBaseParameter.user(),
                Config.dataBaseParameter.pwd()
        );
    }

    private static void initRepositories() {
        roomRepository = new RoomRepository();
        studentRepository = new StudentRepository();
    }

    private static void registerCommands() {
        commands = new HashMap<>();
        commands.put("update", new UpdateDBCommand());
        commands.put("reload", new ReloadCommand());
        commands.put("stop", new StopCommand());
    }
}

