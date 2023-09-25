package dev.cobblesword.ctf.data.playerdata.listeners;

import dev.cobblesword.ctf.data.playerdata.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerDataListener implements Listener
{
    private PlayerDataManager playerDataManager;

    public PlayerDataListener(PlayerDataManager playerDataManager)
    {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e)
    {
        this.playerDataManager.onFetch(e.getUniqueId(), e.getName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        this.playerDataManager.onJoin(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.playerDataManager.onLeave(uniqueId);
    }

    @EventHandler
    public void onKick(PlayerKickEvent e)
    {
        Player player = e.getPlayer();
        UUID uniqueId = player.getUniqueId();

        this.playerDataManager.onLeave(uniqueId);
    }
}
