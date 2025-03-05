package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.TeleportConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

        if(plugin.votingEnabled && plugin.powerUpHolders.contains(e.getPlayer().getName())){
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
                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, (0.075f*timeLeft)+0.5f);
                            } else {
                                progress.append("§a|".repeat(20));
                            }
                            e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§e✴ §f» " + progress));
                            if(!e.getPlayer().isSneaking()){
                                if(timeLeft > 20){
                                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_SHULKER_SHOOT, 1f, 1.2f);
                                    plugin.messagePlayer(e.getPlayer(), "§eSuccessful explosion!");
                                    Location blockBelow = e.getPlayer().getLocation().clone().subtract(3, 1, 3);
                                    for(int i = 0; i <= 6; i++){
                                        for(int j = 0; j <= 6; j++){
                                            Block currentBlock = blockBelow.clone().add(i, 0, j).getBlock();
                                            for (Material wool : getWoolColors()) {
                                                if (currentBlock.getType().equals(wool)) {
                                                    currentBlock.setType(plugin.playerVote.get(e.getPlayer()));
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    Particle.DustOptions dustOptions = new Particle.DustOptions(plugin.woolColors.get(plugin.playerVote.get(e.getPlayer())), 4);

                                    e.getPlayer().getWorld().spawnParticle(Particle.DUST, e.getPlayer().getLocation(), 300, 1.5, 0.0, 1.5, 1, dustOptions, false);
                                    plugin.powerUpHolders.remove(e.getPlayer().getName());
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

    private Material[] getWoolColors() {
        return new Material[]{
                Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL,
                Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL,
                Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL,
                Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL,
                Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL,
                Material.BLACK_WOOL
        };
    }

}
