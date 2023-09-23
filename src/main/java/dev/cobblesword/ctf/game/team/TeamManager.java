package dev.cobblesword.ctf.game.team;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TeamManager
{
    private HashMap<TeamType, Team> teams = new HashMap<>();

    private HashMap<UUID, TeamType> playersTeams = new HashMap<>();

    public void registerTeam(Team team)
    {
        teams.put(team.getTeamType(), team);
    }

    public Collection<Team> getTeams()
    {
        return this.teams.values();
    }

    public Team getTeam(TeamType teamType)
    {
        return teams.get(teamType);
    }

    public void addPlayerToTeam(TeamType teamType, Player player)
    {
        this.playersTeams.put(player.getUniqueId(), teamType);
        Team team = getTeam(teamType);
        team.addPlayer(player);
    }

    public void removePlayerFromTeam(Player player)
    {
        Team playerTeam = this.getPlayerTeam(player);
        if(playerTeam == null)
        {
            return;
        }

        playerTeam.removePlayer(player);
        this.playersTeams.remove(player.getUniqueId());
    }

    public Team getPlayerTeam(Player player)
    {
        TeamType teamType = this.playersTeams.get(player.getUniqueId());
        return this.getTeam(teamType);
    }

    public Team choiceTeam()
    {
        if (this.getTeam(TeamType.RED).getSize() < this.getTeam(TeamType.BLUE).getSize())
        {
            return this.getTeam(TeamType.RED);
        }

        return this.getTeam(TeamType.BLUE);
    }
}
