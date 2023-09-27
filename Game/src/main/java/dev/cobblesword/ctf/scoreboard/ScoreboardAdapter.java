package dev.cobblesword.ctf.scoreboard;

import dev.assemble.AssembleAdapter;
import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.ctf.game.GameState;
import dev.cobblesword.ctf.game.team.TeamType;
import dev.cobblesword.libraries.common.messages.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter implements AssembleAdapter
{
    @Override
    public String getTitle(Player player) {
        return CC.bGold + "Capture The Flag";
    }

    @Override
    public List<String> getLines(Player player)
    {
        final List<String> toReturn = new ArrayList<>();
        PlayerData playerData = CaptureTheFlagPlugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        Game game = CaptureTheFlagPlugin.getInstance().getGameManager().getGame();

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
            toReturn.add(playerData.getTotalWins() + " Wins");
            toReturn.add(playerData.getTotalGames() + " Games");
            toReturn.add(playerData.getTotalKills() + " Kills");
            toReturn.add(playerData.getTotalCaptures() +  " Captures");
            toReturn.add(CC.gold + playerData.getGold() +  " Gold");
        }
        else if(game.getState().InProgress())
        {
            Player redFlagCarrier = game.getTeam(TeamType.RED).getFlag().getFlagCarrier();
            Player blueFlagCarrier = game.getTeam(TeamType.BLUE).getFlag().getFlagCarrier();

            String redFlagLocation = redFlagCarrier == null ? "At base" :redFlagCarrier.getDisplayName();
            String blueFlagLocation = blueFlagCarrier == null ? "At base" : blueFlagCarrier.getDisplayName();

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
