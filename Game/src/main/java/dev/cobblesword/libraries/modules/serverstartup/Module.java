package dev.cobblesword.libraries.modules.serverstartup;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Module implements Listener
{
    private String name;

    private JavaPlugin plugin;

    public Module(String name, JavaPlugin plugin) {
        this.name = name;
        this.plugin = plugin;
    }

    protected abstract void onEnable();

    public void enable()
    {
        System.out.println(name + ") Starting up...");
        onEnable();
        System.out.println(name + ") Started.");
    }

    protected void registerEvents(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, CaptureTheFlagPlugin.getInstance());
    }
}
