package dev.cobblesword.ctf.lobby;

import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.libraries.common.task.Sync;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyModule
{
    private Game game;

    private World lobbyWorld;

    public LobbyModule(JavaPlugin plugin, World lobbyWorld)
    {
        this.lobbyWorld = lobbyWorld;

        Bukkit.getPluginManager().registerEvents(new LobbyListener(this), plugin);
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public boolean isInLobby(Player player)
    {
        return player.getWorld().equals(lobbyWorld);
    }

    public Location getSpawnLocation()
    {
        return new Location(lobbyWorld, 0, 100, 0);
    }
}
