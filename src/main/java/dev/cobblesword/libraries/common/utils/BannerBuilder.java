package dev.cobblesword.libraries.common.utils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;

public class BannerBuilder {

    private final List<Pattern> patterns = new ArrayList<>();
    private DyeColor baseColor = DyeColor.WHITE;

    public BannerBuilder setBaseColor(DyeColor baseColor) {
        this.baseColor = baseColor;
        return this;
    }

    public BannerBuilder addPattern(DyeColor color, PatternType patternType) {
        patterns.add(new Pattern(color, patternType));
        return this;
    }

    public ItemStack build() {
        ItemStack banner = new ItemStack(Material.BANNER, 1, baseColor.getDyeData());
        BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();

        for (Pattern pattern : patterns) {
            bannerMeta.addPattern(pattern);
        }

        banner.setItemMeta(bannerMeta);
        return banner;
    }
}
