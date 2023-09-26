package dev.cobblesword.ctf.data.gamedata.types;

import dev.cobblesword.ctf.game.team.TeamType;

import java.util.ArrayList;
import java.util.List;

public class TeamGameData
{
    private TeamType teamType;
    private int playerCount;

    private int kills;
    private int deaths;

    private List<PlayerGameData> members = new ArrayList<>();
}
