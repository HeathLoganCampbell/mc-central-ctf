package dev.cobblesword.ctf.database;


import dev.cobblesword.libraries.common.config.Configurable;
import dev.cobblesword.libraries.common.config.Optional;

@Configurable
public class DatabaseConfig
{
    @Optional(path = "database", fileName = "CaptureTheFlag/database")
    public static String HOST = "localhost";

    @Optional(path = "database", fileName = "CaptureTheFlag/database")
    public static String PORT = "27017";

    @Optional(path = "database", fileName = "CaptureTheFlag/database")
    public static String DATABASE = "CaptureTheFlag";

    @Optional(path = "database", fileName = "CaptureTheFlag/database")
    public static String USERNAME = "";

    @Optional(path = "database", fileName = "CaptureTheFlag/database")
    public static String PASSWORD = "";
}