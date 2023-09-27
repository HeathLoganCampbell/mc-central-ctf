package dev.cobblesword.ctf.compass;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.ctf.game.team.Team;
import dev.cobblesword.ctf.game.team.TeamType;
import dev.cobblesword.libraries.common.messages.CC;
import dev.cobblesword.libraries.common.task.Sync;
import dev.cobblesword.libraries.modules.serverstartup.Module;
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

import java.util.HashMap;
import java.util.UUID;

public class CompassModule extends Module
{
    private HashMap<UUID, TeamType> targetTeamFlag = new HashMap<UUID, TeamType>();

    public CompassModule(JavaPlugin plugin)
    {
        super("Compass", plugin);

        Sync.get().interval(40).run(() -> {
            Game game = CaptureTheFlagPlugin.getInstance().getGameManager().getGame();
            if(game == null) return;
            if(!game.isInProgress()) return;

            for (Player player : Bukkit.getOnlinePlayers())
            {
                TeamType targetTeamType = getTargetTeam(player);
                if(targetTeamType == null)
                {
                    continue;
                }

                Team targetTeam = game.getTeam(targetTeamType);
                player.setCompassTarget(targetTeam.getFlag().getActiveFlagLocation());
            }
        }).execute();
    }

    private TeamType getTargetTeam(Player player)
    {
        Game game = CaptureTheFlagPlugin.getInstance().getGameManager().getGame();
        if(game == null) return null;

        Team yourTeam = game.getPlayerTeam(player);

        TeamType currentTargetedTeamType = yourTeam.getTeamType();
        TeamType pointingTeam = targetTeamFlag.getOrDefault(player.getUniqueId(), currentTargetedTeamType);
        TeamType targetTeamType = pointingTeam == TeamType.BLUE ? TeamType.RED : TeamType.BLUE;
        return targetTeamType;
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
        Game game = CaptureTheFlagPlugin.getInstance().getGameManager().getGame();
        if(game == null) return;
        if(!game.isInProgress()) return;

        TeamType targetTeamType = getTargetTeam(player);
        targetTeamFlag.put(player.getUniqueId(), targetTeamType);

        Team targetTeam = game.getTeam(targetTeamType);

        player.setCompassTarget(targetTeam.getFlag().getActiveFlagLocation());
        player.sendMessage(targetTeam.getChatColor() + "Pointing at " + targetTeam.getName() + "'s flag");
    }

    @Override
    protected void onEnable()
    {
        this.registerEvents(this);
    }
}
