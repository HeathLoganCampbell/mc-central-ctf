package dev.cobblesword.ctf.lobby;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.libraries.common.task.Sync;
import dev.cobblesword.libraries.common.utils.PlayerUtils;
import dev.cobblesword.libraries.common.world.Worlds;
import dev.cobblesword.libraries.modules.levels.LevelModule;
import dev.cobblesword.libraries.modules.serverstartup.Module;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyModule extends Module
{
    private World lobbyWorld;

    @Getter
    private LevelModule levelModule;

    public LobbyModule(JavaPlugin plugin, LevelModule levelModule)
    {
        super("Lobby", plugin);

        this.levelModule = levelModule;
    }

    public void applyLobbyKit(Player player)
    {
        PlayerUtils.reset(player);

        PlayerData playerData = CaptureTheFlagPlugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

        if(playerData != null)
        {
            player.setLevel(playerData.getLevel());
            player.setExp(levelModule.getPercentageTilNextLevel(player));
        }
    }

    public boolean isInLobby(Player player)
    {
        return player.getWorld().equals(lobbyWorld);
    }

    public Location getSpawnLocation()
    {
        return new Location(lobbyWorld, 0, 100, 0);
    }

    @Override
    protected void onEnable()
    {
        this.registerEvents(new LobbyListener(this));

        this.lobbyWorld = Bukkit.getWorld("lobby");
        Worlds.initStaticWorld(this.lobbyWorld, false);
    }
}
