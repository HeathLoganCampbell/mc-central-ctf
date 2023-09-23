package dev.cobblesword.ctf.scoreboard;

import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.ctf.game.GameState;
import dev.cobblesword.ctf.game.TeamType;
import dev.cobblesword.libraries.common.messages.CC;
import io.github.thatkawaiisam.assemble.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter implements AssembleAdapter
{
    private Game game;

    public ScoreboardAdapter(Game game) {
        this.game = game;
    }

    @Override
    public String getTitle(Player player) {
        return CC.bGold + "Capture The Flag";
    }

    @Override
    public List<String> getLines(Player player)
    {
        final List<String> toReturn = new ArrayList<>();

        if(game.getState().WaitingToStart() || game.getState() == GameState.PREPARE_GAME)
        {
            if (game.getState() == GameState.WAITING_FOR_PLAYERS)
            {
                toReturn.add(CC.gray + "Waiting for players");
                toReturn.add("1/30");
                toReturn.add(" ");
            }

            if (game.getState() == GameState.COUNTDOWN)
            {
                toReturn.add(CC.gray + "Starting in");
                toReturn.add(game.getSecondsRemaining() + " Seconds");
                toReturn.add(" ");
            }

            toReturn.add(CC.bGold + "Your Stats");
            toReturn.add("Wins");
            toReturn.add("Games");
            toReturn.add("Kills");
            toReturn.add("Captures");
        }
        else if(game.getState().InProgress())
        {
            String redFlagLocation = game.getFlag(TeamType.RED).getFlagCarrier() == null ? "At base" : game.getFlag(TeamType.RED).getFlagCarrier().getDisplayName();
            String blueFlagLocation = game.getFlag(TeamType.BLUE).getFlagCarrier() == null ? "At base" : game.getFlag(TeamType.BLUE).getFlagCarrier().getDisplayName();

            toReturn.add(CC.bRed + "Red Team");
            toReturn.add("  Flag: " + redFlagLocation);
            toReturn.add("  Kills: " + game.getTeam(TeamType.RED).getTotalKills());
            toReturn.add(" ");

            toReturn.add(CC.bBlue + "Red Team");
            toReturn.add("  Flag: " + blueFlagLocation);
            toReturn.add("  Kills: " + game.getTeam(TeamType.BLUE).getTotalKills());
            toReturn.add(" ");

            toReturn.add(CC.bGold + "Your Stats");
            toReturn.add(game.getPlayerGameStats(player).getKills() + " Kills");
            toReturn.add(game.getPlayerGameStats(player).getDeaths() + " Deaths");
        }

        toReturn.add(" ");
        toReturn.add(CC.bAqua + "CobbleSword.com");

        return toReturn;
    }
}
