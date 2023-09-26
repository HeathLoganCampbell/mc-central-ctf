package dev.cobblesword.libraries.common.utils;

import dev.cobblesword.libraries.common.task.Sync;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginUtils {

    public static void registerListener(Listener listener, JavaPlugin plugin)
    {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static void unregisterListener(Listener listener)
    {
        HandlerList.unregisterAll(listener);
    }

    public static void call(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void syncCall(Event event) {
        Sync.get().run(() -> {
            PluginUtils.call(event);
        }).execute();
    }
}