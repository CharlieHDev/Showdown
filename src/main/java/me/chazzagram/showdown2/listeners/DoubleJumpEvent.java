package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
        if (p.getGameMode().equals(GameMode.ADVENTURE) && (plugin.currentMode.equals("Zoomo Go") || plugin.currentMode.equals("Slime Golf")) && plugin.doubleJumpEnabled) {
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setVelocity(p.getLocation().getDirection().multiply(1.8).setY(1));
            e.setCancelled(true);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(""));
        }
    }
}
