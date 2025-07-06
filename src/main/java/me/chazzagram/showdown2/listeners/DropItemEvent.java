package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemEvent implements Listener {

    private static Showdown2 plugin;

    public DropItemEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDropItem(final PlayerDropItemEvent event) {
        if(plugin.getPlayers().contains(event.getPlayer())) {
            switch (plugin.currentMode) {
                case "Zoomo Go":
                case "Gub Game":
                case "Colour Dash":
                case "Craftalot":
                case "Slime Golf":
                case "Lobby":
                case "Voting":
                    event.setCancelled(true);
                    break;
            }
        }
    }
}
