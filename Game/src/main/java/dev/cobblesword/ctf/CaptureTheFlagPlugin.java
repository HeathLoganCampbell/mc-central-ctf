package dev.cobblesword.ctf;

import dev.cobblesword.ctf.compass.CompassModule;
import dev.cobblesword.ctf.data.playerdata.PlayerDataManager;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.ctf.game.GameManager;
import dev.cobblesword.ctf.lobby.LobbyModule;
import dev.cobblesword.ctf.chat.ChatModule;
import dev.cobblesword.ctf.database.Database;
import dev.cobblesword.ctf.database.DatabaseConfig;
import dev.cobblesword.ctf.data.playerdata.database.PlayerDataRepository;
import dev.cobblesword.ctf.perks.PerkModule;
import dev.cobblesword.ctf.scoreboard.ScoreboardAdapter;
import dev.cobblesword.libraries.common.commandframework.CommandFramework;
import dev.cobblesword.libraries.common.config.ConfigManager;
import dev.cobblesword.libraries.common.task.Sync;
import dev.cobblesword.libraries.common.world.Worlds;
import dev.assemble.Assemble;
import dev.assemble.AssembleStyle;
import dev.cobblesword.libraries.modules.serverstartup.ServerStartUpModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CaptureTheFlagPlugin extends JavaPlugin
{
    private static CaptureTheFlagPlugin instance;
    public static CaptureTheFlagPlugin getInstance()
    {
        return instance;
    }

    private static Database database;

    private static PlayerDataManager playerDataManager;

    @Getter
    private CommandFramework commandFramework;

    @Getter
    private GameManager gameManager;

    @Getter
    private LobbyModule lobbyModule;

    @Override
    public void onEnable()
    {
        instance = this;
        commandFramework = new CommandFramework(this);

        ConfigManager configManager = new ConfigManager(this, this,"dev.cobblesword.ctf");

        Assemble assemble = new Assemble(this, new ScoreboardAdapter());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);

        gameManager = new GameManager(this);

        database = new Database(this, DatabaseConfig.HOST,
            Integer.parseInt(DatabaseConfig.PORT),
            DatabaseConfig.USERNAME,
            DatabaseConfig.PASSWORD,
            DatabaseConfig.DATABASE);

        PlayerDataRepository playerDataRepository = new PlayerDataRepository(database);

        playerDataManager = new PlayerDataManager(this, playerDataRepository);

        new ChatModule(this);
        new CompassModule(this).enable();
        new PerkModule(this).enable();
        lobbyModule = new LobbyModule(this);
        lobbyModule.enable();

        commandFramework.registerHelp();
    }

    public static Database getMongoDatabase() {
        return database;
    }

    public static PlayerDataManager getPlayerDataManager()
    {
        return playerDataManager;
    }

    @Override
    public void onDisable()
    {
        getPlayerDataManager().onDisable();
        System.out.println("Bye world");
    }
}
