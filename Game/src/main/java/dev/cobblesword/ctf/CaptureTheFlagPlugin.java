package dev.cobblesword.ctf;

import dev.cobblesword.ctf.compass.CompassModule;
import dev.cobblesword.ctf.data.playerdata.PlayerDataManager;
import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.ctf.lobby.LobbyModule;
import dev.cobblesword.ctf.chat.ChatModule;
import dev.cobblesword.ctf.database.Database;
import dev.cobblesword.ctf.database.DatabaseConfig;
import dev.cobblesword.ctf.data.playerdata.database.PlayerDataRepository;
import dev.cobblesword.ctf.scoreboard.ScoreboardAdapter;
import dev.cobblesword.libraries.common.config.ConfigManager;
import dev.cobblesword.libraries.common.world.Worlds;
import dev.assemble.Assemble;
import dev.assemble.AssembleStyle;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CaptureTheFlagPlugin extends JavaPlugin
{
    private static JavaPlugin instance;
    public static JavaPlugin getInstance()
    {
        return instance;
    }

    private static Database database;

    private static PlayerDataManager playerDataManager;

    @Override
    public void onEnable()
    {
        instance = this;

        ConfigManager configManager = new ConfigManager(this, this,"dev.cobblesword.ctf");

        World lobbyWorld = Bukkit.getWorld("lobby");
        Worlds.initStaticWorld(lobbyWorld, false);

        LobbyModule lobbyModule = new LobbyModule(this, lobbyWorld);

        Game game = new Game(this);

        lobbyModule.setGame(game);

        Assemble assemble = new Assemble(this, new ScoreboardAdapter(game));
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);

        database = new Database(this, DatabaseConfig.HOST,
            Integer.parseInt(DatabaseConfig.PORT),
            DatabaseConfig.USERNAME,
            DatabaseConfig.PASSWORD,
            DatabaseConfig.DATABASE);

        PlayerDataRepository playerDataRepository = new PlayerDataRepository(database);

        database.done();

        playerDataManager = new PlayerDataManager(this, playerDataRepository);

        new ChatModule(this);
        new CompassModule(this).setGame(game);
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
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            onlinePlayer.kickPlayer("Server Restart");
        }

        System.out.println("Bye world");
    }
}
