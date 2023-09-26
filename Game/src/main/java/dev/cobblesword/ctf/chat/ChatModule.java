package dev.cobblesword.ctf.chat;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.cobblesword.libraries.common.messages.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatModule implements Listener
{
    public ChatModule(JavaPlugin plugin)
    {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        PlayerData playerData = CaptureTheFlagPlugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

        e.setFormat(CC.gray + playerData.getPlayerLevelDisplay() + " %s" + CC.gray + ": %s");
    }
}
