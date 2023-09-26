package dev.cobblesword.libraries.common.items;

import org.bukkit.Material;

import java.lang.reflect.Field;

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
