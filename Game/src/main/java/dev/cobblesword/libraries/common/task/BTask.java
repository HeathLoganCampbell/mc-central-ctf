package dev.cobblesword.libraries.common.task;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BTask
{
    private BukkitRunnable task;
    private boolean async = false;
    private int delay = 0;
    private int interval = 0;

    public BTask runnable(Runnable runnable)
    {
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        return this;
    }

    public BTask run(Runnable runnable)
    {
        return this.runnable(runnable);
    }

    public BTask async()
    {
        this.async = true;
        return this;
    }

    public BTask sync()
    {
        this.async = false;
        return this;
    }

    public BTask delay(int delayTicks)
    {
        this.delay = delayTicks;
        return this;
    }

    public BTask interval(int intervalTicks)
    {
        this.interval = intervalTicks;
        return this;
    }

    public void cancel()
    {
        if(this.task != null)
            Bukkit.getScheduler().cancelTask(this.task.getTaskId());
    }

    public void execute()
    {
        JavaPlugin plugin = CaptureTheFlagPlugin.getInstance();
        if(async)
        {
            if(delay == 0)
            {
                if (interval == 0)
                    this.task.runTaskAsynchronously(plugin);
                else
                    this.task.runTaskTimerAsynchronously(plugin, delay, interval);
            }
            else
            {
                if (interval == 0)
                    this.task.runTaskLaterAsynchronously(plugin, delay);
                else
                    this.task.runTaskTimerAsynchronously(plugin, delay, interval);
            }
        }
        else
        {
            if(delay == 0)
            {
                if (interval == 0)
                    this.task.runTask(plugin);
                else
                    this.task.runTaskTimer(plugin, delay, interval);
            }
            else
            {
                if(interval == 0)
                    this.task.runTaskLater(plugin, delay);
                else
                    this.task.runTaskTimer(plugin, delay, interval);
            }
        }
    }
}
