package dev.cobblesword.ctf.perks.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Perk implements Listener
{
    private String perkName;

    public Perk(String perkName)
    {
        this.perkName = perkName;
    }

    public boolean hasPerk(Player player)
    {
        return true;
    }
}
