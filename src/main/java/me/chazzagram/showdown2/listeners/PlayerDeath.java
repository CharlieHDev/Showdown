package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.DeathMessagesConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.EventListener;
import java.util.List;
import java.util.Random;

public class PlayerDeath implements Listener {

    private static Showdown2 plugin;

    public PlayerDeath(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Player killer = e.getEntity().getKiller();

        if(killer != null) {
            List<String> deathMessages = DeathMessagesConfig.get().getStringList("kills");
            Random rand = new Random();
            String message = deathMessages.get(rand.nextInt(deathMessages.size()));

            for (Player p : plugin.getServer().getOnlinePlayers()) {
                plugin.messagePlayer(p, String.format(message, plugin.getPlayerDisplayName(player.getName()), plugin.getPlayerDisplayName(killer.getName())));
            }
        }
    }
}
