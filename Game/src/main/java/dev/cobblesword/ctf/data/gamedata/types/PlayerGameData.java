package dev.cobblesword.ctf.data.gamedata.types;

import dev.cobblesword.ctf.game.team.TeamType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@Data
public class PlayerGameData
{
    private int id;
    private int playerId;
    private TeamType teamType;

    private final String playerName;
    private final UUID playerUUID;

    private int kills = 0;
    private int deaths = 0;

    private int captures = 0;

    public void addCapture()
    {
        this.captures++;
    }

    public void addKill()
    {
        this.kills++;
    }

    public void addDeath()
    {
        this.deaths++;
    }
}
