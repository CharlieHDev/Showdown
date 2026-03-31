package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FireballEvent implements Listener {

    private static Showdown2 plugin;

    public FireballEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFireballExplode(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof Fireball)) return;

        Fireball fireball = (Fireball) event.getEntity();

        if(plugin.fireballSenders.containsKey(fireball)) {
            String player = plugin.fireballSenders.get(fireball).getName();
            for (Block block : event.blockList()) {
                for (Player target : block.getWorld().getPlayers()) {
                    if (target.equals(player)) continue;

                    Location feet = target.getLocation();
                    if (feet.getBlock().getRelative(BlockFace.DOWN).equals(block)) {
                        plugin.crumbleKillTracker.put(
                                target.getName(),
                                new CrumbleKillData(player, System.currentTimeMillis())
                        );
                    }
                }
            }
        }
    }
}
