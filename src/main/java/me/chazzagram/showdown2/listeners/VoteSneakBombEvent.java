package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.TeleportConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.AbstractMap;

public class VoteSneakBombEvent implements Listener {

    private static Showdown2 plugin;

    public VoteSneakBombEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void voteSneakBombEvent(PlayerToggleSneakEvent e) {

        if(plugin.votingEnabled){
            if(!e.getPlayer().isSneaking()){
                BukkitTask task = new BukkitRunnable() {
                    int timeLeft = 0;
                    @Override
                    public void run() {
                        if(!plugin.pausedTimers.contains("voting") && plugin.votingEnabled) {
                            timeLeft++;
                            StringBuilder progress = new StringBuilder();
                            if(timeLeft < 20) {
                                progress.append("§e|".repeat(timeLeft));
                                progress.append("§8|".repeat(20 - timeLeft));
                            } else {
                                progress.append("§a|".repeat(20));
                            }
                            e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§e✴ §f» " + progress));
                            if(!e.getPlayer().isSneaking()){
                                if(timeLeft > 20){
                                    plugin.messagePlayer(e.getPlayer(), "§eSuccessful explosion!");
                                    cancel();
                                } else {
                                    StringBuilder progressfail = new StringBuilder();
                                    progressfail.append("§c|".repeat(timeLeft));
                                    progressfail.append("§8|".repeat(20 - timeLeft));
                                    e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§e✴ §f» " + progressfail));
                                    cancel();
                                }
                            }
                        } else {
                            cancel();
                        }
                    }

                }.runTaskTimer(plugin, 0L, 2L);

            }
        }

    }
}
