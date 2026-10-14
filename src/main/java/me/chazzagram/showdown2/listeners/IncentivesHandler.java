package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class IncentivesHandler {

    private static Showdown2 plugin;

    public IncentivesHandler(Showdown2 plugin) {
        this.plugin = plugin;
    }

    List<String> incentivesText = List.of(
            "§c§lPower Punch", // Zoomo
            "§e§lRandom Sizer", // Crumble
            "§b§lExtra Modifiers", // Sulfur
            "§a§lSpeed Up", // Push
            "§a§lPunch Up"); // Craftalot

    HashMap<String, String> incentives = new HashMap<>();


    public void displayIncentives(String incentive) {

        incentives.put("TNT", "§c§lPower Punch"); // Zoomo
        incentives.put("Size", "§e§lRandom Sizer"); // Crumble
        incentives.put("Modifiers", "§b§lExtra Modifiers"); // Sulfur
        incentives.put("Speed", "§a§lSpeed Up"); // Push
        incentives.put("Punch", "§a§lPunch Up"); // Craftalot

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 81;
            @Override
            public void run() {
                if (plugin.runningTimers.containsKey("incentivepicker")) {
                    if (!plugin.pausedTimers.contains("incentivepicker")) {
                        timeLeft--;
                        plugin.runningTimers.get("incentivepicker").setValue(timeLeft);
                        plugin.bossBarBgTest();

                        switch (timeLeft) {
                            case 80,79,78,77, 76,75,74,73, 72, 70, 68, 66, 64, 62, 59, 56, 52, 48, 44, 40, 35, 30, 20, 10:
                                float pitch = 0.5f + (timeLeft - 10) / 70f;
                                plugin.playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, pitch);

                                Random r = new Random();

                                for(Player p : Bukkit.getOnlinePlayers()) {
                                    p.sendTitle("§f§lɪɴᴄᴇɴᴛɪᴠᴇ ʀᴇᴀᴄʜᴇᴅ", incentivesText.get(r.nextInt(incentivesText.size())), 0, 20, 20);
                                }
                                break;
                            case 0:
                                for(Player p : Bukkit.getOnlinePlayers()) {
                                    p.sendTitle("§e§lɪɴᴄᴇɴᴛɪᴠᴇ ʀᴇᴀᴄʜᴇᴅ", incentives.get(incentive), 0, 40, 20);
                                }

                                plugin.playSoundAll(Sound.ENTITY_ENDER_DRAGON_GROWL, 1f);

                                switch (incentive) {
                                    case "TNT":
                                    case "Sizer":
                                    case "Modifiers":
                                    case "Speed":
                                    case "Punch":
                                        plugin.incentivesActive.put(incentive, true);
                                    default:
                                        break;
                                }
                                plugin.runningTimers.remove("incentivepicker");
                                cancel();
                                break;
                        }
                    } else {
                        plugin.messageConsole("Timer removed by external factor.");
                        cancel();
                    }
                }
            }

        }.runTaskTimer(plugin, 0L, 1L);

        plugin.runningTimers.put("incentivepicker", new AbstractMap.SimpleEntry<>(task, 81));
    }

    public void clearIncentives() {
        for(Player p : plugin.getPlayers()) {
            Objects.requireNonNull(p.getAttribute(Attribute.SCALE)).setBaseValue(1);
            plugin.incentivesActive.replaceAll((i, b) -> false);
        }
    }


}
