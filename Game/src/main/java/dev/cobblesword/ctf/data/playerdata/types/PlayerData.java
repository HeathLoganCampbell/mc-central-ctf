package dev.cobblesword.ctf.data.playerdata.types;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity("players")
@Getter @Setter
public class PlayerData
{
    @Id
    private UUID uuid;
    private String name;
    private PlayerConnectionStatus connectionStatus = PlayerConnectionStatus.OFFLINE;

    private int gold;
    private int exp;
    private int level;

    private int totalKills = 0;
    private int totalDeaths = 0;
    private int totalGames = 0;
    private int totalWins = 0;
    private int totalCaptures = 0;

    public void addKills(int kills)
    {
        this.totalKills += kills;
    }

    public void addDeaths(int deaths)
    {
        this.totalDeaths += deaths;
    }

    public void addWins(int wins)
    {
        this.totalWins += wins;
    }

    public void addCaptures(int captures)
    {
        this.totalCaptures += captures;
    }

    public void addGames(int games)
    {
        this.totalGames += games;
    }

    public String getPlayerLevelDisplay()
    {
        return "[" + level + "]";
    }

    public void addGold(int gold)
    {
        this.gold += gold;
    }
}
