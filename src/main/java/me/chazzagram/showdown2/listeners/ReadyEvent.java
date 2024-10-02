package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ReadyEvent implements Listener {

    private static Showdown2 plugin;

    public ReadyEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCrouch(PlayerToggleSneakEvent e){
        if(plugin.runningTimers.containsKey("readytimer")) {
            if (e.isSneaking()) {
                plugin.readyPlayers.put(e.getPlayer().getName(), plugin.readyPlayers.get(e.getPlayer().getName()) + 1);
                if(plugin.readyPlayers.get(e.getPlayer().getName()) < 15){
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, (float)((float)plugin.readyPlayers.get(e.getPlayer().getName())/5));
                }
                if (plugin.readyPlayers.get(e.getPlayer().getName()) == 10) {
                    plugin.messagePlayer(e.getPlayer(), "You are now ready!");
                    e.getPlayer().sendTitle("§a§lYOU'RE READY!", "Good Job!", 0, 60, 40);
                }
            }
        }
    }
}
