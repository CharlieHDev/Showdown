package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJumpEvent implements Listener {

    private static Showdown2 plugin;

    public DoubleJumpEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.ADVENTURE) && plugin.currentMode.equals("Zoomo Go") && plugin.doubleJumpEnabled) {
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setVelocity(p.getLocation().getDirection().multiply(1.8).setY(1));
            e.setCancelled(true);
        }
    }
}
