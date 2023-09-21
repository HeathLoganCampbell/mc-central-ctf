package dev.cobblesword.ctf.game;

import dev.cobblesword.libraries.common.messages.CC;
import dev.cobblesword.libraries.common.task.Sync;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GameListener implements Listener
{
    public Game game;

    public GameListener(Game game)
    {
        this.game = game;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        Player player = e.getPlayer();

        if(player.getGameMode() == GameMode.CREATIVE)
        {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        Player player = e.getPlayer();

        if(player.getGameMode() == GameMode.CREATIVE)
        {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        game.join(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        game.leave(e.getPlayer());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e)
    {
        if(e.getPlayer().getGameMode() == GameMode.CREATIVE)
        {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e)
    {
        if(e.getFoodLevel() >= 20)
        {
            return;
        }

        e.setFoodLevel(20);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        e.setDroppedExp(0);
        e.getDrops().clear();
        Sync.get().delay(1).run(() -> {
            e.getEntity().spigot().respawn();
        }).execute();

        e.setDeathMessage(null);

        Player killer = e.getEntity().getKiller();
        if(killer == null)
        {
            return;
        }

        Team killerTeam = game.getPlayerTeam(killer);
        if(killerTeam == null)
        {
            return;
        }

        e.getEntity().sendMessage(CC.gold + "Killed By" + CC.white + killer.getName());
        killer.sendMessage(CC.gold + "Killed " + CC.white + e.getEntity().getName());
        killerTeam.addKill();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e)
    {
        Team playerTeam = game.getPlayerTeam(e.getPlayer());
        e.setRespawnLocation(playerTeam.getSpawn());
        playerTeam.applyKit(e.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if(e.getWhoClicked().getGameMode() == GameMode.CREATIVE)
        {
            return;
        }

        if (e.getSlotType() == InventoryType.SlotType.ARMOR)
        {
            e.setCancelled(true);
        }
    }
}
