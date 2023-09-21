package dev.cobblesword.libraries.common.items;

import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullItemBuilder extends ItemStackBuilder
{
    private static Field PROFILE_FIELD;

    public SkullItemBuilder()
    {
        super(Material.SKULL_ITEM, 1, (short) 3);
    }

    public SkullItemBuilder setSkinViaUsername(String username)
    {
        this.setSkullOwner(username);
        return this;
    }
}
