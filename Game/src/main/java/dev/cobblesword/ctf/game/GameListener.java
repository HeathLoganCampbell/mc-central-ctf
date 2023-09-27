package dev.cobblesword.ctf.game;

import dev.cobblesword.ctf.game.team.Team;
import dev.cobblesword.libraries.common.messages.CC;
import dev.cobblesword.libraries.common.task.Sync;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.projectiles.ProjectileSource;

public class GameListener implements Listener
{
    private Game game;

    public GameListener(Game game)
    {
        this.game = game;
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
        game.getPlayerGameStats(e.getEntity()).addDeath();

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

        e.getEntity().sendMessage(CC.gold + "Killed By " + CC.white + killer.getName());
        killer.sendMessage(CC.gold + "Killed " + CC.white + e.getEntity().getName());
        killerTeam.addKill();

        this.game.getPlayerGameStats(killer).addKill();

        String killerName = killer.getName();
        String victimName = e.getEntity().getName();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e)
    {
        Team playerTeam = game.getPlayerTeam(e.getPlayer());
        e.setRespawnLocation(playerTeam.getSpawn());
        playerTeam.applyKit(e.getPlayer());
    }

    @EventHandler
    public void onTeamAttack(EntityDamageByEntityEvent e)
    {
        Entity victim = e.getEntity();
        if(!(victim instanceof Player))
        {
            return;
        }

        Player victimPlayer = (Player) victim;

        Entity attackEntity = e.getDamager();
        Player attackPlayer = null;

        if(attackEntity instanceof Player)
        {
            attackPlayer = (Player) attackEntity;
        }

        if(attackEntity instanceof Arrow)
        {
            ProjectileSource shooter = ((Arrow) attackEntity).getShooter();

            if(shooter instanceof Player)
            {
                attackPlayer = (Player) shooter;
            }
        }

        Team victimTeam = this.game.getPlayerTeam(victimPlayer);
        Team attackerTeam = this.game.getPlayerTeam(attackPlayer);

        boolean isFriendlyFire = victimTeam == attackerTeam;
        if(isFriendlyFire)
        {
            e.setCancelled(true);
        }
    }
}
