package dev.cobblesword.ctf.compass;

import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.ctf.game.team.Team;
import dev.cobblesword.ctf.game.team.TeamType;
import dev.cobblesword.libraries.common.messages.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CompassModule implements Listener
{
    @Getter @Setter
    private Game game;

    public CompassModule(JavaPlugin plugin)
    {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        Action action = e.getAction();
        if(!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK))
        {
            return;
        }

        ItemStack item = e.getItem();
        if(item == null) return;
        if(item.getType() != Material.COMPASS) return;
        if(game == null) return;

        Team yourTeam = game.getPlayerTeam(player);
        Team otherTeam = game.getTeam(yourTeam.getTeamType() == TeamType.BLUE ? TeamType.RED : TeamType.BLUE);

        player.setCompassTarget(otherTeam.getFlag().getActiveFlagLocation());
        player.sendMessage(CC.green + "Pointing at " + otherTeam.getName() + "'s flag");
    }
}
