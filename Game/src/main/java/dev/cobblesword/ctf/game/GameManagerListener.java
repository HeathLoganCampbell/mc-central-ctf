package dev.cobblesword.ctf.game;

import dev.cobblesword.libraries.common.task.Sync;
import dev.cobblesword.ctf.game.team.Team;
import dev.cobblesword.libraries.common.messages.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.projectiles.ProjectileSource;

public class GameManagerListener implements Listener
{
    public GameManager gameManager;

    public GameManagerListener(GameManager gameManager)
    {
        this.gameManager = gameManager;
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
        gameManager.join(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        gameManager.leave(e.getPlayer());
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
