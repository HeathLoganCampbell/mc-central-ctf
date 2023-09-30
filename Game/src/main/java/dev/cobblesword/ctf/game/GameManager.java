package dev.cobblesword.ctf.game;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.game.commands.GameCommands;
import dev.cobblesword.ctf.game.team.TeamPrefix;
import dev.cobblesword.ctf.game.team.TeamType;
import dev.cobblesword.ctf.lobby.LobbyModule;
import dev.cobblesword.libraries.common.messages.CC;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class GameManager implements Runnable
{
    @Getter
    private Game game;

    @Getter
    private GameState state = GameState.SETTING_UP;

    @Getter
    private int secondsRemaining = -1;

    @Getter
    private List<Player> gamers = new ArrayList<Player>();

    public GameManager(JavaPlugin plugin)
    {
        this.game = new Game(CaptureTheFlagPlugin.getInstance());

        CaptureTheFlagPlugin.getInstance().getCommandFramework().registerCommands(new GameCommands());

        this.setState(GameState.WAITING_FOR_PLAYERS);

        Bukkit.getPluginManager().registerEvents(new GameManagerListener(this), plugin);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20L, 20L);
    }

    public GameState getState() {
        return this.state;
    }

    public void setState(GameState state)
    {
        System.out.println(this.state + " => " + state);
        this.state = state;
        secondsRemaining = state.getSeconds();
    }

    public void join(Player player)
    {
        this.gamers.add(player);

        if(game != null)
        {
            this.game.join(player);
        }
    }

    public void leave(Player player)
    {
        this.gamers.remove(player);

        if(game != null)
        {
            this.game.leave(player);
        }
    }

    public void backToLobby()
    {
        LobbyModule lobbyModule = CaptureTheFlagPlugin.getInstance().getLobbyModule();
        Location spawnLocation = lobbyModule.getSpawnLocation();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if(player.getVehicle() != null)
            {
                player.getVehicle().eject();
                player.eject();
            }

            player.setDisplayName(player.getName());
            lobbyModule.applyLobbyKit(player);
            player.teleport(spawnLocation);
        }
    }

    public void setUpNextGame()
    {
        this.game = new Game(CaptureTheFlagPlugin.getInstance());

        for (Player player : Bukkit.getOnlinePlayers())
        {
            // Add to next game
            this.game.join(player);
        }
    }

    public void handleTabBanner()
    {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            BaseComponent header = new TextComponent("Capture The Flag");
            header.setBold(true);
            header.setColor(ChatColor.GOLD);

            header.addExtra("\n");
            int tabHeaderSize = 80;
            if(state == GameState.WAITING_FOR_PLAYERS)
            {
                header.addExtra(CC.gray + "Waiting for players" + "\n");
            }
            else if(state == GameState.COUNTDOWN)
            {
                header.addExtra(CC.gray + "Starting in " + this.secondsRemaining + " seconds" + "\n");
            }
            else if(state == GameState.PREPARE_GAME)
            {
                header.addExtra(CC.gray + "Good Luck!" + "\n");
            }
            else if(state == GameState.IN_PROGRESS)
            {
                header.addExtra(CC.gray + this.secondsRemaining + " seconds remaining" + "\n");
            }
            else if(state == GameState.CELEBRATE)
            {
                if(this.getGame().getWinningTeam() != null)
                {
                    header.addExtra(CC.gray + this.getGame().getWinningTeam().getChatColor() + "VICTORY" + "\n");
                }
                else
                {
                    header.addExtra(CC.gray + "DRAW!" + "\n");
                }
            }

            BaseComponent footer = new TextComponent("\nCobbleSword.Com\n");
            footer.setBold(true);
            footer.setColor(ChatColor.AQUA);

            onlinePlayer.setPlayerListHeaderFooter(header, footer);
        }
    }

    @Override
    public void run()
    {
        int minPlayers = 2;

        handleTabBanner();

        if(state == GameState.WAITING_FOR_PLAYERS)
        {
            if(gamers.size() >= minPlayers)
            {
                System.out.println("has enough players");

                this.setState(GameState.COUNTDOWN);
            }
        }

        if(state == GameState.COUNTDOWN)
        {
            if(gamers.size() < minPlayers)
            {
                Bukkit.broadcastMessage(CC.red + "No enough players to start. Requires " + minPlayers + " players to start!");
                this.setState(GameState.WAITING_FOR_PLAYERS);
            }
        }

        if(state == GameState.CELEBRATE)
        {
            if(game.hasWinner())
            {
                for (UUID winnerUUID : this.game.getWinningTeam().getPlayers())
                {
                    Player player = Bukkit.getPlayer(winnerUUID);
                    Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
                    FireworkMeta meta = firework.getFireworkMeta();
                    meta.addEffect(FireworkEffect.builder()
                            .withColor(this.game.getWinningTeam().getDyeColor())
                            .with(FireworkEffect.Type.BALL)
                            .build());
                    firework.setFireworkMeta(meta);
                }
            }
        }

        if(state == GameState.PREPARE_GAME)
        {
            this.game.onStart();
            this.setState(GameState.IN_PROGRESS);
            this.game.setInProgress(true);

            List<Player> allPlayers = new ArrayList<>();
            List<Player> redPlayers = new ArrayList<>();
            List<Player> bluePlayers = new ArrayList<>();

            for (UUID playerId : this.getGame().getTeam(TeamType.RED).getPlayers()) {
                Player player = Bukkit.getPlayer(playerId);
                if(player != null)
                {
                    allPlayers.add(player);
                    redPlayers.add(player);
                }
            }

            for (UUID playerId : this.getGame().getTeam(TeamType.BLUE).getPlayers()) {
                Player player = Bukkit.getPlayer(playerId);
                if(player != null)
                {
                    allPlayers.add(player);
                    bluePlayers.add(player);
                }
            }

            TeamPrefix.createTeamWithPlayer(CC.red, "red_team", redPlayers, allPlayers);
            TeamPrefix.createTeamWithPlayer(CC.blue, "blue_team", bluePlayers, allPlayers);
        }

        boolean changeState = secondsRemaining == 0;
        if((game.hasWinner() || changeState) && state == GameState.IN_PROGRESS)
        {
            this.setState(GameState.CELEBRATE);

            if(changeState && !game.hasWinner() )
            {
                game.onFinish(FinishReason.TIME_OUT);
            }
            else
            {
                game.onFinish(FinishReason.FLAG_CAPTURED);
            }
        }

        if(changeState)
        {
            if(state == GameState.COUNTDOWN)
            {
                this.setState(GameState.PREPARE_GAME);
            }

            if(state == GameState.CELEBRATE)
            {
                this.game.onExit();
                this.setState(GameState.WAITING_FOR_PLAYERS);
                backToLobby();
                setUpNextGame();
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

                if(Stream.of(1, 2, 3, 4, 5, 10).anyMatch(sec -> secondsRemaining == sec))
                {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                    }
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
}
