package dev.cobblesword.ctf.game;

import dev.cobblesword.ctf.flag.Flag;
import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.cobblesword.ctf.flag.FlagListener;
import dev.cobblesword.ctf.data.gamedata.types.PlayerGameData;
import dev.cobblesword.ctf.game.team.Team;
import dev.cobblesword.ctf.game.team.TeamManager;
import dev.cobblesword.ctf.game.team.TeamType;
import dev.cobblesword.ctf.map.GameMap;
import dev.cobblesword.libraries.common.messages.CC;
import dev.cobblesword.libraries.common.world.Worlds;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Stream;

public class Game
{
    @Getter @Setter
    private boolean inProgress = false;

    private GameMap gameMap;

    private HashMap<UUID, PlayerGameData> playerGameStatsMap = new HashMap<>();

    @Getter
    private Team winningTeam;

    private World gameWorld;

    @Getter
    private List<Player> gamers = new ArrayList<Player>();

    @Getter
    private TeamManager teamManager;

    private GameListener gameListener;

    public Game(JavaPlugin plugin)
    {
        // Load map
        this.gameWorld = Worlds.createEmptyWorld("gameWorld");
        this.teamManager = new TeamManager();

        this.gameMap = new GameMap(gameWorld);

        this.teamManager.registerTeam(new Team("Blue", ChatColor.BLUE, Color.BLUE, TeamType.BLUE, new Location(this.gameWorld, 23.5, 51.5, 39.5, 135f, -9.5f), new Location(this.gameWorld, 47.5, 75.0, 62.5)));
        this.teamManager.registerTeam(new Team("Red", ChatColor.RED, Color.RED, TeamType.RED, new Location(this.gameWorld, -72.5, 51.5, -54.5, -45f, -9.5f), new Location(this.gameWorld, -96.5, 75, -77.5)));
    }

    public void join(Player player)
    {
        gamers.add(player);
        playerGameStatsMap.put(player.getUniqueId(), new PlayerGameData(player.getName(), player.getUniqueId()));
        player.setGameMode(GameMode.ADVENTURE);
        System.out.println("Game> " + player.getName() + " joined");
    }

    public void leave(Player player)
    {
        playerGameStatsMap.remove(player.getUniqueId());
        gamers.remove(player);
        this.teamManager.removePlayerFromTeam(player);

        System.out.println("Game> " + player.getName() + " left");
    }

    public Collection<Flag> getAllFlags()
    {
        ArrayList<Flag> flags = new ArrayList<>();
        for (Team team : this.teamManager.getTeams())
        {
            Flag flag = team.getFlag();
            flags.add(flag);
        }

        return flags;
    }

    private void assignPlayersTeams()
    {
        List<Player> onlinePlayersList = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(onlinePlayersList);

        for (Player onlinePlayer : onlinePlayersList)
        {
            Team currentTeam = this.getPlayerTeam(onlinePlayer);
            if(currentTeam != null)
            {
                continue;
            }

            Team team = teamManager.choiceTeam();
            teamManager.addPlayerToTeam(team.getTeamType(), onlinePlayer);
        }
    }

    private void teleportTeamsToMap()
    {
        for (Player onlinePlayer : this.gamers)
        {
            Team team = getPlayerTeam(onlinePlayer);
            if(team == null)
            {
                continue;
            }

            team.spawn(onlinePlayer);
            System.out.println("Spawned " + onlinePlayer.getName());
        }
    }


    private void broadcastGameInfo()
    {
        Bukkit.broadcastMessage(CC.bYellow + CC.strikeThrough + "=====================================");
        Bukkit.broadcastMessage(CC.bYellow + "");
        Bukkit.broadcastMessage(CC.bYellow + "    Map: " + CC.white + "Castle Wars");
        Bukkit.broadcastMessage(CC.bYellow + "    Author: " + CC.white + "????");
        Bukkit.broadcastMessage(CC.bYellow + "");
        Bukkit.broadcastMessage(CC.bYellow + "    Get the enemy's flag and bring it back to your base");
        Bukkit.broadcastMessage(CC.bYellow + "");
        Bukkit.broadcastMessage(CC.bYellow + CC.strikeThrough + "=====================================");
    }

    private void broadcastChampion()
    {
        String gameId = "GAME#12332";
        List<String> players = new ArrayList<>();

        for (Player onlinePlayer : this.gamers)
        {
            players.add(onlinePlayer.getName());
            Team playerTeam = this.getPlayerTeam(onlinePlayer);

            onlinePlayer.sendMessage(CC.bYellow + CC.strikeThrough + "=============[" + CC.r + " " + CC.bAqua + gameId + " " + CC.bYellow + CC.strikeThrough + "]=============");
            onlinePlayer.sendMessage(CC.bYellow + "");
            if(winningTeam == playerTeam)
            {
                onlinePlayer.sendMessage(CC.bYellow + "                 VICTORY!");
            }
            else if(winningTeam == null)
            {
                onlinePlayer.sendMessage(CC.bGray + "                 DRAW!");
            }
            else
            {
                onlinePlayer.sendMessage(CC.bRed + "                 DEFEAT");
            }
            onlinePlayer.sendMessage(CC.bYellow + "");
            onlinePlayer.sendMessage(CC.bYellow + "               " + this.getGoldEarned(onlinePlayer) + " gold");
            onlinePlayer.sendMessage(CC.bYellow + CC.strikeThrough + "========================================");
        }
    }

    private void setUpMap()
    {
        Location spawn = this.getTeamManager().getTeam(TeamType.RED).getSpawn();
        for (Entity entity : spawn.getWorld().getEntities())
        {
            if(entity instanceof LivingEntity && !(entity instanceof Player))
            {
                entity.remove();
            }
        }

        this.getTeam(TeamType.RED).getFlag().setUpFlag();
        this.getTeam(TeamType.BLUE).getFlag().setUpFlag();

        Bukkit.getPluginManager().registerEvents(new FlagListener(this, CaptureTheFlagPlugin.getInstance()), CaptureTheFlagPlugin.getInstance());
    }

    private void handleTimeoutWinner()
    {
        if(this.getTeam(TeamType.RED).getTotalKills() ==  this.getTeam(TeamType.BLUE).getTotalKills())
        {
            // Draw
            winningTeam = null;
        }

        if(this.getTeam(TeamType.RED).getTotalKills() >  this.getTeam(TeamType.BLUE).getTotalKills())
        {
            winningTeam = this.getTeam(TeamType.RED);
        }

        if(this.getTeam(TeamType.RED).getTotalKills() <  this.getTeam(TeamType.BLUE).getTotalKills())
        {
            winningTeam =  this.getTeam(TeamType.BLUE);
        }
    }

    public Team getPlayerTeam(Player player)
    {
        return this.teamManager.getPlayerTeam(player);
    }

    public Team getTeam(TeamType teamType)
    {
        return this.teamManager.getTeam(teamType);
    }

    public void captureFlag(Player flagCarrier)
    {
        Team playerTeam = this.getPlayerTeam(flagCarrier);

        flagCarrier.getWorld().strikeLightningEffect(flagCarrier.getLocation());

        playerTeam.applyKit(flagCarrier);

        PlayerGameData playerGameData = playerGameStatsMap.get(flagCarrier.getUniqueId());
        playerGameData.addCapture();

        winningTeam = playerTeam;
    }

    private int getGoldEarned(Player player)
    {
        PlayerGameData playerGameData = playerGameStatsMap.get(player.getUniqueId());
        Team team = this.getPlayerTeam(player);

        int didWin = this.winningTeam == team ? 1 : 0;

        int goldPerKill = 20;
        int goldPerWin = 500;
        int goldPerCapture = 300;

        int goldToGive = (goldPerKill * playerGameData.getKills()) + (goldPerWin * didWin) + (goldPerCapture * playerGameData.getCaptures());
        return goldToGive;
    }

    public void onGiveRewards()
    {
        for (Player player : this.gamers)
        {
            PlayerGameData playerGameData = playerGameStatsMap.get(player.getUniqueId());
            PlayerData playerData = CaptureTheFlagPlugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
            Team team = this.getPlayerTeam(player);

            int didWin = this.winningTeam == team ? 1 : 0;

            playerData.addKills(playerGameData.getKills());
            playerData.addDeaths(playerGameData.getDeaths());
            playerData.addGames(1);
            playerData.addWins(didWin);
            playerData.addCaptures(playerGameData.getCaptures());

            playerData.addGold(getGoldEarned(player));

            CaptureTheFlagPlugin.getPlayerDataManager().commit(playerData);
        }
    }

    public void onStart()
    {
        Location redTeamSpawn1 = gameMap.getRedTeamSpawn();

        redTeamSpawn1.getWorld().setTime(1200L);
        Worlds.initStaticWorld(redTeamSpawn1.getWorld(), true);

        setUpMap();

        assignPlayersTeams();

        teleportTeamsToMap();

        broadcastGameInfo();

        gameListener = new GameListener(this);
        Bukkit.getPluginManager().registerEvents(gameListener, CaptureTheFlagPlugin.getInstance());
    }

    public void onFinish(FinishReason finishReason)
    {
        if(finishReason == FinishReason.TIME_OUT)
        {
            handleTimeoutWinner();
        }

        HandlerList.unregisterAll(this.gameListener);
        this.setInProgress(false);
        onGiveRewards();
        broadcastChampion();
    }

    public boolean hasWinner()
    {
        return this.winningTeam != null;
    }

    public PlayerGameData getPlayerGameStats(Player player)
    {
        return this.playerGameStatsMap.get(player.getUniqueId());
    }
}
