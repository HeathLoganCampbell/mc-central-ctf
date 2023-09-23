package dev.cobblesword.ctf.playerdata.types;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class PlayerData
{
    private UUID uuid;
    private String name;
    private PlayerConnectionStatus connectionStatus = PlayerConnectionStatus.OFFLINE;

    private int coins;
    private int exp;
    private int level;

    private int totalKills = 0;
    private int totalDeaths = 0;
    private int totalGames = 0;
    private int totalWins = 0;
    private int totalCaptures = 0;
}
