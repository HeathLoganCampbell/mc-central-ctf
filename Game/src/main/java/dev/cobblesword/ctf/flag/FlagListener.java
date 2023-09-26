package dev.cobblesword.ctf.flag;

import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.ctf.game.GameState;
import dev.cobblesword.ctf.game.team.Team;
import dev.cobblesword.libraries.common.task.Sync;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FlagListener implements Listener
{
    private Game game;

    public FlagListener(Game game, JavaPlugin plugin)
    {
        this.game = game;

        Sync.get().sync().run(this::handleFlagPickUpCheck).interval(10).execute();
        Sync.get().sync().run(this::handleFlagCaptureCheck).interval(10).execute();
    }

    public void handleFlagPickUpCheck()
    {
        if(this.game.getState() != GameState.IN_PROGRESS)
        {
            return;
        }

        for (Flag flag : this.game.getAllFlags())
        {
            Location flagLoc = flag.getFlagSpawnLocation();
            for (Player player : Bukkit.getOnlinePlayers())
            {
                Location playerLoc = player.getLocation();

                Team playerTeam = this.game.getPlayerTeam(player);
                if(playerTeam == null)
                {
                    continue;
                }

                boolean isNotYourFlag = playerTeam.getFlag() == flag;
                if(isNotYourFlag)
                {
                    continue;
                }

                if (playerLoc.distanceSquared(flagLoc) <= 4)
                {
                    flag.pickUpFlag(player);
                }
            }
        }
    }

    public void handleFlagCaptureCheck()
    {
        if(this.game.getState() != GameState.IN_PROGRESS)
        {
            return;
        }

        for (Flag flag : this.game.getAllFlags())
        {
            Player flagCarrier = flag.getFlagCarrier();
            if(flagCarrier == null)
            {
                continue;
            }

            Location playerLoc = flagCarrier.getLocation();
            Team otherTeam = this.game.getPlayerTeam(flagCarrier);
            Location spawnLoc = otherTeam.getSpawn();

            if (playerLoc.distanceSquared(spawnLoc) <= 4)
            {
                this.game.captureFlag(flagCarrier);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        if(this.game.getState() != GameState.IN_PROGRESS)
        {
            return;
        }

        Player player = e.getEntity();
        for (Flag flag : this.game.getAllFlags())
        {
            if (flag.getFlagCarrier() == player)
            {
                flag.dropFlag();
            }
        }
    }
}
