package dev.cobblesword.ctf.map;

import dev.cobblesword.ctf.map.configs.GameMapData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class GameMap
{
    private World world;

    public GameMap(World gameWorld) {
        this.world = gameWorld;
    }

    public Location getRedTeamSpawn()
    {
        return new Location(this.world, -72.5, 51.5, -54.5, -45f, -9.5f);
    }

    public Location getBlueTeamSpawn()
    {
        return new Location(this.world, 23.5, 51.5, 39.5, 135f, -9.5f);
    }
}
