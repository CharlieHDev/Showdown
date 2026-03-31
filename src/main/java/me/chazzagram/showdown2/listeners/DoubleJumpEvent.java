package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.AbstractMap;

public class DoubleJumpEvent implements Listener {

    private static Showdown2 plugin;

    public DoubleJumpEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent e) {

        if(plugin.ghostManager.getGhostPlayers().contains(e.getPlayer().getName())) return;

        Player p = e.getPlayer();
        if ((e.getPlayer().getGameMode().equals(GameMode.ADVENTURE) || e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) && (plugin.currentMode.equals("Zoomo Go") || plugin.currentMode.equals("Slime Golf")) && plugin.doubleJumpEnabled) {
            p.setAllowFlight(false);
            p.setFlying(false);
            if(plugin.currentMode.equals("Slime Golf") && plugin.currentRound > 1){
                p.setVelocity(p.getLocation().getDirection().multiply(1.4).setY(1));
            } else {
                p.setVelocity(p.getLocation().getDirection().multiply(1.8).setY(1));
            }
            e.setCancelled(true);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 10, 2F);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(""));
            String name = p.getName() + "dj";
            BukkitTask task = new BukkitRunnable() {
                int timeLeft = 5;
                @Override
                public void run() {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        if (timeLeft == 0) {
                            plugin.runningTimers.remove(name);
                            cancel();
                        }
                    } else {
                        cancel();
                    }
                }

            }.runTaskTimer(plugin, 0L, 1L);
            plugin.runningTimers.put(p.getName() + "dj", new AbstractMap.SimpleEntry<>(task, 5));
        }
    }
}
