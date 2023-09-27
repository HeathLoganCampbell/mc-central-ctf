package dev.cobblesword.ctf.game.team;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.flag.Flag;
import dev.cobblesword.libraries.common.utils.PlayerUtils;
import dev.cobblesword.libraries.common.items.ItemStackBuilder;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Team
{
    private String name;

    private String chatColor;

    private Color dyeColor;

    private Location spawn;

    private Flag flag;

    private List<UUID> players;

    private TeamType teamType;

    private org.bukkit.scoreboard.Team nmsTeam;

    private int totalKills = 0;

    public Team(String name, ChatColor chatColor, Color dyeColor, TeamType teamType, Location spawnLocation, Location flagLocation)
    {
        this.name = name;
        this.chatColor = chatColor.toString();
        this.dyeColor = dyeColor;
        this.teamType = teamType;
        this.spawn = spawnLocation;
        this.flag = new Flag(dyeColor, name, chatColor.toString(), flagLocation);
        this.players = new ArrayList<>();
        nmsTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name);
        if(nmsTeam == null)
        {
            nmsTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
        }
        nmsTeam.setPrefix(chatColor.toString());
    }

    public void spawn(Player player)
    {
        player.teleport(spawn);
        applyKit(player);
    }

    public void addPlayer(Player player)
    {
        player.setDisplayName(chatColor + player.getName());
        player.setPlayerListName(player.getDisplayName());
        players.add(player.getUniqueId());
        nmsTeam.addPlayer(player);
    }

    public void removePlayer(Player player)
    {
        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getDisplayName());
        nmsTeam.removePlayer(player);
        players.remove(player.getUniqueId());
    }

    public int getSize()
    {
        return players.size();
    }

    public void applyKit(Player player)
    {
        player.setGameMode(GameMode.ADVENTURE);
        PlayerUtils.reset(player);

        PlayerInventory inventory = player.getInventory();

        inventory.setItem(0, new ItemStack(Material.STONE_SWORD));
        inventory.setItem(1, new ItemStackBuilder(Material.BOW).enchantment(Enchantment.ARROW_INFINITE, 1).enchantment(Enchantment.ARROW_DAMAGE, 1).build());

        inventory.setItem(8, new ItemStack(Material.COMPASS));
        inventory.setItem(9, new ItemStack(Material.ARROW, 32));

        inventory.setHelmet(new ItemStackBuilder(Material.LEATHER_HELMET).setLeatherColour(dyeColor).build());
        inventory.setChestplate(new ItemStackBuilder(Material.LEATHER_CHESTPLATE).setLeatherColour(dyeColor).build());
        inventory.setLeggings(new ItemStackBuilder(Material.LEATHER_LEGGINGS).setLeatherColour(dyeColor).build());
        inventory.setBoots(new ItemStackBuilder(Material.LEATHER_BOOTS).setLeatherColour(dyeColor).build());

        CaptureTheFlagPlugin.getInstance().getLevelModule().applyLevelToBar(player);
    }

    public int getTotalKills()
    {
        return totalKills;
    }

    public void addKill()
    {
        totalKills++;
    }
}
