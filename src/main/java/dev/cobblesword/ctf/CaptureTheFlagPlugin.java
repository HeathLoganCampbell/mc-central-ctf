package dev.cobblesword.ctf;

import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.ctf.lobby.Lobby;
import dev.cobblesword.ctf.scoreboard.ScoreboardAdapter;
import dev.cobblesword.libraries.common.world.Worlds;
import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class CaptureTheFlagPlugin extends JavaPlugin
{
    private static JavaPlugin instance;
    public static JavaPlugin getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        instance = this;

        World lobbyWorld = Bukkit.getWorld("lobby");
        Worlds.initStaticWorld(lobbyWorld, false);

        Lobby lobby = new Lobby(this, lobbyWorld);

        Game game = new Game(this);

        lobby.setGame(game);

        Assemble assemble = new Assemble(this, new ScoreboardAdapter(game));
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);

        System.out.println("Hello world");
    }

    @Override
    public void onDisable()
    {
        System.out.println("Bye world");
    }
}
