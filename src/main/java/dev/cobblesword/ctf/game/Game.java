package dev.cobblesword.ctf.game;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.cobblesword.ctf.flag.Flag;
import dev.cobblesword.ctf.flag.FlagListener;
import dev.cobblesword.ctf.data.gamedata.types.PlayerGameData;
import dev.cobblesword.ctf.game.team.Team;
import dev.cobblesword.ctf.game.team.TeamManager;
import dev.cobblesword.ctf.game.team.TeamType;
import dev.cobblesword.ctf.map.GameMap;
import dev.cobblesword.libraries.common.messages.CC;
import dev.cobblesword.libraries.common.world.Worlds;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Stream;

public class Game implements Runnable
{
    private GameState state = GameState.SETTING_UP;

    @Getter
    private int secondsRemaining = -1;

    private List<Player> gamers = new ArrayList<Player>();

    private GameMap gameMap;

    private HashMap<UUID, PlayerGameData> playerGameStatsMap = new HashMap<>();

    private Team winningTeam;

    private World gameWorld;

    private TeamManager teamManager;

    public Game(JavaPlugin plugin)
    {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20L, 20L);
        Bukkit.getPluginManager().registerEvents(new GameListener(this), plugin);

        // Load map
        this.gameWorld = Worlds.createEmptyWorld("gameWorld");
        this.teamManager = new TeamManager();

        this.gameMap = new GameMap(gameWorld);

        this.teamManager.registerTeam(new Team("Blue", ChatColor.BLUE, Color.BLUE, TeamType.BLUE, new Location(this.gameWorld, 23.5, 51.5, 39.5, 135f, -9.5f), new Location(this.gameWorld, 47.5, 75.0, 62.5)));
        this.teamManager.registerTeam(new Team("Red", ChatColor.RED, Color.RED, TeamType.RED, new Location(this.gameWorld, -72.5, 51.5, -54.5, -45f, -9.5f), new Location(this.gameWorld, -96.5, 75, -77.5)));

        this.setState(GameState.WAITING_FOR_PLAYERS);
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
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            Team team = teamManager.choiceTeam();
            teamManager.addPlayerToTeam(team.getTeamType(), onlinePlayer);
        }
    }

    private void teleportTeamsToMap()
    {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
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
        if(winningTeam != null)
        {
            String gameId = "GAME#12332";

            Bukkit.broadcastMessage(CC.bYellow + CC.strikeThrough + "=============[" + CC.r + " " + CC.bAqua + gameId + " " + CC.bYellow + CC.strikeThrough + "]=============");
            Bukkit.broadcastMessage(CC.bYellow + "                 " + winningTeam.getChatColor() + winningTeam.getName() + " has won!");
            Bukkit.broadcastMessage(CC.gray + "                 Reason: Flag Capture");
            Bukkit.broadcastMessage(CC.bYellow + "");
            Bukkit.broadcastMessage(CC.bYellow + "      Flag Capture      Most kills" );
            Bukkit.broadcastMessage(CC.bYellow + "+38 Coins");
            Bukkit.broadcastMessage(CC.bYellow + CC.strikeThrough + "========================================");
        }
        else
        {
            Bukkit.broadcastMessage(CC.bYellow + CC.strikeThrough + "=====================================");
            Bukkit.broadcastMessage(CC.bYellow + "");
            Bukkit.broadcastMessage(CC.bYellow + " Draw!");
            Bukkit.broadcastMessage(CC.gray + "Reason: Both teams have equal kills");
            Bukkit.broadcastMessage(CC.bYellow + "");
            Bukkit.broadcastMessage(CC.bYellow + "Flag Capture" );
            Bukkit.broadcastMessage(CC.bYellow + "Most kills" );
            Bukkit.broadcastMessage(CC.bYellow + "    ");
            Bukkit.broadcastMessage(CC.bYellow + "+38 Coins");
            Bukkit.broadcastMessage(CC.bYellow + "");
            Bukkit.broadcastMessage(CC.bYellow + CC.strikeThrough + "=====================================");
        }
    }

    private void setUpMap()
    {
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

    public void setState(GameState state)
    {
        System.out.println(this.state + " => " + state);
        this.state = state;
        secondsRemaining = state.getSeconds();
    }

    @Override
    public void run()
    {
        if(state == GameState.WAITING_FOR_PLAYERS)
        {
            int minPlayers = 2;
            if(gamers.size() >= minPlayers)
            {
                System.out.println("has enough players");

                this.setState(GameState.COUNTDOWN);
            }
        }

        if(state == GameState.CELEBRATE)
        {
            if(winningTeam != null)
            {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Team playerTeam = this.getPlayerTeam(onlinePlayer);
                    if(playerTeam == winningTeam)
                    {
                        Firework firework = onlinePlayer.getWorld().spawn(onlinePlayer.getLocation(), Firework.class);
                        FireworkMeta meta = firework.getFireworkMeta();
                        meta.addEffect(FireworkEffect.builder()
                                .withColor(winningTeam.getDyeColor())
                                .with(FireworkEffect.Type.BALL)
                                .build());
                        firework.setFireworkMeta(meta);
                    }
                }
            }
        }

        if(state == GameState.PREPARE_GAME)
        {
            Location redTeamSpawn1 = gameMap.getRedTeamSpawn();

            redTeamSpawn1.getWorld().setTime(1200L);
            Worlds.initStaticWorld(redTeamSpawn1.getWorld(), true);


            assignPlayersTeams();

            teleportTeamsToMap();

            broadcastGameInfo();

            setUpMap();

            this.setState(GameState.IN_PROGRESS);
        }

        boolean changeState = secondsRemaining == 0;
        if((winningTeam != null || changeState) && state == GameState.IN_PROGRESS)
        {
            this.setState(GameState.CELEBRATE);

            if(changeState && winningTeam == null )
            {
                handleTimeoutWinner();
            }

            onGiveRewards();
            broadcastChampion();
        }

        if(changeState)
        {
            if(state == GameState.COUNTDOWN)
            {
                this.setState(GameState.PREPARE_GAME);
            }

            if(state == GameState.CELEBRATE)
            {
                this.setState(GameState.ENDED);
                Bukkit.getServer().shutdown();
            }
        }


        if(secondsRemaining != -1)
        {
            if(this.state == GameState.COUNTDOWN)
            {
                if(Stream.of(1, 2, 3, 4, 5, 10, 15, 30, 45, 60).anyMatch(sec -> secondsRemaining == sec))
                {
                    Bukkit.broadcastMessage(CC.gray + "Game starting in " + CC.highlight(this.secondsRemaining + "") + " seconds.");
                }
            }

            if(this.state == GameState.IN_PROGRESS)
            {
                // minutes
                if(Stream.of(60, (60 * 2), (60 * 5), (60 * 10)).anyMatch(sec -> secondsRemaining == sec))
                {
                    Bukkit.broadcastMessage(CC.gray + "Game ends in " + CC.highlight((this.secondsRemaining / 60) + "") + " minutes.");
                }

                // seconds
                if(Stream.of(1, 2, 3, 4, 5, 10, 30, 45).anyMatch(sec -> secondsRemaining == sec))
                {
                    Bukkit.broadcastMessage(CC.gray + "Game ends in " + CC.highlight(this.secondsRemaining + "") + " seconds.");
                }
            }

            secondsRemaining--;
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

    public void onGiveRewards()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            PlayerGameData playerGameData = playerGameStatsMap.get(player.getUniqueId());
            PlayerData playerData = CaptureTheFlagPlugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
            Team team = this.getPlayerTeam(player);

            playerData.addKills(playerGameData.getKills());
            playerData.addDeaths(playerGameData.getDeaths());
            playerData.addGames(1);
            playerData.addWins(this.winningTeam == team ? 1 : 0);
            playerData.addCaptures(playerGameData.getCaptures());
        }
    }

    public GameState getState() {
        return this.state;
    }

    public PlayerGameData getPlayerGameStats(Player player)
    {
        return this.playerGameStatsMap.get(player.getUniqueId());
    }
}
