package dev.cobblesword.ctf.perks;

import dev.cobblesword.ctf.perks.perks.Perk;
import dev.cobblesword.ctf.perks.perks.StrongFeetPerk;
import dev.cobblesword.libraries.modules.serverstartup.Module;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PerkModule extends Module
{
    private List<Perk> perks = new ArrayList<Perk>();

    public PerkModule(JavaPlugin plugin)
    {
        super("Perks", plugin);
    }

    public void onEnable()
    {
        perks.add(new StrongFeetPerk());

        for (Perk perk : perks) {
            this.registerEvents(perk);
        }
    }
}
