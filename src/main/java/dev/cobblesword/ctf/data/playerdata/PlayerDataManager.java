package dev.cobblesword.ctf.data.playerdata;

import dev.cobblesword.ctf.data.playerdata.database.PlayerDataRepository;
import dev.cobblesword.ctf.data.playerdata.listeners.PlayerDataListener;
import dev.cobblesword.ctf.data.playerdata.types.PlayerConnectionStatus;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager
{
    private PlayerDataRepository playerDataRepository;

    private HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerDataManager(JavaPlugin plugin, PlayerDataRepository playerDataRepository)
    {
        this.playerDataRepository = playerDataRepository;
        PlayerDataListener playerDataListener = new PlayerDataListener(this);
        Bukkit.getPluginManager().registerEvents(playerDataListener, plugin);
    }

    public void onFetch(UUID uuid, String username)
    {
        PlayerData playerData = this.playerDataRepository.get(uuid.toString());

        if(playerData == null)
        {
            playerData = new PlayerData();
            playerData.setUuid(uuid);
            playerData.setName(username);
        }

        playerData.setConnectionStatus(PlayerConnectionStatus.CONNECTING);

        this.playerDataRepository.save(playerData);

        this.playerDataMap.put(uuid, playerData);
    }

    public void onJoin(Player player)
    {
        PlayerData playerData = this.playerDataMap.get(player.getUniqueId());

        if(playerData == null)
        {
            return;
        }

        player.setLevel(playerData.getLevel());

        playerData.setConnectionStatus(PlayerConnectionStatus.ONLINE);
        this.playerDataRepository.save(playerData);
    }

    public void onLeave(UUID uuid)
    {
        PlayerData playerData = this.playerDataMap.get(uuid);

        playerData.setConnectionStatus(PlayerConnectionStatus.OFFLINE);

        this.playerDataRepository.save(playerData);
        this.playerDataMap.remove(uuid);
    }

    public PlayerData getPlayerData(UUID uuid)
    {
        return this.playerDataMap.get(uuid);
    }

    public void commit(PlayerData playerData)
    {
        this.playerDataRepository.save(playerData);
    }
}
