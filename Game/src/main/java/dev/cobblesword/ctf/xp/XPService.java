package dev.cobblesword.ctf.xp;

import org.bukkit.entity.Player;

public class XPService
{
    public void addExperience(Player player, int amount)
    {
        int totalXp = 1000 + amount;
        if(totalXp > 5000)
        {
            levelUp(player);
        }
    }

    public void levelUp(Player player)
    {

    }
}
