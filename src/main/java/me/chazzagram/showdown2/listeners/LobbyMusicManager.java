package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LobbyMusicManager implements Listener {

    private final Showdown2 plugin;

    public LobbyMusicManager(Showdown2 plugin) {
        this.plugin = plugin;
    }


    Map<UUID, BukkitTask> musicTasks = new HashMap<>();

    public void startMusic(Player player) {
        if(plugin.currentMode.equals("Lobby")) {
            if(!musicTasks.containsKey(player.getUniqueId())) {
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.playSound(player.getLocation(),
                                Sound.MUSIC_DISC_WARD,
                                SoundCategory.VOICE,
                                1f,
                                1f);
                    }
                }.runTaskTimer(plugin, 0L, 20L * 205);

                musicTasks.put(player.getUniqueId(), task);
            }
        }
    }

    public void stopMusic(Player player) {
        BukkitTask task = musicTasks.remove(player.getUniqueId());
        if (task != null) task.cancel();
        player.stopSound(Sound.MUSIC_DISC_WARD, SoundCategory.VOICE);
    }

    public Map<UUID, BukkitTask> getMusicTasks() {
        return musicTasks;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            startMusic(p);
        }, 300L);
    }
}
