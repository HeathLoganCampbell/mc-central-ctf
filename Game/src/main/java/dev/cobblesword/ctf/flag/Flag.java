package dev.cobblesword.ctf.flag;

import dev.cobblesword.libraries.common.utils.ColorUtils;
import dev.cobblesword.libraries.common.items.ItemStackBuilder;
import dev.cobblesword.libraries.common.messages.CC;
import dev.cobblesword.libraries.common.utils.BannerBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@Getter
public class Flag
{
    private String teamName;

    private String teamChatColor;
    private Color flagColor;
    @Getter
    private Location flagSpawnLocation;

    private ArmorStand flagEntity;

    private Player flagCarrier;

    public Flag(Color dyeColor, String teamName, String teamChatColor, Location flagLocation)
    {
        this.flagColor = dyeColor;
        this.teamName = teamName;
        this.teamChatColor = teamChatColor;
        this.flagSpawnLocation = flagLocation;
    }

    public void setUpFlag()
    {
        this.spawn();
    }

    public void pickUpFlag(Player player)
    {
        if(this.getFlagCarrier() != null)
        {
            return;
        }

        this.remove();
        this.setFlagCarrier(player);
        this.applyFlagKit(player);
        this.flagSpawnLocation.getWorld().strikeLightningEffect(this.flagSpawnLocation);
        Bukkit.broadcastMessage(CC.highlight(player.getDisplayName()) + " has stolen the flag");
    }

    public void dropFlag()
    {
        this.spawn();
        Bukkit.broadcastMessage(CC.highlight(this.flagCarrier.getDisplayName()) + " has dropped the flag");
        this.flagCarrier = null;
    }

    private void spawn()
    {
        System.out.println("> " + ColorUtils.getDyeColor(flagColor) + " " +flagColor );
        ItemStack flagItem = new BannerBuilder().setBaseColor(ColorUtils.getDyeColor(flagColor)).build();

        ArmorStand flag = (ArmorStand) this.flagSpawnLocation.getWorld().spawnEntity(this.flagSpawnLocation, EntityType.ARMOR_STAND);
        flag.setVisible(false);
        flag.setHelmet(flagItem);
        flag.setGravity(false);
        flag.setCanPickupItems(false);
        this.flagEntity = flag;
    }

    public Location getActiveFlagLocation()
    {
        if(this.flagCarrier == null)
        {
            return this.flagSpawnLocation;
        }

        return this.flagCarrier.getLocation();
    }

    private void remove()
    {
        this.flagEntity.remove();
    }

    private void setFlagCarrier(Player player)
    {
        this.flagCarrier = player;
    }

    private void applyFlagKit(Player player)
    {
        ItemStack rawFlagItem = new BannerBuilder().addPattern(ColorUtils.getDyeColor(flagColor), PatternType.BASE).build();

        ItemStack flagItem = new ItemStackBuilder(rawFlagItem)
                .displayName(teamChatColor + teamName + "'s Flag")
                .lore(CC.gray + "Bring this flag back", CC.gray + "to your teams spawn", CC.gray + "to win!")
                .build();

        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHelmet(flagItem);

        for (int i = 0; i < 9; i++)
        {
            inventory.setItem(i, flagItem);
        }
    }
}
