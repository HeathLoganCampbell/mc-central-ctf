package dev.cobblesword.libraries.common.utils;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Color;
import org.bukkit.DyeColor;

public class ColorUtils
{
    public static DyeColor getDyeColor(Color color)
    {
        if(color == Color.RED)
        {
            return DyeColor.RED;
        }

        if(color == Color.BLUE)
        {
            return DyeColor.BLUE;
        }

        throw new NotImplementedException("Unknown color " + color);
    }
}
