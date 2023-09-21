package dev.cobblesword.ctf.game;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.flag.Flag;
import dev.cobblesword.ctf.flag.FlagListener;
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

    private Team redTeam;
    private Team blueTeam;

    private HashMap<TeamType, Flag> flagMap = new HashMap<>();

    private HashMap<TeamType, Team> teams = new HashMap<>();

    private HashMap<UUID, TeamType> playersTeams = new HashMap<>();

    private Team winningTeam;

    private World gameWorld;

    public Game(JavaPlugin plugin)
    {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20L, 20L);
        Bukkit.getPluginManager().registerEvents(new GameListener(this), plugin);

        // Load map
        this.gameWorld = Worlds.createEmptyWorld("gameWorld");

        this.gameMap = new GameMap(gameWorld);

        this.blueTeam = new Team("Blue", ChatColor.BLUE, Color.BLUE, TeamType.BLUE, new Location(this.gameWorld, 23.5, 51.5, 39.5, 135f, -9.5f), new Location(this.gameWorld, 47.5, 75.0, 62.5));
        this.redTeam = new Team("Red", ChatColor.RED, Color.RED, TeamType.RED, new Location(this.gameWorld, -72.5, 51.5, -54.5, -45f, -9.5f), new Location(this.gameWorld, -96.5, 75, -77.5));

        this.teams.put(TeamType.BLUE, this.blueTeam);
        this.teams.put(TeamType.RED, this.redTeam);

        this.state = GameState.WAITING_FOR_PLAYERS;
        this.secondsRemaining = this.state.getSeconds();
    }

    public void join(Player player)
    {
        gamers.add(player);
        player.setGameMode(GameMode.ADVENTURE);
        System.out.println("Game> " + player.getName() + " joined");
    }

    public void leave(Player player)
    {
        gamers.remove(player);
        Team playerTeam = getPlayerTeam(player);
        if(playerTeam != null)
        {
            playerTeam.removePlayer(player);
            playersTeams.remove(player.getUniqueId());
        }

        System.out.println("Game> " + player.getName() + " left");
    }

    public Collection<Flag> getAllFlags()
    {
        return this.flagMap.values();
    }

    public Flag getFlag(TeamType teamType)
    {
        return this.flagMap.get(teamType);
    }

    public Team choiceTeam()
    {
        if (redTeam.getSize() > blueTeam.getSize())
        {
            return redTeam;
        }

        return blueTeam;
    }

    private void assignPlayersTeams()
    {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            Team team = choiceTeam();
            team.addPlayer(onlinePlayer);
            this.playersTeams.put(onlinePlayer.getUniqueId(), team.getTeamType());
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
        this.redTeam.getFlag().setUpFlag();
        this.blueTeam.getFlag().setUpFlag();

        this.flagMap.put(TeamType.BLUE, this.blueTeam.getFlag());
        this.flagMap.put(TeamType.RED, this.redTeam.getFlag());

        Bukkit.getPluginManager().registerEvents(new FlagListener(this, CaptureTheFlagPlugin.getInstance()), CaptureTheFlagPlugin.getInstance());
    }

    private void handleTimeoutWinner()
    {
        if(this.redTeam.getTotalKills() == this.blueTeam.getTotalKills())
        {
            // Draw
            winningTeam = null;
        }

        if(this.redTeam.getTotalKills() > this.blueTeam.getTotalKills())
        {
            winningTeam = redTeam;
        }

        if(this.redTeam.getTotalKills() < this.blueTeam.getTotalKills())
        {
            winningTeam = blueTeam;
        }
    }

    @Override
    public void run()
    {
        System.out.println("Game > " + state + " " + secondsRemaining);

        if(state == GameState.WAITING_FOR_PLAYERS)
        {
            int minPlayers = 1;
            if(gamers.size() >= minPlayers)
            {
                System.out.println("has enough players");

                state = GameState.COUNTDOWN;
                secondsRemaining = state.getSeconds();
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

            state = GameState.IN_PROGRESS;
            secondsRemaining = state.getSeconds();
        }

        boolean changeState = secondsRemaining == 0;
        if((winningTeam != null || changeState) && state == GameState.IN_PROGRESS)
        {
            state = GameState.CELEBRATE;
            secondsRemaining = state.getSeconds();

            if(changeState && winningTeam == null )
            {
                handleTimeoutWinner();
            }

            broadcastChampion();
        }

        if(changeState)
        {
            if(state == GameState.COUNTDOWN)
            {
                state = GameState.PREPARE_GAME;
                secondsRemaining = state.getSeconds();
            }

            if(state == GameState.CELEBRATE)
            {
                state = GameState.ENDED;
                secondsRemaining = state.getSeconds();
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

    public int calculateXP(Player player)
    {
        Team playerTeam = getPlayerTeam(player);
        if(playerTeam == winningTeam)
        {
            return 100;
        }

        return 50;
    }

    public Team getPlayerTeam(Player player)
    {
        TeamType teamType = playersTeams.get(player.getUniqueId());
        return teams.get(teamType);
    }

    public void captureFlag(Player flagCarrier)
    {
        Team playerTeam = this.getPlayerTeam(flagCarrier);

        flagCarrier.getWorld().strikeLightningEffect(flagCarrier.getLocation());

        playerTeam.applyKit(flagCarrier);

        winningTeam = playerTeam;
    }

    public GameState getState() {
        return this.state;
    }
}
