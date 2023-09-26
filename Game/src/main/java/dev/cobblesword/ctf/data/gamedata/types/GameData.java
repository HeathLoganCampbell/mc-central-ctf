package dev.cobblesword.ctf.data.gamedata.types;

import dev.cobblesword.ctf.game.GameState;

public class GameData
{
    private String id;
    private String mapName;
    private long startTimestamp = Long.MIN_VALUE;
    private long endTimestamp = Long.MIN_VALUE;
    private GameState gameState;

    private TeamGameData redTeamData;
    private TeamGameData blueTeamData;
}
