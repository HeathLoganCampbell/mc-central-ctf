package dev.cobblesword.libraries.modules.levels.commands;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.data.playerdata.types.PlayerData;
import dev.cobblesword.libraries.common.commandframework.Command;
import dev.cobblesword.libraries.common.commandframework.CommandArgs;
import dev.cobblesword.libraries.common.messages.CC;
import dev.cobblesword.libraries.modules.levels.LevelModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelsCommand
{
    private LevelModule levelModule;

    public LevelsCommand(LevelModule levelModule) {
        this.levelModule = levelModule;
    }

    @Command(name = "level.addexp", aliases = { "lvl.addexp" }, description = "Add experience to a player", usage = "/level addexp <Player> <expAmount>", permission = "levels.addexp")
    public void onAddExp(CommandArgs args) {
        if(args.length() != 2) {
            args.getSender().sendMessage("/level addexp <Player> <expAmount>");
            return;
        }

        String playerName = args.getArgs(0);
        Player player = Bukkit.getPlayer(playerName);
        if(player == null) {
            args.getSender().sendMessage("Player is not online");
            return;
        }

        try {
            int expAmount = Integer.parseInt(args.getArgs(1));
            this.levelModule.addExp(player, expAmount);
            args.getSender().sendMessage(CC.green + "Added " + expAmount + " experience to " + playerName);
        } catch (NumberFormatException e) {
            args.getSender().sendMessage("Experience amount must be a number");
        }
    }

    @Command(name = "level.expneeded", aliases = { "lvl.expneeded" }, description = "View experience needed for a level", usage = "/level expneeded <level>", permission = "levels.expneeded")
    public void onExpNeeded(CommandArgs args) {
        if(args.length() != 1) {
            args.getSender().sendMessage("/level expneeded <level>");
            return;
        }

        try {
            int level = Integer.parseInt(args.getArgs(0));
            int expNeeded = levelModule.getExpRequired(level);
            args.getSender().sendMessage(CC.green + "Experience needed for level " + level + ": " + expNeeded);
        } catch (NumberFormatException e) {
            args.getSender().sendMessage("Level must be a number");
        }
    }

    @Command(name = "level", aliases = { "lvl" }, description = "View experience needed for a level", usage = "/level expneeded <level>", permission = "levels.expneeded")
    public void onLevel(CommandArgs args) {
        Player targetPlayer = args.getPlayer();

        PlayerData playerData = CaptureTheFlagPlugin.getPlayerDataManager().getPlayerData(targetPlayer.getUniqueId());

        CommandSender sender = args.getSender();
        sender.sendMessage("Name: " + targetPlayer.getName());
        sender.sendMessage("Level: " + playerData.getLevel());
        sender.sendMessage("Exp: " + playerData.getExp() + " / " + levelModule.getExpRequired(playerData.getLevel() + 1));
    }
}
