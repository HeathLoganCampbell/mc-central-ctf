package dev.cobblesword.libraries.modules.levels.customevents;

import dev.cobblesword.ctf.data.playerdata.customevents.PlayerDataEvent;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;

public class PlayerLeveUpEvent extends PlayerDataEvent
{
    private int levelsUps = 0;

    public PlayerLeveUpEvent(PlayerData playerData, int amountOfLevels)
    {
        super(playerData);

        this.levelsUps = amountOfLevels;
    }
}