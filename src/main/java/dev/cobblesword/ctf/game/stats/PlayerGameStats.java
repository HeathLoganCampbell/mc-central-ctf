package dev.cobblesword.ctf.game.stats;

import dev.cobblesword.ctf.game.TeamType;

import java.util.UUID;

public class PlayerGameStats
{
    private int id;
    private int playerId;
    private TeamType teamType;

    private String playerName = "Unknown";
    private UUID playerUUID;

    private int kills = 0;
    private int deaths = 0;
}
