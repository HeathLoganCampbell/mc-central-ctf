package dev.cobblesword.libraries.common.world;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class EmptyChunk extends ChunkGenerator
{
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome)
    {
        return this.createChunkData(world);
    }

    public boolean isParallelCapable() {
        return true;
    }
}
