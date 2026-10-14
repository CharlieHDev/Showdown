package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;

public class HarmlessTntListener implements Listener {

    private final NamespacedKey key;

    public HarmlessTntListener(Showdown2 plugin) {
        this.key = new NamespacedKey(plugin, "harmless_tnt");
    }

    private boolean isHarmless(Entity e) {
        return e instanceof TNTPrimed;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (isHarmless(event.getEntity())) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (isHarmless(event.getDamager())) {
            event.setCancelled(true);
        }
    }
}
