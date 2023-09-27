package dev.cobblesword.libraries.modules.levels;


import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.cobblesword.libraries.common.messages.CC;
import dev.cobblesword.libraries.modules.levels.commands.LevelsCommand;
import dev.cobblesword.libraries.modules.levels.customevents.PlayerLeveUpEvent;
import dev.cobblesword.libraries.modules.serverstartup.Module;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class LevelModule extends Module
{
    public LevelModule(JavaPlugin plugin)
    {
        super("Levels", plugin);
    }

    public void addExp(Player player, int exp)
    {
        PlayerData playerData = CaptureTheFlagPlugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        this.addExp(playerData, exp);
    }

    public float getPercentageTilNextLevel(Player player)
    {
        PlayerData playerData = CaptureTheFlagPlugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        int currentExp = playerData.getExp();
        int requireExp = getExpRequired(playerData.getLevel() + 1);

        float percentage = (float) currentExp / (float) requireExp;
        return percentage;
    }

    public void addExp(PlayerData playerData, int exp)
    {
        playerData.addExp(exp);

        int levelsUp = 0;
        int expRequired = this.getExpRequired(playerData.getLevel());
        while(playerData.getExp() >= expRequired)
        {
            int remaining = playerData.getExp() - expRequired;
            playerData.setExp(remaining);

            //Update level
            playerData.setLevel(playerData.getLevel());
            levelsUp++;
            expRequired = this.getExpRequired(playerData.getLevel());
        }

        Player player = Bukkit.getPlayer(playerData.getUuid());
        if(player != null)
        {
            applyLevelToBar(player);
        }

        if(levelsUp > 0)
        {
            new PlayerLeveUpEvent(playerData, levelsUp).call();

            if(player != null)
            {
                Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();

                meta.setPower(2);

                FireworkEffect effect1 = FireworkEffect.builder()
                        .withColor(Color.RED, Color.ORANGE) // Primary colors
                        .withFade(Color.YELLOW) // Fade effect color
                        .with(FireworkEffect.Type.BURST) // Effect type
                        .flicker(true) // Flicker effect
                        .trail(true) // Trail effect
                        .build();

                FireworkEffect effect2 = FireworkEffect.builder()
                        .withColor(Color.BLUE, Color.AQUA) // Primary colors
                        .withFade(Color.BLUE) // Fade effect color
                        .with(FireworkEffect.Type.STAR) // Effect type
                        .flicker(false) // Flicker effect
                        .trail(true) // Trail effect
                        .build();

                meta.addEffect(effect1);
                meta.addEffect(effect2);

                firework.setFireworkMeta(meta);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);

                applyLevelToBar(player);
            }
        }
    }

    public static int getExpRequired(long level)
    {
        int expRequired = 5000;

        if(level == 0)
        {
            expRequired = 500;
        }
        else if(level == 1)
        {
            expRequired = 1000;
        }
        else if(level == 2)
        {
            expRequired = 2000;
        }
        else if(level == 3)
        {
            expRequired = 3000;
        }
        else if(level == 4)
        {
            expRequired = 4000;
        }

        return expRequired;
    }

    public String getColorPrefix(int level)
    {
        String color = CC.gray;
        if(level < 100)
            color = CC.gray;//STONE
        else if(level < 200)
            color = CC.white;//IRON
        else if(level < 300)
            color = CC.gold;//GOLd
        else if(level < 400)
            color = CC.aqua;//diamond
        else if(level < 500)
            color = CC.green;//Emerald
        else if(level < 600)
            color = CC.dAqua;//Sapphire Prestige
        else if(level < 700)
            color = CC.dRed;//ruby Prestige
        else if(level < 800)
            color = CC.pink;//Crystal Prestige
        else if(level < 900)
            color = CC.blue;//Opal  Prestige
        else if(level < 1000)
            color = CC.dPurple;//Amethyst  Prestige
        else
            color = CC.bWhite;//Amethyst  Prestige
        return color;
    }

    public void applyLevelToBar(Player player)
    {
        PlayerData playerData = CaptureTheFlagPlugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

        if(playerData != null)
        {
            player.setLevel(playerData.getLevel());
            player.setExp(getPercentageTilNextLevel(player));
        }
    }

    @Override
    protected void onEnable() {
        this.registerCommands(new LevelsCommand(this));
    }
}