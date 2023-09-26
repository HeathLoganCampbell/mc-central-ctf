package dev.cobblesword.libraries.common.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils
{
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * <p>Converts format <code>id:data</code>, for example 35:6 would be Pink Wool</p>
     *
     * @param string - {@link String} containing block data
     * @return - {@link ItemStack} instance
     */
    @SuppressWarnings({"deprecation"})
    public static ItemStack string2ItemStack(String string) {
        String itemType[] = string.split(":");

        return new ItemStack(
                Material.getMaterial(Integer.parseInt(itemType[0])),
                Integer.parseInt(itemType[2]),
                (short) Integer.parseInt(itemType[1])
        );
    }
}
