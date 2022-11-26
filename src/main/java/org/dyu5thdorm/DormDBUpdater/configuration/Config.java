package org.dyu5thdorm.DormDBUpdater.configuration;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;
import org.dyu5thdorm.models.LoginParameter;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <h2>Generate configuration.json file.</h2>
 */
public class Config {
    static String currentPath; // The jar file execution absolute path.
    static String configPath; // The config file directory.
    static String configFileName; // The config file name.
    static String configFilePath; // The config file absolute path.
    public static LoginParameter loginParameter;
    public static DataBaseParameter dataBaseParameter;

    static {
        currentPath = System.getProperty("user.dir");
        configPath = String.format("%s/%s", currentPath, DormDBUpdater.class.getSimpleName());
        configFileName = "configuration.json";
        configFilePath = String.format("%s/%s", configPath, configFileName);
    }

    /**
     * Initialize configuration profile.
     * @throws IOException File creation Exception.
     */
    public static void init() throws IOException, ClassNotFoundException {
        createConfigFile();
        loadConfig();
        loadDriver();
    }

    /**
     *
     * @throws IOException Configuration file reload Exception.
     */
    public static void reloadConfig() throws IOException {
        loadConfig();
    }

    /**
     *
     * @throws ClassNotFoundException Database driver not found.
     */
    static void loadDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    /**
     * Generate configuration.json file when application execution.
     * @throws IOException File creation Exception.
     */
    static void createConfigFile() throws IOException {
        File directory = new File(configPath),
                configFile = new File(configFilePath);

        if (configFile.exists() || directory.exists()) return;

        if (directory.mkdirs() && configFile.createNewFile()) {
            System.out.println("Configuration generate successful.");
        }

        dumpResource(configFile);
    }

    /**
     * Copy the resource file to configuration.json.
     * @param file The Config file.
     * @throws IOException File creation Exception.
     */
    static void dumpResource(File file) throws IOException {
        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(configFileName);
        OutputStream outputStream = new FileOutputStream(file);

        assert inputStream != null;
        outputStream.write(inputStream.readAllBytes());

        inputStream.close();
        outputStream.close();
    }

    /**
     *
     * @throws IOException Configuration File creation Exception.
     */
    static void loadConfig() throws IOException {
        String content = new String(
                Files.readAllBytes(
                        Path.of(configFilePath)
                )
        );

        JSONObject jsonObject = new JSONObject(content),
            db = jsonObject.getJSONObject("database"),
            login = jsonObject.getJSONObject("login");

        String dbName = db.getString("name"),
                dbUser = db.getString("user"),
                dbPwd = db.getString("pwd"),
                loginId = login.getString("id"),
                loginPwd = login.getString("pwd"),
                loginSmye = login.getString("s_smye"),
                loginSmty = login.getString("s_smty");

        dataBaseParameter = new DataBaseParameter(dbName,  dbUser, dbPwd);
        loginParameter = new LoginParameter(loginId, loginPwd, loginSmye, loginSmty);
    }
}