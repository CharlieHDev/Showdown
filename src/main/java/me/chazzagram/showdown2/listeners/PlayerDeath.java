package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.EventListener;

public class PlayerDeath implements EventListener {

    private static Showdown2 plugin;

    public PlayerDeath(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Player killer = e.getEntity().getKiller();

        for(Player p : plugin.getServer().getOnlinePlayers()) {
            plugin.messagePlayer(p, player + " was eliminated by " + killer);
        }
    }
}
