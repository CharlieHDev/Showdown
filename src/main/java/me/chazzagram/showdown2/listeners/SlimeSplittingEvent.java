package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CubeMobSplitEvent;
import org.bukkit.event.entity.SlimeSplitEvent;

public class SlimeSplittingEvent implements Listener {

    private static Showdown2 plugin;

    public SlimeSplittingEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSlimeSplit(CubeMobSplitEvent event) {
        event.setCancelled(true);
    }
}
