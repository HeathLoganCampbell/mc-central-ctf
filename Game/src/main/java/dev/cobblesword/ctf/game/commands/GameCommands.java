package dev.cobblesword.ctf.game.commands;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.game.Game;
import dev.cobblesword.ctf.game.GameManager;
import dev.cobblesword.ctf.game.GameState;
import dev.cobblesword.ctf.game.team.Team;
import dev.cobblesword.ctf.game.team.TeamType;
import dev.cobblesword.libraries.common.commandframework.Command;
import dev.cobblesword.libraries.common.commandframework.CommandArgs;
import dev.cobblesword.libraries.common.messages.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameCommands
{
    @Command(name = "game", aliases = { "ctf", "CaptureTheFlag" }, description = "This is a test command", usage = "This is how you use it")
    public void testCommands(CommandArgs args)
    {
        args.getSender().sendMessage("This is a test command");
    }

    @Command(name = "game.start", aliases = { "ctf.start", "CaptureTheFlag.start" }, description = "This is a test command", usage = "This is how you use it" , permission = "capturetheflag.start")
    public void onStartGame(CommandArgs args)
    {
        GameManager gameManager = CaptureTheFlagPlugin.getInstance().getGameManager();
        gameManager.setState(GameState.PREPARE_GAME);

        Bukkit.broadcastMessage(CC.bdPurple + "# # # # # # # # # # # # # # #");
        Bukkit.broadcastMessage(CC.bdPurple + "# #  FORCE STARTED GAME   # #");
        Bukkit.broadcastMessage(CC.bdPurple + "# # # # # # # # # # # # # # #");
        args.getSender().sendMessage("Starting game!");
    }

    @Command(name = "game.remake", aliases = { "ctf.remake", "CaptureTheFlag.remake" }, description = "This is a test command", usage = "This is how you use it" , permission = "capturetheflag.start")
    public void onRemakeGame(CommandArgs args)
    {
        GameManager gameManager = CaptureTheFlagPlugin.getInstance().getGameManager();

        gameManager.getGame().onExit();
        gameManager.setState(GameState.WAITING_FOR_PLAYERS);
        gameManager.backToLobby();
        gameManager.setUpNextGame();

        Bukkit.broadcastMessage(CC.bdPurple + "# # # # # # # # # # # #");
        Bukkit.broadcastMessage(CC.bdPurple + "# #   GAME REMAKE   # #");
        Bukkit.broadcastMessage(CC.bdPurple + "# # # # # # # # # # # #");
        args.getSender().sendMessage("Starting game!");
    }

    @Command(name = "game.add", aliases = { "ctf.add", "CaptureTheFlag.add" }, description = "Add Player to team even if it's inprogress", usage = "/game add <Player> <RED|BLUE>", permission = "capturetheflag.add")
    public void onAddPlayer(CommandArgs args)
    {
        Game game = CaptureTheFlagPlugin.getInstance().getGameManager().getGame();

        if(args.length() != 2)
        {
            args.getSender().sendMessage("/game add <Player> <RED|BLUE>");
            return;
        }

        String playerName = args.getArgs(0);
        String teamTypeName = args.getArgs(1);

        Player player = Bukkit.getPlayer(playerName);
        if(player == null)
        {
            args.getSender().sendMessage("player is not online");
            return;
        }

        Team oldTeam = game.getPlayerTeam(player);
        if(oldTeam != null)
        {
            game.leave(player);
        }

        try {
            TeamType teamType = TeamType.valueOf(teamTypeName.toUpperCase());

            game.join(player);
            game.getTeamManager().addPlayerToTeam(teamType, player);
            Team team = game.getPlayerTeam(player);

            if(game.isInProgress())
            {
                team.spawn(player);
            }

            player.sendMessage(CC.green + "You have been added to the " + team.getName() + " team");
            args.getSender().sendMessage(CC.green + "add " + player.getName() + " to the " + teamType.name() + " team");
        } catch (IllegalArgumentException e) {
            args.getSender().sendMessage("Team can only be RED or BLUE");
            return;
        }
    }
}
