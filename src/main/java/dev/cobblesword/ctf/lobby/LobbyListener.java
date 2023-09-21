package dev.cobblesword.ctf.lobby;

import dev.cobblesword.libraries.common.messages.CC;
import dev.cobblesword.libraries.common.task.Sync;
import dev.cobblesword.libraries.common.utils.PlayerUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInitialSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyListener implements Listener
{
    private Lobby lobby;

    public LobbyListener(Lobby lobby)
    {
        this.lobby = lobby;

        Sync.get().interval(20).run(this::handleTabBanner).execute();
    }

    public void handleTabBanner()
    {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            BaseComponent header = new TextComponent("Capture The Flag");
            header.setBold(true);
            header.setColor(ChatColor.GOLD);

            header.addExtra("\n");
            header.addExtra(CC.gray + "      15 minutes remaining\n");


            BaseComponent footer = new TextComponent("\nCobbleSword.Com\n");
            footer.setBold(true);
            footer.setColor(ChatColor.AQUA);

            onlinePlayer.setPlayerListHeaderFooter(header, footer);
        }
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent e)
    {
        Entity entity = e.getEntity();
        if(!(entity instanceof Player))
        {
            return;
        }

        Player player = (Player) entity;
        if(!lobby.isInLobby(player))
        {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerInitialSpawnEvent e)
    {
        Player player = e.getPlayer();
        e.setSpawnLocation(lobby.getSpawnLocation());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        PlayerUtils.reset(player);
    }
}
