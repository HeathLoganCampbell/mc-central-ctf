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
    private LobbyModule lobbyModule;

    public LobbyListener(LobbyModule lobbyModule)
    {
        this.lobbyModule = lobbyModule;

        Sync.get().interval(20).run(this::handleTabBanner).execute();
        Sync.get().interval(50).run(this::handleFallingIntoVoid).execute();
    }

    public void handleFallingIntoVoid()
    {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if(!lobbyModule.isInLobby(onlinePlayer))
            {
                continue;
            }

            if(onlinePlayer.getLocation().getY() < -10)
            {
                onlinePlayer.teleport(lobbyModule.getSpawnLocation());
            }
        }
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
        if(!lobbyModule.isInLobby(player))
        {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerInitialSpawnEvent e)
    {
        Player player = e.getPlayer();
        e.setSpawnLocation(lobbyModule.getSpawnLocation());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        PlayerUtils.reset(player);
    }
}
