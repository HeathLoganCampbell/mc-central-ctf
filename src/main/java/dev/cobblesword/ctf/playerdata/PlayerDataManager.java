package dev.cobblesword.ctf.playerdata;

import dev.cobblesword.ctf.playerdata.listeners.PlayerDataListener;
import dev.cobblesword.ctf.playerdata.types.PlayerConnectionStatus;
import dev.cobblesword.ctf.playerdata.types.PlayerData;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager
{
    private HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerDataManager()
    {
        PlayerDataListener playerDataListener = new PlayerDataListener(this);
    }

    public void onFetch(UUID uuid)
    {
        PlayerData playerData = new PlayerData();

        playerData.setUuid(uuid);
        playerData.setConnectionStatus(PlayerConnectionStatus.CONNECTING);

        this.playerDataMap.put(uuid, playerData);
    }

    public void onJoin(UUID uuid)
    {
        PlayerData playerData = this.playerDataMap.get(uuid);

        if(playerData == null)
        {
            return;
        }

        playerData.setConnectionStatus(PlayerConnectionStatus.ONLINE);
    }

    public void onLeave(UUID uuid)
    {
        this.playerDataMap.remove(uuid);
    }
}
