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
}
