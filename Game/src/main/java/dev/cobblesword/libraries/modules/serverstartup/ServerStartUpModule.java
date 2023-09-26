package dev.cobblesword.libraries.modules.serverstartup;

import dev.cobblesword.ctf.game.GameState;
import dev.cobblesword.libraries.common.messages.CC;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

// just bind late, in the configs
public class ServerStartUpModule implements Listener
{
    @Setter
    private boolean isJoinable = false;

    public ServerStartUpModule(JavaPlugin plugin)
    {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPreLogin(AsyncPlayerPreLoginEvent e)
    {
        if(!isJoinable) return;

        e.setKickMessage(CC.bAqua + "Server is starting up please wait a minute before reconnecting!");
    }
}
