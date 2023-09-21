package dev.cobblesword.libraries.common.world;

import org.bukkit.*;

public class Worlds
{
    private static final EmptyChunk EMPTY_CHUNK = new EmptyChunk();

    public static void initStaticWorld(World world, boolean pvp)
    {
        world.setAutoSave(false);
        world.setAmbientSpawnLimit(0);
        world.setAnimalSpawnLimit(0);
        world.setDifficulty(Difficulty.HARD);
        world.setKeepSpawnInMemory(true);
        world.setMonsterSpawnLimit(0);
        world.setPVP(pvp);
        world.setTicksPerAnimalSpawns(Integer.MAX_VALUE);
        world.setTicksPerMonsterSpawns(Integer.MAX_VALUE);
        world.setWaterAnimalSpawnLimit(0);
        world.setSpawnFlags(false, false);
        if (world.isThundering())
            world.setThunderDuration(Integer.MAX_VALUE);

        world.setWeatherDuration(Integer.MAX_VALUE);
    }

    public static World createEmptyWorld(String name)
    {
        WorldCreator creator = new WorldCreator(name);
        creator.environment(World.Environment.NORMAL);
        creator.generateStructures(false);
        creator.seed(1L);
        creator.type(WorldType.FLAT);
        creator.generator(EMPTY_CHUNK);
        return creator.createWorld();
    }
}
