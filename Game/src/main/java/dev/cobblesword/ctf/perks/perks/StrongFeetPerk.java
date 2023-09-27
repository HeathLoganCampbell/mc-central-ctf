package dev.cobblesword.ctf.perks.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class StrongFeetPerk extends Perk
{
    public StrongFeetPerk()
    {
        super("Strong Feet");
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e)
    {
        if(!(e.getEntity() instanceof Player))
        {
            return;
        }
        Player player = (Player) e.getEntity();

        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        if(!this.hasPerk(player))
        {
            return;
        }

        e.setCancelled(true);
    }
}
