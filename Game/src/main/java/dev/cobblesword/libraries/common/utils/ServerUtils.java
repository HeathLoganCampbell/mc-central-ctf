package dev.cobblesword.libraries.common.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class ServerUtils
{
    public static void runCommand(String command)
    {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, command);
    }

}
