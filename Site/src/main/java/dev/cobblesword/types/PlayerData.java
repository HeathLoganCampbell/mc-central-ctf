package dev.cobblesword.types;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("players")
@Getter
@Setter
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

    private transient int currentPlacement = -1;
}
